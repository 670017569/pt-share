package com.pt.share.mapper;

import com.pt.share.entity.Permission;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface PermissionMapper extends Mapper<Permission> {

    List<Permission> selectPermissionByRoleId(Long id);

}
