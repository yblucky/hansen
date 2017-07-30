package mapper;

import com.hansen.base.dao.CommonDao;
import com.hansen.model.UserDepartment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDepartmentMapper extends CommonDao<UserDepartment> {

    List<UserDepartment> getAll(@Param("parentUserId") String parentUserId);

    Double getSumAmt(@Param("parentUserId") String parentUserId);
}
