package com.pt.share.mapper;

import com.pt.share.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserMapper extends Mapper<User> , ExampleMapper<User> {


    User selectOneByUid(Long id);

    User selectByUsername(String username);

}
