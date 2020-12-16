package com.pt.share.mapper;

import com.pt.share.entity.Role;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RoleMapper extends Mapper<Role> {


    List<Role> selectByUid(Long uid);

}
