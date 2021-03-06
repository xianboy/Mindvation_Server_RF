package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.MemberRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ListUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ListUtil.class);

    public static List getDistinctList(List list) {
        HashSet hs = new HashSet(list);
        list.clear();
        list.addAll(hs);
        return list;
    }

    public static List<Long> getDistinctAddList(List<MemberRequest> list) {

        if(!list.isEmpty()){
            List<Long> addList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getAddList().isEmpty()) {
                    addList.addAll(list.get(i).getAddList());
                }
            }
            return getDistinctList(addList);
        }

        return new ArrayList<>();

    }

    public static List<Long> getDistinctRemoveList(List<MemberRequest> list) {
        if(!list.isEmpty()){
            List<Long> removeList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getRemoveList().isEmpty()) {
                    removeList.addAll(list.get(i).getRemoveList());
                }
            }
            return getDistinctList(removeList);
        }
        return new ArrayList<>();
    }
}
