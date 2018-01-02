package com.mdvns.mdvn.common.util;

import java.util.Collection;
import java.util.Iterator;


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

}
