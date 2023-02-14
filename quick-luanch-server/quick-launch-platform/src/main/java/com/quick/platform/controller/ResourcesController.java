package com.quick.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.base.Result;
import com.quick.common.handler.ResultHandler;
import com.quick.platform.dto.resources.*;
import com.quick.platform.service.ResourcesService;
import com.quick.platform.vo.resources.DetailResourcesPfVO;
import com.quick.platform.vo.resources.PageResourcesPfVO;
import com.quick.platform.vo.resources.TreeResourcesPfVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yuanbai
 */
@Tag(name = "【后台】资源接口", description = "ResourcesController")
@RequestMapping("/platform/resources")
@RestController
public class ResourcesController {
    private final ResourcesService resourcesService;

    @Autowired
    public ResourcesController(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @Operation(summary = "分页")
    @PostMapping("/page")
    public Result<IPage<PageResourcesPfVO>> page(@RequestBody PageResourcesPfDTO dto) {
        return ResultHandler.ok(resourcesService.page(dto));
    }

    @Operation(summary = "根据资源ID查询")
    @GetMapping("/{resourceId}")
    public Result<DetailResourcesPfVO> getOneById(@PathVariable Long resourceId) {
        return ResultHandler.ok(resourcesService.getOneById(resourceId));
    }

    @Operation(summary = "新增资源信息")
    @PostMapping("/")
    public Result<Boolean> save(@RequestBody @Valid SaveResourcesPfDTO dto) {
        return ResultHandler.ok(resourcesService.save(dto));
    }

    @Operation(summary = "更新资源信息")
    @PutMapping("/")
    public Result<Boolean> updateById(@RequestBody @Valid UpdateResourcesPfDTO dto) {
        return ResultHandler.ok(resourcesService.updateById(dto));
    }

    @Operation(summary = "删除资源信息")
    @DeleteMapping("/")
    public Result<Boolean> deleteById(@RequestBody DeleteResourcesPfDTO dto) {
        return ResultHandler.ok(resourcesService.deleteById(dto));
    }

    @Operation(summary = "切换资源启用状态")
    @PutMapping("/toggle-enable")
    public Result<Boolean> toggleEnable(@RequestBody @Valid ToggleEnableResourcesPfDTO dto) {
        return ResultHandler.ok(resourcesService.toggleEnable(dto));
    }

    @Operation(summary = "根据pid查询子资源树")
    @GetMapping("/tree/{pid}")
    public Result<List<TreeResourcesPfVO>> treePermission(@PathVariable Long pid) {
        return ResultHandler.ok(resourcesService.treeResources(pid));
    }
}
