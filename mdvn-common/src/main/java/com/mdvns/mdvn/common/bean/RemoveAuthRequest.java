package com.mdvns.mdvn.common.bean;

import com.mdvns.mdvn.common.bean.model.StaffAuthInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveAuthRequest {

    /*操作人(删除权限的人)Id*/

	private String projSerialNo;
	private Long assignerId;
	private List<Long> removeList;
	private String hostSerialNo;
	private Integer authCode;

}
