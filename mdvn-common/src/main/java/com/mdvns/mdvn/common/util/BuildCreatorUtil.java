package com.mdvns.mdvn.common.util;

import com.mdvns.mdvn.common.bean.RetrieveTerseInfoRequest;
import com.mdvns.mdvn.common.bean.model.TerseInfo;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class BuildCreatorUtil {


    private static TerseInfo buildCreator(Long creatorId) {
        List<Long> ids = new ArrayList<>();
        ids.add(creatorId);
        //构建request
        RetrieveTerseInfoRequest baseInfoRequest = new RetrieveTerseInfoRequest(creatorId, ids);

        RestTemplate restTemplate = new RestTemplate();




        return null;
    }
}
