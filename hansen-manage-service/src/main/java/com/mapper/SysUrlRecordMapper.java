package com.mapper;


import com.base.dao.CommonDao;
import com.model.SysUrlRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * URL不拦截记录数据操作类
 */

@Repository
public interface SysUrlRecordMapper extends CommonDao<SysUrlRecord> {

    public List<String> findUrl();

    /**
     * 分页查询URl记录信息
     *
     * @param rowBounds
     * @return
     */
    public List<SysUrlRecord> loadRecords(RowBounds rowBounds);

    /**
     * 查询URl记录信息列表总记录数
     *
     * @return 总记录数
     */
    public int findAllCount();

    /**
     * 修改URL记录
     *
     * @param recordPo
     */
    public void updateRecord(@Param("po") SysUrlRecord recordPo);

    /**
     * 删除单条URL记录
     *
     * @param recordPo
     */
    public void deleteRecord(@Param("po") SysUrlRecord recordPo);

    /**
     * 删除多条URl记录
     *
     * @param delIds
     * @return
     */
    public int deleteRecords(List<String> delIds);

}
