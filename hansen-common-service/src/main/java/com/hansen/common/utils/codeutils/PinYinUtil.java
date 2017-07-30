package com.hansen.common.utils.codeutils;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.HashMap;

/**
 *
 * @author zzwei
 * @since 2017-07-14
 *
 */
public class PinYinUtil {
    private static HashMap<String, String> replace = new HashMap<>();

    static {

    }

    public static String getFirstLetter(String hanzi) {
        if (replace.containsKey(hanzi)) return replace.get(hanzi);
        char char1 = hanzi.charAt(0);
        String[] toHanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(char1);
        if (toHanyuPinyinStringArray != null && toHanyuPinyinStringArray.length > 0) {
            return toHanyuPinyinStringArray[0].substring(0, 1);
        } else {
            return "#";
        }
    }

    public static void main(String[] args) {
        System.out.println(getFirstLetter("北京欢迎你"));
    }
}
