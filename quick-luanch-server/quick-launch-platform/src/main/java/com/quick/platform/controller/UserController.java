package com.quick.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.base.Result;
import com.quick.common.handler.ResultHandler;
import com.quick.common.utils.HttpUtil;
import com.quick.platform.dto.user.PageUserPfDTO;
import com.quick.platform.dto.user.ToggleEnableUserPfDTO;
import com.quick.platform.dto.user.ToggleLockUserPfDTO;
import com.quick.platform.dto.user.UpdateUserPfDTO;
import com.quick.platform.service.UserService;
import com.quick.platform.vo.role.ListRelationRolePfVO;
import com.quick.platform.vo.user.DetailUserPfVO;
import com.quick.platform.vo.user.PageUserPfVO;
import com.quick.platform.vo.user.PersonalInfoPfVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author CShisan
 */
@Tag(name = "【后台】用户接口", description = "UserController")
@RestController
@RequestMapping("/platform/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "获取个人信息")
    @GetMapping("/personal-info")
    public Result<PersonalInfoPfVO> personalInfo() {
        Long uid = HttpUtil.currentUidWithException();
        return ResultHandler.ok(userService.personalInfo(uid));
    }

    @Operation(summary = "用户信息分页")
    @PostMapping("/page")
    public Result<IPage<PageUserPfVO>> page(@RequestBody PageUserPfDTO dto) {
        return ResultHandler.ok(userService.page(dto));
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/")
    public Result<Integer> updateById(@RequestBody @Valid UpdateUserPfDTO dto) {
        return ResultHandler.ok(userService.updateById(dto));
    }

    @Operation(summary = "根据uid查询单个用户")
    @GetMapping("/{uid}")
    public Result<DetailUserPfVO> getOneById(@PathVariable Long uid) {
        return ResultHandler.ok(userService.getOneById(uid));
    }

    @Operation(summary = "切换用户启用状态")
    @PutMapping("/toggle-enable")
    public Result<Boolean> toggleEnable(@RequestBody @Valid ToggleEnableUserPfDTO dto) {
        return ResultHandler.ok(userService.toggleEnable(dto));
    }

    @Operation(summary = "切换用户启用状态")
    @PutMapping("/toggle-lock")
    public Result<Boolean> toggleLock(@RequestBody @Valid ToggleLockUserPfDTO dto) {
        return ResultHandler.ok(userService.toggleLock(dto));
    }

    @Operation(summary = "根据uid查询用户关联的角色列表")
    @GetMapping("/list/roles/{uid}")
    public Result<List<ListRelationRolePfVO>> getRolesByUid(@PathVariable Long uid) {
        return ResultHandler.ok(userService.getRolesByUid(uid));
    }
}
