package com.service;

import com.base.page.Paging;
import com.base.service.CommonService;
import com.model.SysParameter;
import com.vo.SysParameterVo;

import java.util.List;

/**
 * 参数设置业务层接口
 */

public interface ManageParameterService extends CommonService<SysParameter> {

    /**
     * 查找所有数据
     *
     * @param paging 分页
     * @return 集合
     * @throws Exception
     */
    public List<SysParameterVo> findAll(Paging paging) throws Exception;

    /**
     * 新增
     *
     * @param parameterVo
     * @throws Exception
     */
    public void add(SysParameterVo parameterVo) throws Exception;

    /**
     * 修改
     *
     * @param parameterVo
     * @throws Exception
     */
    public void update(SysParameterVo parameterVo) throws Exception;

    /**
     * 删除
     *
     * @param parameterVo
     */
    public void delete(SysParameterVo parameterVo);

    /**
     * 查找总记录数
     *
     * @return
     */
    public long findCount();

}
