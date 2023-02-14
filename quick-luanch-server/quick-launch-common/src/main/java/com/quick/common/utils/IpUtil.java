package com.quick.common.utils;

import com.quick.common.handler.LogHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author CShisan
 */
@Slf4j
public class IpUtil {
    private static final int IP_MIN_LENGTH = 15;
    private static final String IP_2_REGION_DB_FILE_PATH = "classpath:ip2region/ip2region.xdb";

    /**
     * 获取请求主机ip地址,如果代理进来,则透过防火墙获取真实ip地址
     *
     * @return ip
     */
    public static String getIpAddress() {
        HttpServletRequest request = HttpUtil.getHttpServletRequest();
        LogHandler.assertTrue(log::error, CheckUtil.nonNull(request), LogHandler.format("用户信息-更新最后登录地址", "获取HttpServletRequest为空"));

        String ip = Optional.ofNullable(request).map(IpUtil::getIpAddress).orElse(null);
        LogHandler.assertTrue(log::error, CheckUtil.nonBlank(ip), LogHandler.format("用户信息-更新最后登录地址", "IP获取失败,request{}"), request);

        return ip;
    }

    /**
     * 获取请求主机ip地址,如果代理进来,则透过防火墙获取真实ip地址
     *
     * @param request request
     * @return ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        for (IpTypeEnum type : IpTypeEnum.values()) {
            if (Objects.equals(type, IpTypeEnum.UNKNOWN)) {
                continue;
            }
            ip = request.getHeader(type.value);
            if (CheckUtil.isBlank(ip) || ip.length() < IP_MIN_LENGTH) {
                continue;
            }
            String[] ips = ip.split(",");
            for (String item : ips) {
                if (!IpTypeEnum.UNKNOWN.value.equalsIgnoreCase(item)) {
                    ip = item;
                    break;
                }
            }
            break;
        }
        return CheckUtil.isBlank(ip) ? request.getRemoteAddr() : ip;
    }

    /**
     * 校验ip
     *
     * @param ip ip
     * @return status
     */
    public static boolean validate(String ip) {
        if (CheckUtil.nonBlank(ip)) {
            String currentIp = getIpAddress();
            return Objects.equals(currentIp, ip);
        }
        return false;
    }

    /**
     * 根据IP转换省市
     *
     * @param ip ip
     * @return region
     */
    public static String ip2Region(String ip) {
        Searcher searcher = null;
        try {
            // 获取db文件
            File file = ResourceUtils.getFile(IP_2_REGION_DB_FILE_PATH);
            // 根据搜索算法类型获取对应方法
            searcher = Searcher.newWithFileOnly(file.getPath());
            return searcher.search(ip);
        } catch (FileNotFoundException fileE) {
            String tips = "无效的db文件路径,地址路径：".concat(IP_2_REGION_DB_FILE_PATH);
            log.error(LogHandler.format("IpUtil异常", tips));
        } catch (Exception e) {
            log.error(LogHandler.format("IpUtil异常", "转换失败"));
        } finally {
            if (CheckUtil.nonNull(searcher)) {
                try {
                    // 频繁创建关闭资源,这里待改进
                    searcher.close();
                } catch (IOException e) {
                    log.error(LogHandler.format("IpUtil异常", "资源关闭失败"));
                }
            }
        }
        return "IP转换省市失败";
    }

    @Getter
    @AllArgsConstructor
    private enum IpTypeEnum {
        /**
         * IP类型
         */
        UNKNOWN("Unknown"),
        XFF("X-Forwarded-For"),
        PCI("Proxy-Client-IP"),
        WL_PCI("WL-Proxy-Client-IP"),
        HTTP_CI("HTTP_CLIENT_IP"),
        HTTP_XFF("HTTP_X_FORWARDED_FOR");

        public final String value;
    }
}
