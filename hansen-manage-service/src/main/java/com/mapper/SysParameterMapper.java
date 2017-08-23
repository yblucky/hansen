package com.mapper;


import com.base.dao.CommonDao;
import com.model.SysParameter;
import com.vo.SysParameterVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 参数设置DAO
 */
@Repository
public interface SysParameterMapper extends CommonDao<SysParameter> {

    /**
     * @param rwoBounds
     * @return
     */
    public List<SysParameterVo> findAll(RowBounds rwoBounds);

    /**
     * @return
     */
    public long findCount();

}
