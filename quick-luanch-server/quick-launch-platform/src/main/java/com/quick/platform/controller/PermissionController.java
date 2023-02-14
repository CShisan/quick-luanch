package com.quick.platform.controller;

import com.quick.common.base.Result;
import com.quick.common.handler.ResultHandler;
import com.quick.platform.dto.permission.*;
import com.quick.platform.service.PermissionService;
import com.quick.platform.vo.permission.DetailPermissionPfVO;
import com.quick.platform.vo.permission.TreePermissionPfVO;
import com.quick.platform.vo.resources.RelationResourcesPfVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author CShisan
 */
@Tag(name = "【后台】权限接口", description = "PermissionController")
@RestController
@RequestMapping("/platform/permission")
public class PermissionController {
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Operation(summary = "根据pid查询子权限树")
    @GetMapping("/tree/{pid}")
    public Result<List<TreePermissionPfVO>> treePermission(@PathVariable Long pid) {
        return ResultHandler.ok(permissionService.treePermission(pid));
    }

    @Operation(summary = "根据权限ID查询")
    @GetMapping("/{permissionId}")
    public Result<DetailPermissionPfVO> getOneById(@PathVariable Long permissionId) {
        return ResultHandler.ok(permissionService.getOneById(permissionId));
    }

    @Operation(summary = "新增权限信息")
    @PostMapping("/")
    public Result<Boolean> save(@RequestBody @Valid SavePermissionPfDTO dto) {
        return ResultHandler.ok(permissionService.save(dto));
    }

    @Operation(summary = "更新权限信息")
    @PutMapping("/")
    public Result<Boolean> updateById(@RequestBody @Valid UpdatePermissionPfDTO dto) {
        return ResultHandler.ok(permissionService.updateById(dto));
    }

    @Operation(summary = "删除权限信息")
    @DeleteMapping("/")
    public Result<Boolean> deleteById(@RequestBody DeletePermissionPfDTO dto) {
        return ResultHandler.ok(permissionService.deleteById(dto));
    }

    @Operation(summary = "切换权限启用状态")
    @PutMapping("/toggle-enable")
    public Result<Boolean> toggleEnable(@RequestBody @Valid ToggleEnablePermissionPfDTO dto) {
        return ResultHandler.ok(permissionService.toggleEnable(dto));
    }

    @Operation(summary = "根据权限id查询权限关联的资源列表")
    @GetMapping("/list/resources/{permissionId}")
    public Result<List<RelationResourcesPfVO>> listResourcesByPermissionId(@PathVariable Long permissionId) {
        return ResultHandler.ok(permissionService.listResourcesByPermissionId(permissionId));
    }

    @Operation(summary = "绑定资源")
    @PutMapping("/bind/resources")
    public Result<Boolean> bindResource(@RequestBody @Valid BindResourcePermissionPfDTO dto) {
        return ResultHandler.ok(permissionService.bindResource(dto));
    }
}
