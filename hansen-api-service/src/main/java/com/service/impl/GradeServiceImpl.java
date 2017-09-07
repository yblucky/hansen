package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.GradeType;
import com.mapper.GradeMapper;
import com.model.Grade;
import com.model.User;
import com.model.UserDepartment;
import com.service.GradeService;
import com.service.UserDepartmentService;
import com.service.UserService;
import com.utils.numberutils.CurrencyUtil;
import com.utils.toolutils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @date 2016年11月27日
 */
@Service
public class GradeServiceImpl extends CommonServiceImpl<Grade> implements GradeService {
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDepartmentService userDepartmentService;

    @Override
    protected CommonDao<Grade> getDao() {
        return gradeMapper;
    }

    @Override
    protected Class<Grade> getModelClass() {
        return Grade.class;
    }

    /**
     * 获取会员等级信息
     *
     * @param grade 等级code
     */
    @Override
    public Grade getGradeDetail(Integer grade) {
        Grade model = new Grade();
        model.setGrade(grade);
        return this.readOne(model);
    }

    /**
     * 会员业绩等级计算 :加业绩，同时判断用户等级
     *
     * @param userId 用户id
     */
    @Override
    public Grade getUserGrade(String userId) throws Exception {
        User user = userService.readById(userId);
//        if (user.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode().intValue()) {
//            System.out.println("用户不是激活状态");
//            return null;
//        }
        //获取用户的所有部门（不包含设有接点人的部门）
        List<UserDepartment> list = userDepartmentService.getAll(userId);
        //获取用户的所有部门的总业绩（不包含设有接点人的部门）
        Double sumAmt = userDepartmentService.getSumAmt(userId);
        //部门数量小于2个或所有部门总业绩不达标则不计算
        if (list == null || list.size() < 2 || sumAmt < 100000) {
            System.out.println("用户未达到升级条件");
            return null;
        }
        Integer last = 0;
        for (GradeType gradeType : GradeType.values()) {
            List<Integer> gradeList = new ArrayList<>();

            //县 市  省  董事判断条件
            //获取最大三个部门的等级
            List<UserDepartment> departments = userDepartmentService.getDirectMaxGradeList(userId);
            if (departments == null || ToolUtil.isNotEmpty(departments)) {
                for (UserDepartment userDepartment : departments) {
                    gradeList.add(userDepartment.getGrade());
                }
                Collections.sort(gradeList);
                if (gradeList.get(0) > GradeType.GRADE7.getCode()) {
                    //三个最小是大于8 董事
                    return this.getGradeDetail(GradeType.GRADE8.getCode());
                } else if (gradeList.get(0) > GradeType.GRADE6.getCode()) {
                    //三个最小是大于7 省代
                    return this.getGradeDetail(GradeType.GRADE7.getCode());
                } else if (gradeList.get(0) > GradeType.GRADE5.getCode()) {
                    //三个最小是大于6 市代
                    return this.getGradeDetail(GradeType.GRADE6.getCode());
                } else if (gradeList.get(0) > GradeType.GRADE4.getCode()) {
                    //三个最小是大于5 县代
                    return this.getGradeDetail(GradeType.GRADE5.getCode());
                }
            }
        }
        Grade grade =null;
       for (int i=0;i<4;i++){
           grade= this.getGradeDetail(4-i);
           //专员 10万，小部分必须大于20%   专员  主任  经理 区代的判断条件一致
           Double firstMaxAmt = list.get(0).getPerformance();
           Double sedMaxAmt = list.get(1).getPerformance();
           Double smallAmt = firstMaxAmt > sedMaxAmt ? sedMaxAmt : firstMaxAmt;
           Double sumMax = CurrencyUtil.getPoundage(firstMaxAmt + sedMaxAmt, 1d);
           if (sumMax >= grade.getSumPerformance() && sumMax * grade.getMinScale() <= sedMaxAmt) {
               return this.getGradeDetail(4-i);
           }
       }
        return null;
    }



    /**
     * 会员业绩等级计算
     *
     * @param userId 用户id
     */
    @Override
    public Grade getUserGrade2(String userId) throws Exception {
        User user = userService.readById(userId);
        if (user.getGrade() == null) {
            user.setGrade(0);
        }
        if (user.getGrade() == GradeType.GRADE8.getCode().intValue()) {
            System.out.println("已是最高等级");
            return null;
        }
        //获取用户的所有部门（不包含设有接点人的部门）
        List<UserDepartment> list = userDepartmentService.getAll(userId);
        //获取用户的所有部门的总业绩（不包含设有接点人的部门）
        Double sumAmt = userDepartmentService.getSumAmt(userId);
        //获取用户的三个部门用户的最高等级的级别
        Integer maxGrade = userDepartmentService.getMaxGrade(userId);
        //部门数量小于2个或所有部门总业绩不达标则不计算
        if (list == null || list.size() < 2 || sumAmt < 100000) {
            System.out.println("用户未达到升级条件");
            return null;
        }
        //市代、省代、董事长符合即返回
        if (maxGrade.intValue() > 0 && (maxGrade.intValue() + 1) > user.getGrade().intValue()) {
            return this.getGradeDetail(maxGrade + 1);
        }
        for (GradeType gradeType : GradeType.values()) {
            if (user.getGrade().intValue() >= gradeType.getCode().intValue()) {
                continue;
            }
            Grade grade = this.getGradeDetail(gradeType.getCode());
            switch (gradeType) {
                //专员、主任、经理的最大两个部门大于10W,30W,50W业绩，其中最小部门不小于总业绩的20%
                case GRADE1:
                case GRADE2:
                case GRADE3:
                case GRADE4: {
                    Double firstMaxAmt = list.get(0).getPerformance();
                    Double sedMaxAmt = list.get(1).getPerformance();
                    Double sumMax = CurrencyUtil.getPoundage(firstMaxAmt + sedMaxAmt, 1d);
                    Double minProfit = CurrencyUtil.getPoundage(sumAmt * 0.2, 1d);
                    if (sumMax >= grade.getSumPerformance() && sedMaxAmt >= minProfit) {
                        return grade;
                    }
                    break;
                }
            }
        }
        return null;
    }
}
