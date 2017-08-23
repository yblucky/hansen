package com.utils.numberutils;

import java.util.Random;
import java.util.UUID;

/**
 * UUID生成器
 *
 * @date 2016年12月7日
 */
public class UUIDUtil {

	/**
	 * 生成一个UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	/**
	 * 生成多个UUID
	 * 
	 * @param number
	 * @return
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}


	/**
	 * 生成UUID
	 *
	 * @return
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().toUpperCase();
	}

	/**
	 *随机生成8位数授权码
	 *
	 */
	public static String randomUtil(){
		Random r = new Random();
		String code = "";
		for (int i = 0; i < 64; ++i) {
			int temp = r.nextInt(52);
			char x = (char) (temp < 26 ? temp + 97 : (temp % 26) + 65);
			code += x;
		}
		return code;
	}

	public static void main(String[] args) {
		System.out.println(generateUUID());
	}

}
