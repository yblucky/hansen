package com.hansen.sysmapper;


import com.base.dao.BaseMapper;
import com.base.dao.CommonDao;
import com.hansen.vo.SysUserVo;
import com.model.SysUserPo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 用户信息DAO
 */
public interface SysUserMapper extends BaseMapper<SysUserPo>{

    /**
     * 用户登录
     *
     * @param loginName
     * @return
     */
    @Select("select * from sys_user where loginName=#{ln}")
    public SysUserVo findByloginName(@Param("ln") String loginName);

    /**
     * 分页查询
     *
     * @param rwoBounds
     * @return
     */
    @Select("select id,userName,loginName,mobile,roleId,roleName,userIcon,createTime,lastTime,state from sys_user order by createTime desc")
    public List<SysUserVo> findAll(RowBounds rwoBounds);

    /**
     * 查总记录数
     *
     * @return
     */
    @Select("select count(1) from sys_user")
    public long findCount();

    /**
     * 查找登录名是否存在
     *
     * @param loginName
     * @return
     */
    @Select("select count(1) from sys_user where loginName=#{ln}")
    public int findLoginName(@Param("ln") String loginName);

}
