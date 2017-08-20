package hansen.service;

import com.base.service.CommonService;
import com.common.utils.ParamUtil;
import com.common.utils.httputils.HttpUtil;
import com.model.Parameter;

import java.util.List;

/**
 * 获取系统参数公用service
 */
public interface ParameterService extends CommonService<Parameter> {

    /**
     * 获取系统参数列表
     * @return
     * @throws Exception
     */
    List<Parameter> getList() throws Exception;

    Double getRmbConvertCoinRate(String id,String name);

}
