package com.tianlin.linpaobackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author 天琳
 */

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 21412312L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private Long userCode;
}