package com.hansen.sysmapper;

import com.hansen.base.BaseMapper;
import com.hansen.vo.SysParameterVo;
import com.model.SysParameterPo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 参数设置DAO
 */
public interface SysParameterMapper extends BaseMapper<SysParameterPo> {

    /**
     * @param rwoBounds
     * @return
     */
    @Select("select * from sys_parameter order by createTime desc")
    public List<SysParameterVo> findAll(RowBounds rwoBounds);

    /**
     * @return
     */
    @Select("select count(1) from sys_parameter")
    public long findCount();

}
