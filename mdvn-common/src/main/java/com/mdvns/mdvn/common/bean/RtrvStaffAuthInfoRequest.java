package com.mdvns.mdvn.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RtrvStaffAuthInfoRequest {
	
	private String projId;
	
	private String staffId;
	
	private String hierarchyId;


}
