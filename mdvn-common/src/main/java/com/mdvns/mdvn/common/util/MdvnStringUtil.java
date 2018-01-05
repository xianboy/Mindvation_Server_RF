package com.mdvns.mdvn.common.util;

import java.util.*;


public class MdvnStringUtil {

    public static String join(Collection var0, String var1) {
        StringBuffer var2 = new StringBuffer();

        for (Iterator var3 = var0.iterator(); var3.hasNext(); var2.append((String) var3.next())) {
            if (var2.length() != 0) {
                var2.append(var1);
            }
        }

        return var2.toString();
    }


    public static String joinLong(Collection var0, String var1) {
        StringBuffer var2 = new StringBuffer();

        for (Iterator var3 = var0.iterator(); var3.hasNext(); var2.append((Long) var3.next())) {
            if (var2.length() != 0) {
                var2.append(var1);
            }
        }

        return var2.toString();
    }

    /**
     * 将逗号隔开的字符串转化成list
     * @param var1
     * @return
     */
    public static List stringToList(String var1){
        String[] var1ArrayIds = var1.split(",");
        List<String> var1List = Arrays.asList(var1ArrayIds);
        List<String> list = new ArrayList(var1List);
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            longList.add(Long.valueOf(list.get(i)));
        }
        return longList;
    }


}
