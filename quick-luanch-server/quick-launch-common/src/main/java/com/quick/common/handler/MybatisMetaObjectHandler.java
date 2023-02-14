package com.quick.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.quick.common.utils.CheckUtil;
import com.quick.common.utils.HttpUtil;
import com.quick.domain.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;
import java.util.Optional;

/**
 * @author CShisan
 */
@Slf4j
public class MybatisMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        BaseEntity entity = (BaseEntity) metaObject.getOriginalObject();
        if (CheckUtil.isEmpty(entity.getEditor())) {
            fillEditor(metaObject, getUid());
        }
        if (CheckUtil.isNull(entity.getDeleted())) {
            fillDeleted(metaObject);
        }
        fillCurrentTime(metaObject, "createTime", "updateTime");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        fillCurrentTime(metaObject, "updateTime");
        fillEditor(metaObject, getUid());
    }

    /**
     * 填充当前时间
     *
     * @param metaObject metaObject
     * @param fieldNames fieldNames
     */
    private void fillCurrentTime(MetaObject metaObject, String... fieldNames) {
        Date date = new Date();
        for (String fieldName : fieldNames) {
            this.strictInsertFill(metaObject, fieldName, Date.class, date);
        }
    }

    /**
     * 填充编辑者ID
     *
     * @param metaObject metaObject
     * @param uid        uid
     */
    private void fillEditor(MetaObject metaObject, Long uid) {
        this.strictInsertFill(metaObject, "editor", Long.class, uid);
    }

    /**
     * 填充逻辑删除
     *
     * @param metaObject metaObject
     */
    private void fillDeleted(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "deleted", Boolean.class, false);
    }

    /**
     * 获取编辑者id
     *
     * @return uid
     */
    private Long getUid() {
        return Optional.ofNullable(HttpUtil.currentUid()).orElseGet(() -> {
            log.error(LogHandler.format("MyBatis-Plus警告", "获取当前用户uid失败,自动设置为管理员uid[9527]"));
            return 9627L;
        });
    }
}
