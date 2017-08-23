package com.utils.numberutils;

import java.util.Random;
import java.util.Set;

public final class RandomUtil {

	/**
	 * 获取两个数字之前的随机数
	 * @return
	 */
	public static double randomDouble() {
		Random rand = new Random();
		return rand.nextDouble();
	}


	/**
	 * 获取两个数字之前的随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomInt(int min, int max) {
		Random rand = new Random();
		if (min == max)
			return min;
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}
		return rand.nextInt(max - min) + min;
	}

	/**
	 * 获取随机字符串
	 * 
	 * @param len
	 * @return
	 */
	public static String randomString(int len) {
		if (len <= 0) {
			return "";
		}
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * 获取随机字符串
	 * 
	 * @param len
	 * @return
	 */
	public static String randomNumString(int len) {
		if (len <= 0) {
			return "";
		}
		String base = "012345678901234567890123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}


	/**
	 * 从set中随机取得一个元素
	 * @param set
	 * @return
	 */
	public static <E> E getRandomElement(Set<E> set){
		int rn = randomInt(0,set.size());
		int i = 0;
		for (E e : set) {
			if(i==rn){
				return e;
			}
			i++;
		}
		return null;
	}
}
