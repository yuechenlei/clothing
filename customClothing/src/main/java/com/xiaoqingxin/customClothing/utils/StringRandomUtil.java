package com.xiaoqingxin.customClothing.utils;

import java.util.Random;

/** 生成随机数字和字母组合 */
public class StringRandomUtil {

	/**
	 * 大、小写字母与数字组合
	 * 
	 * @param length 表示生成几位随机数
	 * @return String
	 */
	public static String getStringRandom(int length) {
		String value = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";

			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				// 排除B和b，O和o，小写的L
				int echar = random.nextInt(26) + temp;
				while (echar == 66 || echar == 98 || echar == 79 || echar == 111 || echar == 108) {
					echar = random.nextInt(26) + temp;
				}
				value += (char) echar;
			} else {
				// 排除4和0
				int eint = random.nextInt(10);
				while (eint == 4 || eint == 0) {
					eint = random.nextInt(10);
				}
				value += String.valueOf(eint);
			}
		}
		return value;
	}

	/**
	 * 大写字母与数字组合
	 * 
	 * @param length 表示生成几位随机数
	 * @return String
	 */
	public static String getUpperStringRandom(int length) {
		String value = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";

			if ("char".equalsIgnoreCase(charOrNum)) {
				// 大写字母
				int temp = 65;
				// 排除B，O
				int echar = random.nextInt(26) + temp;
				while (echar == 66 || echar == 79) {
					echar = random.nextInt(26) + temp;
				}
				value += (char) echar;
			} else {
				// 排除4和0
				int eint = random.nextInt(10);
				while (eint == 4 || eint == 0) {
					eint = random.nextInt(10);
				}
				value += String.valueOf(eint);
			}
		}
		return value;
	}

	/**
	 * 随机生成数字
	 * 
	 * @param length 表示生成几位随机数
	 * @return String
	 */
	public static String getNumStringRandom(int length) {
		String value = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 排除4
			int num = random.nextInt(10);
			while (num == 4) {
				num = random.nextInt(10);
			}
			value += String.valueOf(num);

		}
		return value;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(getNumStringRandom(6));
		}
	}

}
