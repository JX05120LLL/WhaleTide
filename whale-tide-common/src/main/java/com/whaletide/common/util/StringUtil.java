package com.whaletide.common.util;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 常用正则表达式模式
     */
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^\\d{17}[0-9Xx]$|^\\d{15}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{4,16}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}$");
    
    /**
     * 判断字符串是否为空或空字符串
     * @param str 源字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    
    /**
     * 判断字符串是否不为空且不为空字符串
     * @param str 源字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 判断字符串是否全部为数字
     * @param str 源字符串
     * @return 是否全部为数字
     */
    public static boolean isNumeric(String str) {
        return StringUtils.isNumeric(str);
    }
    
    /**
     * 截取字符串，超出部分用省略号代替
     * @param str 源字符串
     * @param maxLength 最大长度
     * @return 截取后的字符串
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }
    
    /**
     * 生成指定长度的随机数字字符串
     * @param length 长度
     * @return 随机数字字符串
     */
    public static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    /**
     * 生成不带连字符的UUID字符串
     * @return UUID字符串
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 检查字符串是否为有效的手机号
     * @param mobile 手机号
     * @return 是否有效
     */
    public static boolean isValidMobile(String mobile) {
        return isNotEmpty(mobile) && MOBILE_PATTERN.matcher(mobile).matches();
    }
    
    /**
     * 检查字符串是否为有效的电子邮箱
     * @param email 电子邮箱
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 检查字符串是否为有效的身份证号
     * @param idCard 身份证号
     * @return 是否有效
     */
    public static boolean isValidIdCard(String idCard) {
        // 简单格式验证，不包含复杂的校验算法
        return isNotEmpty(idCard) && ID_CARD_PATTERN.matcher(idCard).matches();
    }
    
    /**
     * 检查字符串是否为有效的用户名
     * 用户名要求：4-16位，只能包含字母、数字、下划线和连字符
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean isValidUsername(String username) {
        return isNotEmpty(username) && USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * 检查字符串是否为有效的密码
     * 密码要求：6-20位，必须包含字母和数字
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValidPassword(String password) {
        return isNotEmpty(password) && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * 隐藏手机号中间四位
     * @param mobile 手机号
     * @return 隐藏后的手机号
     */
    public static String maskMobile(String mobile) {
        if (!isValidMobile(mobile)) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }
    
    /**
     * 隐藏邮箱用户名部分
     * @param email 邮箱
     * @return 隐藏后的邮箱
     */
    public static String maskEmail(String email) {
        if (!isValidEmail(email)) {
            return email;
        }
        int atIndex = email.indexOf('@');
        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        
        if (username.length() <= 2) {
            return "*" + domain;
        } else {
            return username.charAt(0) + "***" + username.charAt(username.length() - 1) + domain;
        }
    }
    
    /**
     * 驼峰命名转下划线命名
     * @param camelCase 驼峰命名的字符串
     * @return 下划线命名的字符串
     */
    public static String camelToUnderscore(String camelCase) {
        if (isEmpty(camelCase)) {
            return camelCase;
        }
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        return camelCase.replaceAll(regex, replacement).toLowerCase();
    }
    
    /**
     * 下划线命名转驼峰命名
     * @param underscore 下划线命名的字符串
     * @param capitalizeFirstLetter 是否大写第一个字母
     * @return 驼峰命名的字符串
     */
    public static String underscoreToCamel(String underscore, boolean capitalizeFirstLetter) {
        if (isEmpty(underscore)) {
            return underscore;
        }
        
        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;
        
        for (int i = 0; i < underscore.length(); i++) {
            char currentChar = underscore.charAt(i);
            if (currentChar == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpperCase = false;
                } else {
                    result.append(Character.toLowerCase(currentChar));
                }
            }
        }
        
        if (capitalizeFirstLetter && result.length() > 0) {
            result.setCharAt(0, Character.toUpperCase(result.charAt(0)));
        }
        
        return result.toString();
    }
    
    /**
     * 将字符串转换为字节数组（UTF-8编码）
     * @param str 字符串
     * @return 字节数组
     */
    public static byte[] getBytes(String str) {
        if (str == null) {
            return new byte[0];
        }
        return str.getBytes(StandardCharsets.UTF_8);
    }
    
    /**
     * 将字节数组转换为字符串（UTF-8编码）
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String toString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
