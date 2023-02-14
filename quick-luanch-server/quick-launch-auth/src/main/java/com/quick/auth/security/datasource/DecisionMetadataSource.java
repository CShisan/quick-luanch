package com.quick.auth.security.datasource;

import com.quick.common.utils.CheckUtil;
import com.quick.common.utils.RelationUtil;
import com.quick.common.utils.ResourceUtil;
import com.quick.domain.enums.ResourceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuanbai
 */
@Component
public class DecisionMetadataSource implements SecurityMetadataSource {
    private final ResourceUtil resourceUtil;

    @Autowired
    public DecisionMetadataSource(ResourceUtil resourceUtil) {
        this.resourceUtil = resourceUtil;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 先查询redis,不存在则查数据库
        Map<String, RelationUtil.Relation> bindRelation = resourceUtil.bindRelation(RelationUtil::keyBind, ResourceTypeEnum.BACK_END);
        Set<Map.Entry<String, RelationUtil.Relation>> entries = bindRelation.entrySet();

        // result为空, 则构造一个无效结果以防redis报错
        if (CheckUtil.isEmpty(entries)) {
            SecurityConfigAttribute invalid = SecurityConfigAttribute.invalid();
            return Collections.singletonList(invalid);
        }

        return entries.stream().map(entry -> {
            List<RelationUtil.Relation> permissions = Optional.ofNullable(entry.getValue())
                    .map(RelationUtil.Relation::getRelations).orElse(new ArrayList<>());
            List<String> permissionKeys = permissions.stream().map(RelationUtil.Relation::getKey).collect(Collectors.toList());

            // 构造SecurityConfigAttribute
            SecurityConfigAttribute attribute = new SecurityConfigAttribute();
            attribute.setPath(entry.getKey());
            attribute.setPermissions(permissionKeys);
            return attribute;
        }).collect(Collectors.toList());
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
