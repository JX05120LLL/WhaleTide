package com.whale_tide.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie 工具类
 *
 */

public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/"); // 设置 Cookie 的路径
        cookie.setMaxAge(maxAge); // 设置 Cookie 的最大存活时间
        cookie.setHttpOnly(true); // 设置 Cookie 为 HttpOnly，防止 XSS 攻击
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
