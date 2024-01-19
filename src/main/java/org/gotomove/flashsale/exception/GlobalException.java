package org.gotomove.flashsale.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gotomove.flashsale.vo.RespBeanEnum;

/**
 * @Author zhang
 * @Date 2024/1/19
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private RespBeanEnum respBeanEnum;
}
