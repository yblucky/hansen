package com.common.utils.FileUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 过滤非UTF-8字符集，如Emoji表情
 * @date 2017年08月07日
 */
public class EmojiUtil {

	private static final String DEFAULT_REPLACE_STR = "";

	/**
	 * 过滤非UTF-8字符集，如Emoji表情，用"?"代替，
	 */
	public static String replaceEmoji(String str) {
		return replaceEmoji(str, DEFAULT_REPLACE_STR);
	}

	/**
	 * 过滤非UTF-8字符集，如Emoji表情
	 */
	public static String replaceEmoji(String str, String replaceStr) {
		if (StringUtils.isBlank(str))
			return str;
		if (null == replaceStr)
			replaceStr = DEFAULT_REPLACE_STR;
		return str.replaceAll("[^\u0000-\uFFFF]", replaceStr);
	}

	public static String filterEmoji(String source) throws IOException {
		if (source != null) {
			Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\\xF0\\x9F\\x98\\x92\\xF0\\x9F]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
			Matcher emojiMatcher = emoji.matcher(source);
			if (emojiMatcher.find()) {
				source = emojiMatcher.replaceAll("");
				return source;
			}
			return source;
		}
		return source;
	}

	
}
