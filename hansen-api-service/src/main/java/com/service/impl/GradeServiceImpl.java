package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.GradeType;
import com.constant.UserStatusType;
import com.mapper.GradeMapper;
import com.service.GradeService;
import com.service.UserDepartmentService;
import com.service.UserService;
import com.model.Grade;
import com.model.User;
import com.model.UserDepartment;
import com.utils.numberutils.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 会员业绩等级计算
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
        if (list == null || list.size() < 2 || sumAmt < 300000) {
            System.out.println("用户未达到升级条件");
            return null;
        }
        for (GradeType gradeType : GradeType.values()) {
            Grade grade = this.getGradeDetail(gradeType.getCode());
            //1星等级特殊判断，业绩最大的两个部门之和要达到30万且业绩较小的部门业绩要大于10万
            if (gradeType.getCode().intValue() == GradeType.GRADE1.getCode()) {
                Double firstMaxAmt = list.get(0).getPerformance();
                Double sedMaxAmt = list.get(1).getPerformance();
                Double sumMax = CurrencyUtil.getPoundage(firstMaxAmt + sedMaxAmt, 1d);
                if (sumMax >= grade.getSumPerformance() && sedMaxAmt >= 100000) {
                    return grade;
                }
                continue;
            }
            //2星到5星有规则判断，除去最大几个部门，剩余部门业绩之和占总业绩之比率
            if (sumAmt > grade.getSumPerformance() && list != null && list.size() > grade.getRemoveNo()) {
                Double maxAmt = 0d;
                //去除5个业绩最大部门
                for (int i = 0; i < grade.getRemoveNo(); i++) {
                    maxAmt = CurrencyUtil.getPoundage(maxAmt + list.get(i).getPerformance(), 1d);
                }
                //其余部门业绩之和必须大于总业绩的10%
                Double diffAmt = CurrencyUtil.getPoundage(sumAmt - maxAmt, 1d);
                if (diffAmt > grade.getSumPerformance() * grade.getRemainScale()) {
                    return grade;
                }
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
        if(user.getGrade() == null){
            user.setGrade(0);
        }
        if(user.getGrade() == GradeType.GRADE8.getCode().intValue()){
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
        if(maxGrade.intValue() > 0 && (maxGrade.intValue()+1) > user.getGrade().intValue()){
            return this.getGradeDetail(maxGrade+1);
        }
        for (GradeType gradeType : GradeType.values()) {
            if(user.getGrade().intValue() >= gradeType.getCode().intValue()){
                continue;
            }
            Grade grade = this.getGradeDetail(gradeType.getCode());
            switch (gradeType){
                //专员、主任、经理的最大两个部门大于10W,30W,50W业绩，其中最小部门不小于总业绩的20%
                case GRADE1:
                case GRADE2:
                case GRADE3:
                case GRADE4:{
                    Double firstMaxAmt = list.get(0).getPerformance();
                    Double sedMaxAmt = list.get(1).getPerformance();
                    Double sumMax = CurrencyUtil.getPoundage(firstMaxAmt + sedMaxAmt, 1d);
                    Double minProfit = CurrencyUtil.getPoundage(sumAmt*0.2,1d);
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
