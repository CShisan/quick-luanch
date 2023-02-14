package com.quick.domain.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quick.domain.dao.PermResourcesDao;
import com.quick.domain.entity.PermResources;
import com.quick.domain.mapper.PermResourcesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author CShisan
 */
@Component
public class PermResourcesDaoImpl implements PermResourcesDao {
    private final PermResourcesMapper mapper;

    @Autowired
    public PermResourcesDaoImpl(PermResourcesMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据权限ID查询
     *
     * @param permissionId permissionId
     * @return entity
     */
    @Override
    public List<PermResources> listByPermissionId(Long permissionId) {
        QueryWrapper<PermResources> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermResources::getPermissionId, permissionId);
        return mapper.selectList(wrapper);
    }

    /**
     * 根据资源ID查询
     *
     * @param resourceId resourceId
     * @return entity
     */
    @Override
    public List<PermResources> listByResourceId(Long resourceId) {
        QueryWrapper<PermResources> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermResources::getResourceId, resourceId);
        return mapper.selectList(wrapper);
    }

    /**
     * 根据权限IDS查询
     *
     * @param permissionIds permissionIds
     * @return entity
     */
    @Override
    public List<PermResources> listByPermissionIds(List<Long> permissionIds) {
        QueryWrapper<PermResources> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PermResources::getPermissionId, permissionIds);
        return mapper.selectList(wrapper);
    }

    /**
     * 根据资源IDS查询
     *
     * @param resourceIds resourceIds
     * @return entity
     */
    @Override
    public List<PermResources> listByResourceIds(List<Long> resourceIds) {
        QueryWrapper<PermResources> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PermResources::getResourceId, resourceIds);
        return mapper.selectList(wrapper);
    }

    /**
     * 根据资源ID查询是否存在绑定关系
     *
     * @param resourceId resourceId
     * @return status
     */
    @Override
    public boolean existRelation(Long resourceId) {
        QueryWrapper<PermResources> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermResources::getResourceId, resourceId);
        wrapper.last("LIMIT 1");
        return mapper.exists(wrapper);
    }

    /**
     * 新增
     *
     * @param entity entity
     * @return row
     */
    @Override
    public int save(PermResources entity) {
        return mapper.insert(entity);
    }

    /**
     * 批量新增
     *
     * @param entityList entityList
     * @return row
     */
    @Override
    public int saveBatch(List<PermResources> entityList) {
        return mapper.insertBatchSomeColumn(entityList);
    }

    /**
     * 查询所有
     *
     * @return list
     */
    @Override
    public List<PermResources> all() {
        return mapper.selectList(new QueryWrapper<>());
    }

    /**
     * 批量删除
     *
     * @param permissionId permissionId
     * @param reduceList   reduceList
     * @return row
     */
    @Override
    public int deleteBatch(Long permissionId, List<Long> reduceList) {
        QueryWrapper<PermResources> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermResources::getPermissionId, permissionId);
        wrapper.lambda().in(PermResources::getResourceId, reduceList);
        return mapper.delete(wrapper);
    }
}
