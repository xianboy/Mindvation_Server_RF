package com.mdvns.mdvn.staff.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class RtrvStaffAuthInfoRequest {
	
	private String projSerialNo;
	
	private Long staffId;
	
	private String hostSerialNo;


}
