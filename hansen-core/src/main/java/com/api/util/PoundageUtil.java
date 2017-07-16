package com.api.util;

import java.math.BigDecimal;

/**
 * 四舍五入计算手续费
 * @date 2016年12月14日
 */
public class PoundageUtil {

	/**
	 * 手续费至少收一分钱
	 */
	private static final Double MIN_POUNDAGE = 0d;

	/**
	 * 默认保留两位小数位数，精确到分
	 */
	private static final Integer DEFAULT_DECIMAL = 2;

	/**
	 * @param score
	 *            积分
	 * @param poundageScale
	 *            收费比例
	 * @return
	 */
	public static Double getPoundage(Double score, Double poundageScale) {
		return getPoundage(score, poundageScale, DEFAULT_DECIMAL);
	}

	/**
	 * @param score
	 *            积分
	 * @param poundageScale
	 *            收费比例
	 * @param decimal
	 *            保留小数位数
	 * @return
	 */
	public static Double getPoundage(Double score, Double poundageScale, Integer decimal) {
		Double poundage = score * poundageScale;
		if (decimal == null) {
			decimal = DEFAULT_DECIMAL;
		}
		if (poundage < MIN_POUNDAGE) {
			poundage = MIN_POUNDAGE;
		} else { // 四舍五入计算手续费
			BigDecimal bd = new BigDecimal(poundage);
			poundage = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return poundage;
	}

}
