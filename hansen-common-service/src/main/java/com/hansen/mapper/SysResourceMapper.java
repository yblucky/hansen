package com.hansen.mapper;


import com.base.dao.CommonDao;
import com.hansen.vo.SysResourceVo;
import com.model.SysResource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
