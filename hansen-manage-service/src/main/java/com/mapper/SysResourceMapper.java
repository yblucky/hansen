package com.mapper;


import com.base.dao.CommonDao;
import com.model.SysResource;
import com.vo.SysResourceVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资源DAO接口
 */
@Repository
public interface SysResourceMapper extends CommonDao<SysResource> {

    public List<SysResourceVo> findMenus(@Param("roleId") String roleId);

    /**
     * @param roleId
     * @return
     */
    public List<SysResourceVo> findRoleMenus(@Param("roleId") String roleId);

}
