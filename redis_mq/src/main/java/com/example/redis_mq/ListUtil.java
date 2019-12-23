package com.example.redis_mq;

import com.alibaba.fastjson.JSON;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

/**
 * Created by Administrator on 2016/5/10.
 */
public class ListUtil {

    public static <T> ArrayList<T> empty() {
        return new ArrayList<>();
    }

    public static <K, V> HashMap<K, V> emptyHashMap() {
        return new HashMap<>();
    }

    /**
     * 根据数组得到arrayList列表
     *
     * @param array
     * @return
     */
    @SafeVarargs
    public static <T> ArrayList<T> asList(T... array) {
        ArrayList<T> data = empty();
        if (array != null) {
            for (T t : array) {
                data.add(t);
            }
        }
        return data;
    }

    public static <T> List<T> subList(List<T> list, int i, int j) {
        if (list == null) {
            return null;
        }
        ArrayList<T> list2 = empty();
        for (int j2 = 0; j2 < list.size(); j2++) {
            if (j2 >= i && j2 < j) {
                list2.add(list.get(j2));
            }
        }
        return list2;
    }

    @SafeVarargs
    public static <T> int addToList(ArrayList<T> list, T... subitem) {
        int count = 0;
        if (list != null && subitem != null) {
            for (T t : subitem) {
                int index = list.indexOf(t);
                if (index > -1) {
                    list.remove(index);
                    list.add(index, t);
                } else {
                    list.add(t);
                    count++;
                }
            }
        }
        return count;
    }

    public static <T> int addToList(List<T> list, List<? extends T> items) {
        final int[] count = {0};
        if (!isEmpty(list) && items != null) {
            list.forEach(t -> {
                int index = list.indexOf(t);
                if (index > -1) {
                    list.remove(index);
                    list.add(index, t);
                } else {
                    list.add(t);
                    count[0]++;
                }
            });
        }
        return count[0];
    }

    /**
     * 添加指定数量的item到List
     *
     * @param list
     * @param subitem
     * @param addCount
     * @return
     */
    @SafeVarargs
    public static <T> int addToList(List<T> list, int addCount, T... subitem) {
        int count = 0;
        if (list != null && subitem != null) {
            for (T t : subitem) {
                int index = list.indexOf(t);
                if (index > -1) {
                    list.remove(index);
                    list.add(index, t);
                } else {
                    list.add(t);
                    count++;
                    if (count >= addCount) {
                        break;
                    }
                }
            }
        }
        return count;
    }

    @SafeVarargs
    public static <T> int removeFromList(List<T> list, T... subitem) {
        int count = 0;
        if (list != null && subitem != null) {
            for (T t : subitem) {
                int index = list.indexOf(t);
                if (index > -1) {
                    list.remove(index);
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean isEmpty(Map<String, Object> map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isEmpty(List<?> list) {
        return (list == null || list.isEmpty());
    }

    public static boolean isListHasIndex(List<?> list, int index) {
        return list != null && list.size() > index && index >= 0;
    }

    public static int size(List<?> list) {
        return isEmpty(list) ? 0 : list.size();
    }

    public static <T> int size(T[] items) {
        return items != null ? items.length : 0;
    }



    /**
     * 解析为list
     */
    public static <T> List<T> parseArray(String data, Class<T> t) {
        List<T> result = null;
        if (data == null || data.equals("[]")) {
            result = empty();
        } else {
            try {
                result = JSON.parseArray(data, t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 解析为对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(String data, Class<T> t) {
        T result = null;
        if (data == null || data.equals("{}") || data.equals("null")) {
        } else {
            // result = new Gson().fromJson(data,t);
            if (t == String.class) {
                return (T) data;
            }
            try {
                result = JSON.parseObject(data, t);
            } catch (com.alibaba.fastjson.JSONException e) {

                e.printStackTrace();
                ListUtil.e(e.toString());
            }
        }
        return result;
    }

    public static <T> void copyList(List<T> list, int minSize) {
        if (isEmpty(list)) {
            return;
        }
        while (list.size() < minSize) {
            list.addAll(list);
        }
    }





    /**
     * @param msg 打印信息
     */
    public static void e(String msg, Object packageName) {
        if (packageName == null || packageName.equals(""))
            packageName = "shall we talk==sakura";

        if (packageName instanceof String) {
            System.err.printf("---------%s-----%s", packageName, msg);

        } else
            System.err.printf("----------%s----%s", packageName.getClass().getName(), msg);

    }

    public static void e(Object msg, Object packageName) {
        if (packageName == null || packageName.equals(""))
            packageName = "shall we talk==sakura";

        if (packageName instanceof String) {
            System.err.printf("---------%s-----%s", packageName, msg);

        } else
            System.err.printf("----------%s----%s", packageName.getClass().getName(), msg);

    }

    public static void e(Object packageName) {

        e("shall we talk--sakura---", packageName);
    }

    /*
         * 将时间转换为时间戳
         */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        String a = s;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static String TimeSampToString(Timestamp timestamp) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//       Timestamp nowdate = new Timestamp(System.currentTimeMillis());
        String datestr = sdf.format(timestamp);
        System.out.println(datestr);
        return datestr;
    }

    /**
     * ListUtil.removeList(userList,usera ->  usera.getUser_id().equals(""));
     *
     * @param list
     * @param filter java 8 的谓语判定
     * @param <E>    list 的对象
     * @return
     */
    public static <E> boolean removeList(List<E> list, Predicate<? super E> filter) {

        return list.removeIf(filter);
    }

    public static Boolean mIsEmpty(String s) {
        Optional<String> optional = Optional.ofNullable(s);   //optional.isPresent()  与 ! =null 差不多
        return !optional.isPresent() || s == null || s.length() == 0;

    }

    public static Map<String, Object> beanToMap(Object obj) throws IntrospectionException {
        Map<String, Object> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            // 过滤class属性
            if (!key.equals("class")) {
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = "";
                try {
                    value = getter.invoke(obj);
                } catch (Exception e) {

                }finally{
                    value = value==null?"":value;
                    map.put(key, value.toString());
                }

            }
        }
//        map.put("beanFlag", BeanFlag.Insert.toString());
        return map;
    }



//public  static JSONObject getOrganzation(){}
}
