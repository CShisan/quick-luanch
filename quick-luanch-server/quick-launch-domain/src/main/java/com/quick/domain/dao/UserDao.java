package com.quick.domain.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.domain.entity.User;

/**
 * @author CShisan
 */
public interface UserDao {

    /**
     * 根据uid获取user
     *
     * @param uid uid
     * @return user
     */
    User getOneById(Long uid);

    /**
     * 根据phone获取user
     *
     * @param phone phone
     * @return user
     */
    User getOneByPhone(String phone);

    /**
     * 根据email获取user
     *
     * @param email email
     * @return user
     */
    User getOneByEmail(String email);

    /**
     * 根据参数获取user
     *
     * @param user 请求参数
     * @return user
     */
    User getOneBy(User user);

    /**
     * 保存
     *
     * @param user user
     * @return row
     */
    int save(User user);

    /**
     * 根据uid更新
     *
     * @param user user
     * @return row
     */
    int updateById(User user);

    /**
     * 分页
     *
     * @param entity entity
     * @param page   page
     * @return page
     */
    IPage<User> page(User entity, IPage<User> page);
}
