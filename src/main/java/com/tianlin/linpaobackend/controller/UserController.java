package com.tianlin.linpaobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianlin.linpaobackend.common.BaseResponse;
import com.tianlin.linpaobackend.common.ErrorCode;
import com.tianlin.linpaobackend.common.ResultUtils;
import com.tianlin.linpaobackend.exception.BusinessException;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.model.domain.request.UserLoginRequest;
import com.tianlin.linpaobackend.model.domain.request.UserRegisterRequest;
import com.tianlin.linpaobackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.tianlin.linpaobackend.constant.UserConstant.ADMIN_ROLE;
import static com.tianlin.linpaobackend.constant.UserConstant.USER_LOGIN_STATUS;


/**
 * 用户接口
 * @author 天琳
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 判断是否为管理员
     *
     * @param request 请求
     * @return 是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) userObj;
        return user == null || user.getUserRole() != ADMIN_ROLE; // 不是管理员且未登录
    }


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Long userCode = userRegisterRequest.getUserCode();

        if (userAccount == null || userPassword == null || checkPassword == null || userCode == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, userCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (userAccount == null || userPassword == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> userCurrent(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User) userObj; // 强转
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        User user = userService.getById(userId);
        User result = userService.getSafetUser(user);
        return ResultUtils.success(result);
    }

    @GetMapping("/list")
    public BaseResponse<List<User>> userList(String username, HttpServletRequest request) {
        // 鉴权
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> result = userList.stream().map(user -> userService.getSafetUser(user)).collect(Collectors.toList());
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> userDelete(@RequestBody User body,HttpServletRequest request) {
        // 鉴权
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        Long id = body.getId();
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 管理员更新用户信息
     *
     * @param body    用户信息
     * @param request 请求
     * @return 是否更新成功
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> userUpdate(@RequestBody User body,HttpServletRequest request) {
        // 获取当前登录用户
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User) userObj; // 强转
        Long id = body.getId();
        // 鉴权
        if (isAdmin(request) || !currentUser.getId().equals(id)) { // 不是管理员或者不是本人
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        User userInfo = userService.checkUserInfo(body);
        boolean result = userService.updateById(userInfo);
        return ResultUtils.success(result);
    }

    /**
     * 获取用户信息列表
     *
     * @param tagNameList 标签名列表
     * @return 用户信息列表
     */
    @GetMapping("/friendList")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(value = "tags",required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> result = userService.getUserByTag(tagNameList);
        return ResultUtils.success(result);
    }

    @GetMapping("/recommend")
    public BaseResponse<List<User>> recommendUsers(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User) userObj; // 强转
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // 查询所有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> result = userService.list(queryWrapper);
        // 随机打乱用户列表
        Collections.shuffle(result);
        // 获取前10个用户作为推荐结果
        List<User> recommendUsers = result.subList(0, Math.min(result.size(), 10));
        return ResultUtils.success(recommendUsers);
    }
}