package com.mdvns.mdvn.common.bean;

import com.mdvns.mdvn.common.bean.model.StaffAuthInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RemoveAuthRequest {

    /*操作人(删除权限的人)Id*/
//    private String operatorId;
//    private StaffAuthInfo staffAuthInfo;

	private Long projId;
	private Long assignerId;
	private List<Long> removeList;
	private Long hostId;
	private Integer authCode;

}
