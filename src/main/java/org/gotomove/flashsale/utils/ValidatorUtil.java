package org.gotomove.flashsale.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author zhang
 * @Date 2024/1/19
 * @Description
 */
public class ValidatorUtil {
    private static final Pattern mobile_pattern = Pattern.compile("([1])([3-9])[0-9]{9}$");

    public static boolean isMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
