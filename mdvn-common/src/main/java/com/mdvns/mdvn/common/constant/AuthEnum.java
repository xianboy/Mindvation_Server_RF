package com.mdvns.mdvn.common.enums;

public enum AuthEnum {
	BOSS(1), Leader(2), RMEMBER(3),SMEMBER(4), TMEMBER(5);

Integer code;
	
private AuthEnum(Integer code) {
	this.code = code;
}

public Integer getCode() {
	return code;
}

}
