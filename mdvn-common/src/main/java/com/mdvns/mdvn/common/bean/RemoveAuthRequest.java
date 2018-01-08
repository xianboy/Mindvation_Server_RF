package com.mdvns.mdvn.common.bean;

import com.mdvns.mdvn.common.bean.model.StaffAuthInfo;

public class RemoveAuthRequest {

    /*操作人(删除权限的人)Id*/
    private String operatorId;

    private StaffAuthInfo staffAuthInfo;

    
    public RemoveAuthRequest() {
    }


	public String getOperatorId() {
		return operatorId;
	}


	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}


	public StaffAuthInfo getStaffAuthInfo() {
		return staffAuthInfo;
	}


	public void setStaffAuthInfo(StaffAuthInfo staffAuthInfo) {
		this.staffAuthInfo = staffAuthInfo;
	}

    
}
