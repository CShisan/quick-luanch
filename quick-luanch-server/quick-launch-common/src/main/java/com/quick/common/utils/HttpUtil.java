package com.quick.common.utils;

import com.quick.common.base.Result;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ServiceException;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author CShisan
 */
public class HttpUtil {
    /**
     * 获取当前请求的HttpServletRequest实例
     * RequestContextHolder.getRequestAttributes()在非控制层获取需要开启RequestContextListener,否则为空
     */
    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(requestAttributes).map(item ->
                ((ServletRequestAttributes) item).getRequest()
        ).orElse(null);
    }

    /**
     * 获取请求头
     *
     * @see HttpUtil#getHeaders(HttpServletRequest)
     */
    public static Map<String, String> getHeaders() {
        HttpServletRequest request = getHttpServletRequest();
        return Optional.ofNullable(request).map(HttpUtil::getHeaders).orElse(null);
    }

    /**
     * 获取请求头
     */
    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 获取Body
     */
    public static String getBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            ServletInputStream is = request.getInputStream();
            InputStreamReader isReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isReader);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new ServiceException(CodeEnum.FAIL, "请求获取Body失败", e);
        }
        return sb.toString();
    }

    /**
     * 根据name获取cookie
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (Objects.equals(name, cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * 获取当前用户ID
     *
     * @see HttpUtil#currentUid()
     */
    public static Long currentUidWithException() {
        return Optional.ofNullable(currentUid()).orElseThrow(() ->
                new ServiceException(CodeEnum.SERVICE_DATA_NULL, "获取用户ID失败, 请重新登录")
        );
    }

    /**
     * 获取当前用户ID
     */
    public static Long currentUid() {
        return Optional.ofNullable(getHttpServletRequest())
                .flatMap(item -> Optional.ofNullable(item.getAttribute("uid")))
                .map(String::valueOf).map(Long::valueOf).orElse(null);
    }

    /**
     * 写入response
     */
    public static void writeResponse(HttpServletResponse response, Result<?> result) throws IOException {
        response.setCharacterEncoding(Consts.UTF_8.name());
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.setStatus(HttpServletResponse.SC_OK);

        // 这里需要使用JackJson进行转换,因为有使用到Long转String的序列化工具
        response.getWriter().write(JsonUtil.toJson(result));
    }
}
