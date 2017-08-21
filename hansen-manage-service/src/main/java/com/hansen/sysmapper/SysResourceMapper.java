package com.hansen.sysmapper;


import com.base.dao.BaseMapper;
import com.base.dao.CommonDao;
import com.hansen.vo.SysResourceVo;
import com.model.SysResourcePo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 资源DAO接口
 */
public interface SysResourceMapper extends BaseMapper<SysResourcePo>{

    @Select("SELECT sr.* from sys_resource sr INNER JOIN sys_role_permission srp on sr.id = srp.resourceId and sr.state = '10' and srp.roleId=#{roleId} order by sr.resSort")
    public List<SysResourceVo> findMenus(@Param("roleId") String roleId);

    /**
     * @param roleId
     * @return
     */
    @Select("SELECT res.*,srp.resourceId is not NULL selected FROM sys_resource res LEFT JOIN sys_role_permission srp on srp.resourceId=res.id and srp.roleId=#{roleId} and res.state='10' order by res.resSort")
    public List<SysResourceVo> findRoleMenus(@Param("roleId") String roleId);

}
