package com.mdvns.mdvn.staff.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RemoveAuthRequest {

    /*操作人(删除权限的人)Id*/
//    private String operatorId;
//    private StaffAuthInfo staffAuthInfo;

	private String projSerialNo;
	private Long assignerId;
	private List<Long> removeList;
	private String hostSerialNo;
	private Integer authCode;

}
