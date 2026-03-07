package com.oxn.xiaoxiong.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxn.xiaoxiong.common.BaseResponse;
import com.oxn.xiaoxiong.common.ResultUtils;
import com.oxn.xiaoxiong.domain.SysUser;
import com.oxn.xiaoxiong.enums.StatusCode;
import com.oxn.xiaoxiong.exception.ThrowUtils;
import com.oxn.xiaoxiong.sa.StpKit;
import com.oxn.xiaoxiong.sa.annotation.SaUserCheckRole;
import com.oxn.xiaoxiong.service.SysUserService;
import com.oxn.xiaoxiong.util.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @Resource
    private RedisUtil redisUtil;

    private static final String PREFIX = "sys_user";

    /**
     * 测试
     *
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<IPage<SysUser>> list() {
        // 构造分页对象
        Page<SysUser> page = new Page<>(1, 3);
        // 构造查询条件（可以在这里加 where 条件）
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        // 分页查询
        IPage<SysUser> resultPage = sysUserService.page(page, queryWrapper);
        redisUtil.setConfigObjectList(PREFIX, resultPage.getRecords(), user -> user.getId().toString());
        // 直接把分页结果返回
        return ResultUtils.success(resultPage);
    }

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<Boolean> login(@RequestParam("username") String username,
            @RequestParam("password") String password) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(username != null, "username", username)
                .eq(password != null, "password", password);
        SysUser one = sysUserService.getOne(queryWrapper);
        if (one != null) {
            // 使用系统用户账号体系进行登录，确保与 SaUserAuthAspect 中的 StpKit.SYS_ROLE 保持一致
            StpKit.SYS_ROLE.login(one.getId());
            return ResultUtils.success(true);
        }
        ThrowUtils.throwIf(true, StatusCode.NOT_FOUND_ERROR, "用户不存在");
        return null;
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    @GetMapping("/isLogin")
    public String isLogin() {
        boolean login = StpUtil.isLogin();
        return "当前会话是否登录：" + login;
    }

    /**
     * 只有【普通用户】角色可以访问的接口（用户端专用权限注解）
     *
     * <p>
     * 对应角色标识：user（见 SysRoleEnum.USER.roleKey）
     */
    @GetMapping("/role/user")
    @SaUserCheckRole("user")
    public String userRoleEndpoint() {
        return "普通用户可访问接口，当前角色集合：" + StpKit.SYS_ROLE.getPermissionList();
    }

    /**
     * 只有【管理员】角色可以访问的接口（用户端专用权限注解）
     *
     * <p>
     * 对应角色标识：admin（见 SysRoleEnum.ADMIN.roleKey）
     */
    @GetMapping("/role/admin")
    @SaUserCheckRole("admin")
    public String adminRoleEndpoint() {
        return "管理员可访问接口，当前角色集合：" + StpKit.SYS_ROLE.getPermissionList();
    }

    /**
     * 只有【超级管理员】角色可以访问的接口（用户端专用权限注解）
     *
     * <p>
     * 对应角色标识：super-admin（见 SysRoleEnum.SUPER_ADMIN.roleKey）
     */
    @GetMapping("/role/super-admin")
    @SaUserCheckRole("super-admin")
    public String superAdminRoleEndpoint() {
        return "超级管理员可访问接口，当前角色集合：" + StpKit.SYS_ROLE.getPermissionList();
    }

}
