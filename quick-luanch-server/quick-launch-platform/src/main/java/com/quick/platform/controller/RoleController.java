package com.quick.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.base.Result;
import com.quick.common.handler.ResultHandler;
import com.quick.platform.dto.role.*;
import com.quick.platform.service.RoleService;
import com.quick.platform.vo.permission.ListRelationPermissionPfVO;
import com.quick.platform.vo.role.DetailRolePfVO;
import com.quick.platform.vo.role.PageRolePfVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author CShisan
 */
@Tag(name = "【后台】角色接口", description = "RoleController")
@RestController
@RequestMapping("/platform/role")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "角色信息分页")
    @PostMapping("/page")
    public Result<IPage<PageRolePfVO>> page(@RequestBody PageRolePfDTO dto) {
        return ResultHandler.ok(roleService.page(dto));
    }

    @Operation(summary = "根据roleId查询单个角色")
    @GetMapping("/{roleId}")
    public Result<DetailRolePfVO> getOneById(@PathVariable Long roleId) {
        return ResultHandler.ok(roleService.getOneById(roleId));
    }

    @Operation(summary = "新增角色")
    @PostMapping("/")
    public Result<Boolean> save(@RequestBody @Valid SaveRolePfDTO dto) {
        return ResultHandler.ok(roleService.save(dto));
    }

    @Operation(summary = "更新角色")
    @PutMapping("/")
    public Result<Boolean> updateById(@RequestBody @Valid UpdateRolePfDTO dto) {
        return ResultHandler.ok(roleService.updateById(dto));
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/")
    public Result<Boolean> deleteById(@RequestBody DeleteRolePfDTO dto) {
        return ResultHandler.ok(roleService.deleteById(dto));
    }

    @Operation(summary = "切换角色启用状态")
    @PutMapping("/toggle-enable")
    public Result<Boolean> toggleEnable(@RequestBody @Valid ToggleEnableRolePfDTO dto) {
        return ResultHandler.ok(roleService.toggleEnable(dto));
    }

    @Operation(summary = "根据roleId查询角色关联的权限列表")
    @GetMapping("/list/permission/{roleId}")
    public Result<List<ListRelationPermissionPfVO>> getPermissionsByRoleId(@PathVariable Long roleId) {
        return ResultHandler.ok(roleService.getPermissionsByRoleId(roleId));
    }

    @Operation(summary = "更新角色关联的权限")
    @PutMapping("/relation/permission")
    public Result<Boolean> updateRolePermission(@RequestBody @Valid UpdateRolePermissionPfDTO dto) {
        return ResultHandler.ok(roleService.updateRolePermission(dto));
    }
}
