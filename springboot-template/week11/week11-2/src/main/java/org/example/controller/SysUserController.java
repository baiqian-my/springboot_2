package org.example.controller;

import lombok.Data;
import org.example.common.Result;
import org.example.entity.SysUser;
import org.example.service.SysUserService;
import org.example.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器 - 匹配 maku-boot 前端规范
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
    
    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        SysUser user = sysUserService.getById(userId);
        
        if (user == null) {
            return Result.unauthorized("用户不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("avatar", user.getAvatar());
        result.put("gender", user.getGender());
        result.put("email", user.getEmail());
        result.put("mobile", user.getMobile());
        result.put("superAdmin", user.getSuperAdmin());
        
        // TODO: 获取用户角色和权限
        result.put("roles", new ArrayList<>());
        result.put("permissions", new ArrayList<>());
        
        return Result.success(result);
    }
    
    /**
     * 更新当前用户信息
     */
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody UserInfoDTO dto, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        SysUser user = sysUserService.getById(userId);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 只更新允许更新的字段
        if (dto.getRealName() != null) {
            user.setRealName(dto.getRealName());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getMobile() != null) {
            user.setMobile(dto.getMobile());
        }
        
        sysUserService.updateById(user);
        return Result.success("更新成功", null);
    }
    
    /**
     * 更新头像
     */
    @PutMapping("/avatar")
    public Result<Void> updateAvatar(@RequestBody AvatarDTO dto, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        SysUser user = sysUserService.getById(userId);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        user.setAvatar(dto.getAvatar());
        sysUserService.updateById(user);
        
        return Result.success("头像更新成功", null);
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody PasswordDTO dto, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        SysUser user = sysUserService.getById(userId);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 验证原密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            return Result.error("原密码错误");
        }
        
        // 更新密码
        user.setPassword(encoder.encode(dto.getNewPassword()));
        sysUserService.updateById(user);
        
        return Result.success("密码修改成功", null);
    }
    
    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getUserById(@PathVariable("id") Long id) {
        SysUser user = sysUserService.getById(id);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        Map<String, Object> result = convertToMap(user);
        return Result.success(result);
    }
    
    /**
     * 获取用户真实姓名列表
     */
    @PostMapping("/nameList")
    public Result<List<Map<String, Object>>> getRealNameList(@RequestBody List<Long> idList) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        if (idList == null || idList.isEmpty()) {
            return Result.success(result);
        }
        
        List<SysUser> users = sysUserService.listByIds(idList);
        for (SysUser user : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("realName", user.getRealName());
            map.put("username", user.getUsername());
            result.add(map);
        }
        
        return Result.success(result);
    }
    
    /**
     * 创建/更新用户
     */
    @PostMapping
    public Result<Void> saveUser(@RequestBody SysUser user) {
        // 新增用户时加密密码
        if (user.getId() == null && user.getPassword() != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
        }
        
        sysUserService.saveOrUpdate(user);
        return Result.success("保存成功", null);
    }
    
    /**
     * 更新用户
     */
    @PutMapping
    public Result<Void> updateUser(@RequestBody SysUser user) {
        if (user.getId() == null) {
            return Result.badRequest("用户ID不能为空");
        }
        
        // 不更新密码字段
        user.setPassword(null);
        sysUserService.updateById(user);
        
        return Result.success("更新成功", null);
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable("id") Long id) {
        sysUserService.removeById(id);
        return Result.success("删除成功", null);
    }
    
    /**
     * 获取组织用户列表
     */
    @GetMapping("/orgUserList/{orgId}")
    public Result<List<Map<String, Object>>> getOrgUserList(@PathVariable("orgId") Long orgId) {
        // TODO: 实现根据组织ID查询用户
        // 目前返回所有用户
        List<SysUser> users = sysUserService.list();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (SysUser user : users) {
            result.add(convertToMap(user));
        }
        
        return Result.success(result);
    }
    
    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    public Result<Map<String, Object>> page(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "username", required = false) String username) {
        
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<SysUser> pageParam = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, pageSize);
        
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        
        if (username != null && !username.isEmpty()) {
            wrapper.like(SysUser::getUsername, username);
        }
        
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<SysUser> pageResult = 
            sysUserService.page(pageParam, wrapper);
        
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysUser user : pageResult.getRecords()) {
            list.add(convertToMap(user));
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", pageResult.getTotal());
        result.put("page", page);
        result.put("pageSize", pageSize);
        
        return Result.success(result);
    }
    
    // ==================== 私有方法 ====================
    
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = jwtUtils.extractTokenFromHeader(request.getHeader("Authorization"));
        return jwtUtils.getUserId(token);
    }
    
    private Map<String, Object> convertToMap(SysUser user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("realName", user.getRealName());
        map.put("avatar", user.getAvatar());
        map.put("gender", user.getGender());
        map.put("email", user.getEmail());
        map.put("mobile", user.getMobile());
        map.put("status", user.getStatus());
        map.put("superAdmin", user.getSuperAdmin());
        map.put("createTime", user.getCreateTime());
        map.put("updateTime", user.getUpdateTime());
        return map;
    }
    
    // ==================== DTO ====================
    
    @Data
    public static class UserInfoDTO {
        private String realName;
        private Integer gender;
        private String email;
        private String mobile;
    }
    
    @Data
    public static class AvatarDTO {
        private String avatar;
    }
    
    @Data
    public static class PasswordDTO {
        private String oldPassword;
        private String newPassword;
    }
}