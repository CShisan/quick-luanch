package com.quick.domain.mapper;

import com.quick.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CShisan
 */
@Mapper
public interface UserMapper extends CustomBaseMapper<User> {
}
