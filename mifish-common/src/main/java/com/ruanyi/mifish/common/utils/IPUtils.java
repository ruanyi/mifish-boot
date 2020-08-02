package com.ruanyi.mifish.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2020-08-02 22:47
 */
public final class IPUtils {

    /**
     * 获取本机所有ip 返回map key为网卡名 value为对应ip
     *
     * @return
     */
    public static Map<String, String> getLocalIps() {
        try {
            Map<String, String> result = new HashMap<>(64);
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                String name = ni.getName();
                String ip = "";
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress address = ips.nextElement();
                    if (address instanceof Inet4Address) {
                        ip = address.getHostAddress();
                        break;
                    }
                }
                result.put(name, ip);
            }
            return result;
        } catch (SocketException e) {
            return Collections.emptyMap();
        }
    }

    /**
     * 获取服务器ip 判断规则 eth0 > eth1 > ... ethN > wlan > lo
     *
     * @return
     */
    public static String getLocalIp() {

        Map<String, String> ips = getLocalIps();
        List<String> faceNames = new ArrayList<String>(ips.keySet());
        Collections.sort(faceNames);

        for (String name : faceNames) {
            if ("lo".equals(name)) {
                continue;
            }
            String ip = ips.get(name);
            if (!StringUtils.isBlank(ip)) {
                return ip;
            }
        }
        return "127.0.0.1";
    }

    /** forbit instance */
    private IPUtils() {

    }
}
