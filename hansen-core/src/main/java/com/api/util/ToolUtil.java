package com.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class ToolUtil {
    /**
     * 赤道半径
     */
    private static final double EARTH_RADIUS = 6378137;

    /***
     * 判断传入的对象是否为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值, 为空或等于0时返回true
     */
    public static boolean isEmpty(Object obj) {
        return checkObjectIsEmpty(obj, true);
    }

    public static Double getBigDecimalDouble4(Double cost) {
        BigDecimal b = new BigDecimal(cost);
        return b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /***
     * 判断传入的对象是否不为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值, 不为空或不等于0时返回true
     */
    public static boolean isNotEmpty(Object obj) {
        return checkObjectIsEmpty(obj, false);
    }

    private static boolean checkObjectIsEmpty(Object obj, boolean bool) {
        if (null == obj)
            return bool;
        else if (obj == "")
            return bool;
        else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
            try {
                Double.parseDouble(obj + "");
            } catch (Exception e) {
                return bool;
            }
        } else if (obj instanceof String) {
            if (((String) obj).length() <= 0)
                return bool;
            if ("null".equals(obj))
                return bool;
        } else if (obj instanceof Map) {
            if (((Map<?, ?>) obj).size() == 0)
                return bool;
        } else if (obj instanceof List) {
            if (((List<?>) obj).size() == 0)
                return bool;
        } else if (obj instanceof Set) {
            if (((Set<?>) obj).size() == 0)
                return bool;
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == 0)
                return bool;
        }
        return !bool;
    }

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (isNotEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (isNotEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * java对象转换成json
     *
     * @param object java 对象
     * @return
     */
    public static String objectToJson(Object object) {
        return JSON.toJSON(object).toString();
    }

    /***
     * json字符串 转换成java对象
     * @param jsonObjectString json 字符串
     * @param clazz
     * @return Object
     */
    public static Object jsonToObject(String jsonObjectString, Class<?> clazz) {
        if (ToolUtil.isEmpty(jsonObjectString)) {
            return null;
        }
        return JSON.parseObject(jsonObjectString, clazz);
    }

    /***
     * 通过jsonObjectString字符串 获取 java 对象
     *
     *
     * @param jsonObjectString
     *            json 字符串
     * @param clazz
     * @return
     */

    public static Object getJavaObject(String jsonObjectString, Class<?> clazz) {
        if (ToolUtil.isEmpty(jsonObjectString)) {
            return null;
        }

        return JSON.parseObject(jsonObjectString, clazz);
    }

    /**
     * 获取 JSONObject 实例
     *
     * @return
     */
    public static JSONObject getJSONObject() {
        return new JSONObject();

    }

    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    public static boolean isMobileNumber(String mobiles) {
        return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[^1^4,\\D]))\\d{8}").matcher(mobiles).matches();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * 获取两个数组不同的值的集合
     *
     * @param listA
     * @param listB
     * @return
     */
    public static List<String> getDifference(List<String> listA, List<String> listB) {
        Map<String, Integer> map = new HashMap<String, Integer>(listA.size() + listB.size());
        List<String> diff = new ArrayList<String>();
        for (String string : listA) {
            map.put(string, 1);
        }
        for (String string : listB) {
            Integer cc = map.get(string);
            if (cc != null) {
                map.put(string, ++cc);
                continue;
            }
            map.put(string, 1);
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }
        return diff;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        System.out.println(ToolUtil.isEmpty(list));
    }


    /**
     * String类型转Integer 默认为0.
     *
     * @param sValue
     * @return
     */
    public static int parseInt(String sValue) {
        return parseInt(sValue, 0);
    }

    /**
     * String类型转Integer
     *
     * @param sValue
     * @param iDefault
     * @return
     */
    public static int parseInt(String sValue, int iDefault) {
        if (sValue == null) return iDefault;
        int _iValue = iDefault;
        try {
            _iValue = Integer.parseInt(sValue);
        } catch (NumberFormatException e) {
            ;
        }
        return _iValue;
    }

    /**
     * String类型转Long 默认为0.
     *
     * @param sValue
     * @return
     */
    public static long parseLong(String sValue) {
        return parseLong(sValue, 0L);
    }

    /**
     * String类型转Long
     *
     * @param sValue
     * @param lDefault
     * @return
     */
    public static long parseLong(String sValue, long lDefault) {
        if (sValue == null) return lDefault;
        long _lValue = lDefault;
        try {
            _lValue = Long.parseLong(sValue);
        } catch (NumberFormatException e) {
            ;
        }
        return _lValue;
    }

    /**
     * String类型转float 默认为0.
     *
     * @param sValue
     * @return
     */
    public static float parseFloat(String sValue) {
        return parseFloat(sValue, 0);
    }

    /**
     * String类型转float
     *
     * @param sValue
     * @param fDefault
     * @return
     */
    public static float parseFloat(String sValue, float fDefault) {
        if (sValue == null) return fDefault;
        float _fValue = fDefault;
        try {
            _fValue = Float.parseFloat(sValue);
        } catch (NumberFormatException e) {
        }
        return _fValue;
    }

    /**
     * String类型转double 默认为0.
     *
     * @param sValue
     * @return
     */
    public static double parseDouble(String sValue) {
        return parseDouble(sValue, 0.0);
    }

    /**
     * String类型转double.
     *
     * @param sValue
     * @param dDefault
     * @return
     */
    public static double parseDouble(String sValue, double dDefault) {
        if (sValue == null) return dDefault;
        double _dValue = dDefault;
        try {
            _dValue = Double.parseDouble(sValue);
        } catch (NumberFormatException e) {
            ;
        }
        return _dValue;
    }

    /**
     * 把Object转换成String，如Object==null 返回""
     *
     * @param obj
     * @return
     */
    public static String parseString(Object obj) {
        return parseString(obj, ""); //$NON-NLS-1$
    }

    /**
     * 把Object转换成String，如Object==null 返回默认值
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String parseString(Object obj, String defaultValue) {
        if (obj == null)
            return defaultValue;
        else
            return String.valueOf(obj);
    }


    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;//单位米
    }

    /**
     * 比较两个日期之间相差天数
     *
     * @param smdate 起始日期
     * @param bdate  结束日期
     * @return 天数 integer类型
     */
    public static int daysBetween(String smdate, String bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        long time1 = 0;
        long time2 = 0;

        try {
            cal.setTime(sdf.parse(smdate));
            time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            time2 = cal.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
}
