package com.mapper;


import com.base.dao.CommonDao;
import com.model.SysUser;
import com.vo.SysUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户信息DAO
 */
@Repository
public interface SysUserMapper extends CommonDao<SysUser> {

    /**
     * 用户登录
     *
     * @param loginName
     * @return
     */
    public SysUserVo findByloginName(@Param("ln") String loginName);

    /**
     * 分页查询
     *
     * @param rwoBounds
     * @return
     */
    public List<SysUserVo> findAll(RowBounds rwoBounds);

    /**
     * 查总记录数
     *
     * @return
     */
    public long findCount();

    /**
     * 查找登录名是否存在
     *
     * @param loginName
     * @return
     */
    public int findLoginName(@Param("ln") String loginName);

}
