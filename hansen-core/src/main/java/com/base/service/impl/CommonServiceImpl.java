package com.base.service.impl;

import com.base.dao.CommonDao;
import com.base.page.DBPage;
import com.BaseModel;
import com.utils.toolutils.ToolUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class CommonServiceImpl<M extends BaseModel> {

    public final Log logger = LogFactory.getLog(getClass());

    protected abstract CommonDao<M> getDao();

    protected abstract Class<M> getModelClass();

    protected void processCacheAfterCreate(M model) {
        // default do nothing
    }

    protected void processCacheAfterDelete(String id) {
        // default do nothing
    }

    protected void processCacheAfterUpdateById(String id, M model) {
        // default do nothing
    }

    protected String getModelName() {
        try {
            Class<M> clz = getModelClass();
            if (clz != null) return clz.getSimpleName();
        } catch (Exception e) {
        }
        return "";
    }

    // C
    // 创建方法捕捉异常，打出日志后，抛出RuntimeException以便上层事务回滚
    public void create(M model) {
        try {
            defaultCreate(model);
            getDao().create(model);
            processCacheAfterCreate(model);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("[Layer:DB]_[Method:create]_[Info:Model:" + getModelName() + "]", e);
            }
            throw new RuntimeException(e);
        }
    }

    public void createWithUUID(M model) {
        try {
//            defaultStatus(model);
            model.setId(ToolUtil.getUUID());
            getDao().create(model);
            processCacheAfterCreate(model);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("[Layer:DB]_[Method:create]_[Info:Model:" + getModelName() + "]", e);
            }
            throw new RuntimeException(e);
        }
    }

    // R
    public M readById(String id) {
        try {
            System.out.println(getDao().getClass().getName());
            return getDao().readById(id);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("[Layer:DB]_[Method:readOne]_[Info:Model:" + getModelName() + "#ID:" + id + "]", e);
            }
        }
        return null;
    }


    public M readOne(M model) {
        try {
            System.out.println("-====================="+model.toString());
            System.out.println("getDao()"+getDao());
//            defaultStatus(model);
            return getDao().readOne(model);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("[Layer:DB]_[Method:readOne]_[Info:Model:" + getModelName() + "]", e);
            }
        }
        return null;
    }

    // pageNo 业务上的起始页，一般从1开始
    public List<M> readList(M model, int pageNo, int pageSize, int totalRow) {
        DBPage dbPage = DBPage.page(pageNo, pageSize);
        if (totalRow != 0) {
            dbPage.setTotalRow(totalRow);
        }
        List<M> list = null;
        try {
//            defaultStatus(model);
            list = getDao().readList(model, dbPage.getStartRow(), dbPage.getPageSize());
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("[Layer:DB]_[Method:readList]_[Info:Model:" + getModelName() + "#pageNo:" + pageNo + "#pageSize:" + pageSize + "]", e);
            }
        }
        return CollectionUtils.isEmpty(list) ? new ArrayList<M>(0) : list;
    }

    public List<M> readAll(M model) {
        List<M> list = null;
        try {
//            defaultStatus(model);
            list = getDao().readList(model, 0, 1000);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("[Layer:DB]_[Method:readAll]_[Info:Model:" + getModelName() + "]", e);
            }
        }
        return CollectionUtils.isEmpty(list) ? new ArrayList<M>(0) : list;
    }

    // 出错时统一返回0
    public int readCount(M model) {
        Integer count = 0;
        try {
//            defaultStatus(model);
            count = getDao().readCount(model);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("[Layer:DB]_[Method:readCount]_[Info:Model:" + getModelName() + "]", e);
            }
        }
        return count == null ? 0 : count;
    }

    // U
    // 更新方法捕捉异常，打出日志后，抛出RuntimeException以便上层事务回滚
    public void updateById(String id, M model) {
        try {
            M dbModel = readById(id);
            if (dbModel == null) return;
//            defaultUpdate(model);
            getDao().updateById(id, model);
            processCacheAfterUpdateById(id, dbModel);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("[Layer:DB]_[Method:updateById]_[Info:Model:" + getModelName() + "#ID:" + id + "]", e);
            }
            throw new RuntimeException(e);
        }
    }

    // D
    // 删除方法捕捉异常，打出日志后，抛出RuntimeException以便上层事务回滚
    public void deleteById(String id) {
        try {
            getDao().deleteById(id);
            processCacheAfterDelete(id);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("[Layer:DB]_[Method:deleteById]_[Info:Model:" + getModelName() + "#ID:" + id + "]", e);
            }
            throw new RuntimeException(e);
        }
    }

    public void defaultCreate(M model) {
        try {
            Integer status = model.getStatus();
            status = status == null ? 1 : status;
            model.setStatus(status);
            if (StringUtils.isEmpty(model.getId())) {
                model.setId(ToolUtil.getUUID());
            }
            if (model.getCreateTime() == null) {
                model.setCreateTime(new Date());
            }
        } catch (Exception e) {
            model.setStatus(1);
        }
    }

    public void defaultUpdate(M model) {
        try {
            Integer status = model.getStatus();
            status = status == null ? 0 : status;
            model.setStatus(status);
            model.setUpdateTime(new Date());
        } catch (Exception e) {
            model.setStatus(1);
        }
    }
}
