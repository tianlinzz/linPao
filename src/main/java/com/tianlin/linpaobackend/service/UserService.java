package com.tianlin.linpaobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.model.response.PageResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 天琳
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-06-02 19:34:35
 */
public interface UserService extends IService<User> {

    /**
     * 获取安全用户信息
     *
     * @param originUser 原始用户信息
     * @return 安全用户信息
     */
    User getSafetUser(User originUser);

    /**
     * 用户注册
     *
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, Long userCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request 请求
     * @return
     */
    int userLogout(HttpServletRequest request);

    // 校验用户信息是否合法
    User checkUserInfo(User userInfo);

    List<User> getUserByTag(List<String> tagNameList);

    PageResponse<User> getUserByPage(HttpServletRequest request, long pageNum, long pageSize);

    /**
     * 判断是否为管理员
     *
     * @param request 请求
     * @return 是否为管理员
     */
    boolean isAdmin(HttpServletRequest request);
}