package com.tianlin.linpaobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianlin.linpaobackend.common.ErrorCode;
import com.tianlin.linpaobackend.exception.BusinessException;
import com.tianlin.linpaobackend.mapper.UserMapper;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.model.response.PageResponse;
import com.tianlin.linpaobackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.tianlin.linpaobackend.constant.UserConstant.*;


/**
* @author 天琳
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-06-02 19:34:35
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * md5加密密码
     */
    private static final String md5Password = "tianlin";


    /**
     * 判断是否为管理员
     *
     * @param request 请求
     * @return 是否为管理员
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) userObj;
        return user == null || user.getUserRole() != ADMIN_ROLE; // 不是管理员且未登录
    }

    /**
     * @description 获取安全用户信息,不包含密码
     * @param originUser 原始用户信息
     * @return User
     */
    @Override
    public User getSafetUser(User originUser) {
        User safetyUser = new User();
        if (originUser == null) {
            return null;
        }
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserCode(originUser.getUserCode());
        safetyUser.setTags(originUser.getTags());
        return safetyUser;
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, Long userCode) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码不能为空");
        }
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度必须在4-16位之间");
        }
        if (userPassword.length() < 6 || userPassword.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度必须在6-20位之间");
        }
        // 用户编码转化成整数只能大于零
        if (userCode < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户编码必须大于零");
        }
        // 账号不能包含特殊字符,只能是字母数字下划线
        String reg = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(reg).matcher(userAccount);
        if (!matcher.find()) { // 如果包含特殊字符
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }
        // 判断账号是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        // 判断用户编码是否也存在了
        queryWrapper.clear();  // 清空条件
        queryWrapper.eq("userCode", userCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户编码已存在");
        }
        // 2.加密
        String newPassword = md5Password + userPassword + md5Password;
        String password = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        // 3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(password);
        user.setUserCode(userCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码不能为空");
        }
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度必须在4-16位之间");
        }
        if (userPassword.length() < 6 || userPassword.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度必须在6-20位之间");
        }
        // 账号不能包含特殊字符,只能是字母数字下划线
        String reg = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(reg).matcher(userAccount);
        if (!matcher.find()) { // 如果包含特殊字符
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
        // 2.加密
        String newPassword = md5Password + userPassword + md5Password;
        String password = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        // 3.查询数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", password);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在或密码错误
        if (user == null) {
            log.info("user longin fail, userAccount can not find or userPassword error");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
        }
        // 3.返回用户信息,不包含密码
        User safetyUser = getSafetUser(user);
        // 4.记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);
        // 5.返回用户信息
        return getSafetUser(user);
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        // 1.清除session中的用户登录状态
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }


    @Override
    public User checkUserInfo(User userInfo) {
        // 1.校验
        if (userInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户信息不能为空");
        }
        if (StringUtils.isAnyBlank(userInfo.getUserAccount())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能为空");
        }
        if (userInfo.getUserAccount().length() < 4 || userInfo.getUserAccount().length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度必须在4-16位之间");
        }
        // 账号不能包含特殊字符,只能是字母数字下划线
        String reg = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(reg).matcher(userInfo.getUserAccount());
        if (!matcher.find()) { // 如果包含特殊字符
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
       // 头像必须是网络地址
        if (StringUtils.isNotBlank(userInfo.getAvatarUrl())) {
            if (!userInfo.getAvatarUrl().startsWith("http")) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "头像必须是网络地址");
            }
        }
        // TODO 需要专门提供接口修改密码的
        // TODO 需要校验邮箱和手机号格式
        return userInfo;
    }

    /**
     * 根据用户标签查询用户信息
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> getUserByTag(List<String> tagNameList) {

        // 1.校验
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // sql过滤
        // 拼接and查询
        // like %tag1% and like %tag2% and like %tag3%
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags", tagName);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        // 每个用户都需要脱敏
        return userList.stream().map(this::getSafetUser).collect(Collectors.toList());
        // 在内存中过滤
//        queryWrapper = new QueryWrapper<>();
//        List<User> userList = userMapper.selectList(queryWrapper);
//        Gson gson = new Gson();
//        return userList.stream().filter(user -> {
//            String tagsJson = user.getTags();
//            // 用户标签是否为空
//            if (StringUtils.isBlank(tagsJson)) {
//                return false;
//            }
//            Set<String> tagNameSet = gson.fromJson(tagsJson, new TypeToken<Set<String>>() {
//            }.getType());
//            tagNameSet = Optional.ofNullable(tagNameSet).orElse(new HashSet<>()); // 防止集合中的空指针
//            for (String tagName : tagNameList) {
//                if (!tagNameSet.contains(tagName)) {
//                    return false; // 有一个标准不包含
//                }
//            }
//            return true;
//        }).map(this::getSafetUser).collect(Collectors.toList());
    }

    @Override
    public PageResponse<User> getUserByPage(HttpServletRequest request, long pageNum, long pageSize) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User) userObj; // 强转
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        String redisKey = String.format("%s:recommend:%s", REDIS_USER_PREFIX , currentUser.getId());
        // 如果有缓存，先读取缓存
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        PageResponse<User> pageResponse = new PageResponse<>();
        if (userPage != null) {
            pageResponse.setTotal(userPage.getTotal());
            pageResponse.setItems(userPage.getRecords());
            return pageResponse;
        }
        // 如果没有缓存，才从数据库中读取并缓存
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        pageResponse.setTotal(userPage.getTotal());
        pageResponse.setItems(userPage.getRecords());
        try {
            // 设置缓存，过期时间为一天
            valueOperations.set(redisKey, userPage, 1, TimeUnit.DAYS);
        }catch (Exception e) {
            log.error("redis set key error:{}", e.getMessage());
        }
        return pageResponse;
    }
}



