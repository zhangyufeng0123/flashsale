package org.gotomove.flashsale.Validator;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.gotomove.flashsale.Validator.IsMobile;
import org.gotomove.flashsale.utils.ValidatorUtil;

/**
 * @Author zhang
 * @Date 2024/1/19
 * @Description
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.isMobile(str);
        } else {
            if (StringUtils.isEmpty(str)){
                return true;
            } else {
                return ValidatorUtil.isMobile(str);
            }
        }
    }
}
