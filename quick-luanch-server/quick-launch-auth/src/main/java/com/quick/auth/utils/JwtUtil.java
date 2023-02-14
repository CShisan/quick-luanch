package com.quick.auth.utils;

import com.quick.auth.entity.LoginEntity;
import com.quick.common.config.JwtConfig;
import com.quick.common.enums.CodeEnum;
import com.quick.common.enums.RedisKeyEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.CheckUtil;
import com.quick.common.utils.IpUtil;
import com.quick.common.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author CShisan
 */
@Component
public class JwtUtil {
    private final JwtConfig config;
    private final RedisUtil redisUtil;
    private final String tokenKey;
    private final String refreshTokenKey;
    private final String tokenValuePrefix;

    @Autowired
    public JwtUtil(JwtConfig config, RedisUtil redisUtil) {
        this.config = config;
        this.redisUtil = redisUtil;
        this.tokenKey = config.getTokenKey();
        this.refreshTokenKey = config.getRefreshTokenKey();
        this.tokenValuePrefix = config.getTokenValuePrefix().concat(" ");
    }

    /**
     * 生成token
     *
     * @param entity LoginEntity
     * @return token
     */
    public String token(LoginEntity entity) {
        return generate(toClaims(entity), tokenExpTime());
    }

    /**
     * 生成refresh-token
     *
     * @param entity LoginEntity
     * @return refresh-token
     */
    public String refreshToken(LoginEntity entity) {
        return generate(toClaims(entity), refreshTokenExpTime());
    }

    /**
     * entity转换claims
     *
     * @param entity LoginEntity
     * @return claims
     */
    public Map<String, Object> toClaims(LoginEntity entity) {
        ExceptionHandler.requireNotNull(entity);
        Map<String, Object> claims = new HashMap<>(16);
        claims.put("uid", entity.getUid());
        claims.put("username", entity.getUsername());
        claims.put("roles", entity.getRoles());
        claims.put("permissions", entity.getPermissions());
        claims.put("ip", IpUtil.getIpAddress());
        return claims;
    }

