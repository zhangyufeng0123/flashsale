package org.gotomove.flashsale.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @Author zhang
 * @Date 2024/1/16
 * @Description MD5工具类
 */
@Component
public class MD5Util {
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFromPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String fromPassToDBPass(String fromPass, String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDBPass(fromPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        String pass1 = inputPassToFromPass("123456");
        String pass2 = fromPassToDBPass(pass1, "1a2b3c4d");
        String pass3 = inputPassToDBPass("123456", salt);
        System.out.println(pass1);
        System.out.println(pass2);
        System.out.println(pass3);
    }
}
