package com.tianlin.linpaobackend.script;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author 张添琳
 * 用户信息映射
 */
@Data
public class CenterUserInfo {

    /**
     * 用户编号
     */
    @ExcelProperty("用户编号")
    private Long userCode;

    /**
     * 用户昵称
     */
    @ExcelProperty("用户昵称")
    private String username;
}
