package com.oxn.xiaoxiong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxn.xiaoxiong.common.BaseResponse;
import com.oxn.xiaoxiong.common.ResultUtils;
import com.oxn.xiaoxiong.domain.SysUser;
import com.oxn.xiaoxiong.service.SysUserService;
import com.oxn.xiaoxiong.util.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * 测试接口
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
}
