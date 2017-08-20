package com.common.utils.classutils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类复制
 */
public class MyBeanUtils {
	
	/**
	 * 对象复制
	 * @param src 源对象
	 * @param cls 目标类型
	 * @return 目标对象
	 * @throws Exception
	 */
	public static <T> T copyProperties(Object src,Class<T> cls) throws Exception{
		T target = cls.newInstance();
		BeanUtils.copyProperties(src,target);
		return target;
	}
	
	/**
	 * list集合复制
	 * @param srcs 源集合
	 * @param cls 目标集合类型
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> copyList(List<?> srcs,Class<T> cls) throws Exception{
		List<T> target = null;
		if(srcs != null){
			for(Object o:srcs){
				if(target == null){
					target = new ArrayList<T>();
				}
				target.add(copyProperties(o,cls));
			}
		}
		return target;
	}
}
