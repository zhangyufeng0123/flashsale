package org.gotomove.flashsale.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.gotomove.flashsale.Validator.IsMobile;
import org.hibernate.validator.constraints.Length;

/**
 * @Author zhang
 * @Date 2024/1/19
 * @Description
 */
@Data
public class LoginVo {
    @NotNull
    @IsMobile(required = true)
    private String mobile;

    @NotNull
    private String password;
}