    /**
     * 根据claims生成token
     *
     * @param claims  claims
     * @param expTime 失效时间
     * @return token
     */
    private String generate(Map<String, Object> claims, Date expTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expTime)
                .signWith(SignatureAlgorithm.HS512, config.getSecret())
                .compact();
    }

    /**
     * 通过token获取荷载
     *
     * @param token token
     * @return 荷载claims
     */
    public Claims getClaimsByToken(String token) {
        // 若token有前缀要先剪除 不然报错
        token = tokenValue(token);
        ExceptionHandler.requireNotBlank(token);
        return Jwts.parser()
                .setSigningKey(config.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析token
     *
     * @param token    token
     * @param attrName attrName
     * @return value
     */
    public Object parse(String token, String attrName) {
        return Optional.ofNullable(token).map(this::getClaimsByToken)
                .map(item -> item.get(attrName)).orElse(null);
    }

    /**
     * 验证一致性和时效性
     *
     * @param tokenKey tokenKey
     * @param token    token
     * @return status
     */
    public boolean validAndNotExp(String tokenKey, String token) {
        return validate(tokenKey, token) && isNotExpired(token);
    }

    /**
     * 获取缓存token验证
     *
     * @param token token
     * @return status
     */
    public boolean validate(String tokenKey, String token) {
        ExceptionHandler.requireNotBlank(tokenKey, CodeEnum.USER_AUTH_ERROR, "token前缀不能为空");
        token = tokenValue(token);
        Long uid = Long.parseLong(String.valueOf((int) parse(token, "uid")));
        String cacheToken = getCacheToken(tokenKey, uid);
        return Objects.equals(token, cacheToken) && isNotExpired(token);
    }

    /**
     * 验证是否过期
     *
     * @param token token
     * @return status
     */
    public boolean isNotExpired(String token) {
        Date expireTime = getExpTimeByToken(token);
        return new Date().before(expireTime);
    }

    /**
     * 通过token获取失效时间
     *
     * @param token token
     * @return 失效时间
     */
    public Date getExpTimeByToken(String token) {
        Claims claims = getClaimsByToken(token);
        ExceptionHandler.requireNotNull(claims);
        return claims.getExpiration();
    }

    /**
     * 获取Redis缓存的token
     *
     * @param key key
     * @param uid uid
     * @return cacheToken
     */
    public String getCacheToken(String key, Long uid) {
        String redisKey = redisKey(key, uid);
        String cacheToken = redisUtil.get(redisKey);
        return CheckUtil.isBlank(cacheToken) ? null : cacheToken;
    }

    /**
     * 添加token到Redis缓存
     *
     * @param user userLoginEntity
     */
    public Map<String, Object> putCacheToken(LoginEntity user) {
        if (CheckUtil.anyNull(user, user.getUid())) {
            return null;
        }
        Long uid = user.getUid();
        String token = getCacheToken(tokenKey, uid);

        Map<String, Object> result = new HashMap<>(16);
        String refreshToken;
        if (CheckUtil.isBlank(token)) {
            // 生成token
            token = token(user);
            refreshToken = refreshToken(user);
            setToken(tokenKey, uid, token, config.getTokenExp());
            setToken(refreshTokenKey, uid, refreshToken, config.getRefreshTokenExp());
        } else {
            refreshToken = getCacheToken(refreshTokenKey, uid);
        }
        result.put(tokenKey(), token);
        result.put(refreshTokenKey(), refreshToken);
        return result;
    }

    /**
     * 获取token的key(包含前缀)
     *
     * @param uid uid
     * @return fullKey
     */
    public String fullTokenKey(Long uid) {
        return getFullTokenKey(tokenKey, uid);
    }


    /**
     * 获取刷新token的key(包含前缀)
     *
     * @param uid uid
     * @return fullKey
     */
    public String fullRefreshTokenKey(Long uid) {
        return getFullTokenKey(refreshTokenKey, uid);
    }

    /**
     * 获取token的key(包含前缀)
     *
     * @param prefix prefix
     * @param uid    uid
     * @return fullKey
     */
    private String getFullTokenKey(String prefix, Long uid) {
        return prefix.concat(String.valueOf(uid));
    }

    /**
     * 更新token到redis
     *
     * @param prefix prefix
     * @param uid    uid
     * @param value  value
     * @param exp    exp
     */
    private void setToken(String prefix, Long uid, String value, Long exp) {
        String redisKey = redisKey(prefix, uid);
        redisUtil.set(redisKey, value, exp);
    }

    /**
     * 删除token
     *
     * @param uid uid
     */
    public void deleteToken(Long uid) {
        String redisTokenKey = redisKey(tokenKey, uid);
        String redisRefreshTokenKey = redisKey(refreshTokenKey, uid);
        redisUtil.batchDelete(redisTokenKey, redisRefreshTokenKey);
    }

    /**
     * 拼接redis前缀
     *
     * @param prefix prefix
     * @param uid    uid
     * @return key
     */
    private String redisKey(String prefix, Long uid) {
        return RedisKeyEnum.USER_AUTH_TOKEN.format(prefix, uid);
    }

    /**
     * 获取配置信息
     *
     * @return config
     */
    public JwtConfig config() {
        return config;
    }

    /**
     * 获取token值(不包含前缀)
     *
     * @param token token
     * @return token
     */
    public String tokenValue(String token) {
        return token.replaceFirst(tokenValuePrefix, "");
    }


    /**
     * 生成token失效时间
     *
     * @see JwtUtil#expTime(Long)
     */
    private Date tokenExpTime() {
        return expTime(config.getTokenExp());
    }

    /**
     * 生成refresh-token失效时间
     *
     * @see JwtUtil#expTime(Long)
     */
    private Date refreshTokenExpTime() {
        return expTime(config.getRefreshTokenExp());
    }

    /**
     * 生成失效时间
     *
     * @return 失效时间
     */
    private Date expTime(Long exp) {
        return new Date(System.currentTimeMillis() + exp);
    }

    /**
     * 获取tokenKey
     *
     * @return key
     */
    public String tokenKey() {
        return tokenKey;
    }

    /**
     * 获取刷新token
     *
     * @return key
     */
    public String refreshTokenKey() {
        return refreshTokenKey;
    }

    /**
     * 获取token值前缀
     *
     * @return prefix
     */
    public String tokenValuePrefix() {
        return tokenValuePrefix;
    }
}
