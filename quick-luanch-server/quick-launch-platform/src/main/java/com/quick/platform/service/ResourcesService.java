package com.quick.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.enums.CodeEnum;
import com.quick.common.exception.ExceptionHandler;
import com.quick.common.utils.BeanUtil;
import com.quick.common.utils.CheckUtil;
import com.quick.common.utils.PageUtil;
import com.quick.domain.dao.PermResourcesDao;
import com.quick.domain.dao.ResourcesDao;
import com.quick.domain.entity.Resources;
import com.quick.platform.dto.resources.*;
import com.quick.platform.vo.resources.DetailResourcesPfVO;
import com.quick.platform.vo.resources.PageResourcesPfVO;
import com.quick.platform.vo.resources.TreeResourcesPfVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author yuanbai
 */
@Service
public class ResourcesService {
    private final ResourcesDao resourcesDao;
    private final PermResourcesDao permResourcesDao;

    @Autowired
    public ResourcesService(ResourcesDao resourcesDao, PermResourcesDao permResourcesDao) {
        this.resourcesDao = resourcesDao;
        this.permResourcesDao = permResourcesDao;
    }

    /**
     * 分页
     *
     * @param dto dto
     * @return page
     */
    public IPage<PageResourcesPfVO> page(PageResourcesPfDTO dto) {
        Resources entity = BeanUtil.convert(dto, Resources::new);
        IPage<Resources> page = resourcesDao.page(entity, dto.page());
        return PageUtil.convert(page, PageResourcesPfVO::new);
    }

    /**
     * 根据资源ID查询
     *
     * @param resourceId resourceId
     * @return vo
     */
    public DetailResourcesPfVO getOneById(Long resourceId) {
        Resources resources = resourcesDao.getOneById(resourceId);
        DetailResourcesPfVO vo = BeanUtil.convert(resources, DetailResourcesPfVO::new);
        // 查询父资源描述
        Long pid = resources.getPid();
        if (CheckUtil.nonNull(pid)) {
            long rootId = 0;
            String dec = "根节点";
            if (!Objects.equals(rootId, pid)) {
                Resources parent = resourcesDao.getOneById(pid);
                ExceptionHandler.requireNotNull(parent, CodeEnum.SERVICE_DATA_ERROR, "父资源不存在");
                dec = parent.getDescription();
            }
            vo.setParentDec(dec);
        }
        return vo;
    }

    /**
     * 新增
     *
     * @param dto dto
     * @return status
     */
    public Boolean save(SaveResourcesPfDTO dto) {
        // 检查父资源是否存在/启用
        this.checkParentResources(dto.getPid());

        Resources resources = BeanUtil.convert(dto, Resources::new);
        int row = resourcesDao.save(resources);
        ExceptionHandler.operateDbSingleOk(row, "新增资源失败");
        return true;
    }

    /**
     * 根据ID更新
     *
     * @param dto dto
     * @return status
     */
    public Boolean updateById(UpdateResourcesPfDTO dto) {
        // 检查父资源是否存在/启用
        this.checkParentResources(dto.getPid());

        Resources resources = BeanUtil.convert(dto, Resources::new);
        int row = resourcesDao.updateById(resources);
        ExceptionHandler.operateDbSingleOk(row, "更新资源失败");
        return true;
    }

    /**
     * 检查父资源是否存在/启用
     *
     * @param pid pid
     */
    private void checkParentResources(Long pid) {
        if (CheckUtil.nonNull(pid)) {
            Resources parent = resourcesDao.getOneById(pid);
            ExceptionHandler.requireNotNull(parent, CodeEnum.SERVICE_DATA_NULL, "父资源不存在");
            boolean parentEnable = Objects.equals(parent.getEnable(), true);
            ExceptionHandler.assertTrue(parentEnable, CodeEnum.SERVICE_DATA_ERROR, "父资源已被禁用,禁止挂载");
        }
    }

    /**
     * 根据ID删除
     *
     * @param dto dto
     * @return status
     */
    public Boolean deleteById(DeleteResourcesPfDTO dto) {
        Long resourceId = dto.getResourceId();
        ExceptionHandler.requireNotNull(resourceId, CodeEnum.PARAM_ERROR, "资源ID不能为空");

        // 存在关联的权限则禁止删除
        boolean exist = permResourcesDao.existRelation(resourceId);
        ExceptionHandler.assertTrue(!exist, CodeEnum.SERVICE_DATA_ERROR, "该资源与资源存在关联, 禁止删除");

        int row = resourcesDao.deleteById(resourceId);
        ExceptionHandler.operateDbSingleOk(row, "删除资源失败", true);
        return true;
    }

    /**
     * 切换资源启用状态
     *
     * @param dto dto
     * @return status
     */
    public Boolean toggleEnable(ToggleEnableResourcesPfDTO dto) {
        // 禁用时检查关联状态
        Boolean enable = dto.getEnable();
        if (Objects.equals(enable, false)) {
            Long resourceId = dto.getResourceId();
            // 权限与资源存在关联时禁止禁用
            boolean exist = permResourcesDao.existRelation(resourceId);
            ExceptionHandler.assertTrue(!exist, CodeEnum.SERVICE_DATA_ERROR, "该资源与权限存在关联, 禁止禁用");
        }

        Resources resources = BeanUtil.convert(dto, Resources::new);
        int row = resourcesDao.updateById(resources);
        ExceptionHandler.operateDbSingleOk(row, "切换失败");
        return true;
    }

    /**
     * 根据pid查询子资源树
     *
     * @param pid pid
     * @return list
     */
    public List<TreeResourcesPfVO> treeResources(Long pid) {
        ExceptionHandler.requireNotNull(pid, CodeEnum.PARAM_ERROR, "父ID不能为空");
        List<Resources> resources = resourcesDao.listByPid(pid);
        return BeanUtil.listConvert(resources, TreeResourcesPfVO::new, (source, target) -> {
            List<TreeResourcesPfVO> children = treeResources(target.getResourceId());
            target.setChildren(children);
        });
    }
}
