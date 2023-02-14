package com.quick.domain.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.quick.domain.dao.UserDao;
import com.quick.domain.entity.User;
import com.quick.domain.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author CShisan
 */
@Component
public class UserDaoImpl implements UserDao {
    private final UserMapper mapper;

    @Autowired
    public UserDaoImpl(UserMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据uid获取user
     *
     * @param uid uid
     * @return user
     */
    @Override
    public User getOneById(Long uid) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUid, uid);
        return mapper.selectOne(wrapper);
    }

    @Override
    public User getOneByPhone(String phone) {
        return null;
    }

    @Override
    public User getOneByEmail(String email) {
        return null;
    }

    /**
     * 根据参数获取user
     *
     * @param user 请求参数
     * @return user
     */
    @Override
    public User getOneBy(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        Long uid = user.getUid();
        wrapper.lambda().eq(ObjectUtils.isNotNull(uid), User::getUid, uid);

        String phone = user.getPhone();
        wrapper.lambda().eq(ObjectUtils.isNotEmpty(phone), User::getPhone, phone);

        String openId = user.getOpenId();
        wrapper.lambda().eq(ObjectUtils.isNotEmpty(openId), User::getOpenId, openId);

        return mapper.selectOne(wrapper);
    }

    @Override
    public int save(User user) {
        return mapper.insert(user);
    }

    /**
     * 根据uid更新
     *
     * @param user user
     * @return row
     */
    @Override
    public int updateById(User user) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(User::getUid, user.getUid());

        String username = user.getUsername();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(username), User::getUsername, username);

        String phone = user.getPhone();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(phone), User::getPhone, phone);

        String email = user.getEmail();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(email), User::getEmail, email);

        String avatar = user.getAvatar();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(avatar), User::getAvatar, avatar);

        String loginLastIp = user.getLoginLastIp();
        wrapper.lambda().set(ObjectUtils.isNotEmpty(loginLastIp), User::getLoginLastIp, loginLastIp);

        Date loginLastTime = user.getLoginLastTime();
        wrapper.lambda().set(ObjectUtils.isNotNull(loginLastTime), User::getLoginLastTime, loginLastTime);

        return mapper.update(new User(), wrapper);
    }

    /**
     * 分页
     *
     * @param entity entity
     * @param page   page
     * @return page
     */
    @Override
    public IPage<User> page(User entity, IPage<User> page) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        Long uid = entity.getUid();
        wrapper.lambda().eq(ObjectUtils.isNotNull(uid), User::getUid, uid);

        return mapper.selectPage(page, wrapper);
    }
}
