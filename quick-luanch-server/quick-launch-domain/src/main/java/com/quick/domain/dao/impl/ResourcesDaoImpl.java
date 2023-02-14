package com.quick.domain.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.domain.dao.ResourcesDao;
import com.quick.domain.entity.Resources;
import com.quick.domain.enums.ResourceTypeEnum;
import com.quick.domain.mapper.ResourcesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author CShisan
 */
@Component
public class ResourcesDaoImpl implements ResourcesDao {
    private final ResourcesMapper mapper;

    @Autowired
    public ResourcesDaoImpl(ResourcesMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据resourceIds查询list
     *
     * @param resourceIds resourceIds
     * @return list
     */
    @Override
    public List<Resources> listByResourceIds(List<Long> resourceIds) {
        QueryWrapper<Resources> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(Resources::getResourceId, resourceIds);
        wrapper.select("DISTINCT *");
        return mapper.selectList(wrapper);
    }

    /**
     * 分页
     *
     * @param entity entity
     * @param page   page
     * @return page
     */
    @Override
    public IPage<Resources> page(Resources entity, IPage<Resources> page) {
        QueryWrapper<Resources> wrapper = new QueryWrapper<>();

        Long resourceId = entity.getResourceId();
        wrapper.lambda().eq(ObjectUtils.isNotNull(resourceId), Resources::getResourceId, resourceId);

        ResourceTypeEnum type = entity.getType();
        wrapper.lambda().eq(ObjectUtils.isNotNull(type), Resources::getType, type);

        String description = entity.getDescription();
        wrapper.lambda().eq(ObjectUtils.isNotEmpty(description), Resources::getDescription, description);

        return mapper.selectPage(page, wrapper);
    }

    /**
     * 根据资源ID查询
     *
     * @param resourceId resourceId
     * @return entity
     */
    @Override
    public Resources getOneById(Long resourceId) {
        QueryWrapper<Resources> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Resources::getResourceId, resourceId);
        return mapper.selectOne(wrapper);
    }

    /**
     * 新增
     *
     * @param resources resources
     * @return status
     */
    @Override
    public int save(Resources resources) {
        return mapper.insert(resources);
    }

    /**
     * 根据ID更新
     *
     * @param resources resources
     * @return row
     */
    @Override
    public int updateById(Resources resources) {
        UpdateWrapper<Resources> wrapper = new UpdateWrapper<>();
        Long resourceId = resources.getResourceId();
        wrapper.lambda().eq(Resources::getResourceId, resourceId);

        ResourceTypeEnum type = resources.getType();
        wrapper.lambda().set(ObjectUtils.isNotNull(type), Resources::getType, type);

        String path = resources.getPath();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(path), Resources::getPath, path);

        String description = resources.getDescription();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(description), Resources::getDescription, description);

        Long pid = resources.getPid();
        wrapper.lambda().set(ObjectUtils.isNotNull(pid), Resources::getPid, pid);

        Boolean enable = resources.getEnable();
        wrapper.lambda().set(ObjectUtils.isNotNull(enable), Resources::getEnable, enable);

        return mapper.update(new Resources(), wrapper);
    }

    /**
     * 根据ID删除
     *
     * @param resourceId resourceId
     * @return row
     */
    @Override
    public int deleteById(Long resourceId) {
        QueryWrapper<Resources> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Resources::getResourceId, resourceId);
        return mapper.delete(wrapper);
    }

    /**
     * 根据pid查询列表
     *
     * @param pid pid
     * @return list
     */
    @Override
    public List<Resources> listByPid(Long pid) {
        QueryWrapper<Resources> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Resources::getPid, pid);
        return mapper.selectList(wrapper);
    }

    /**
     * 查询全部
     *
     * @return list
     */
    @Override
    public List<Resources> listAll() {
        return mapper.selectList(new QueryWrapper<>());
    }

    /**
     * 查询对应类型的全部记录
     *
     * @param type type
     * @return list
     */
    @Override
    public List<Resources> listByType(ResourceTypeEnum type) {
        QueryWrapper<Resources> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Resources::getType, type);
        return mapper.selectList(wrapper);
    }

    /**
     * 查询全部
     *
     * @return list
     */
    @Override
    public List<Resources> all() {
        return mapper.selectList(new QueryWrapper<>());
    }
}
