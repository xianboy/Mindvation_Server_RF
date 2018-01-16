package com.mdvns.mdvn.staff.domain;

public class RtrvStaffAuthInfoRequest {
	
	private String projId;
	
	private String staffId;
	
	private String hierarchyId;

	
	public RtrvStaffAuthInfoRequest() {
		super();
	}

	public RtrvStaffAuthInfoRequest(String projId, String staffId, String hierarchyId) {
		super();
		this.projId = projId;
		this.staffId = staffId;
		this.hierarchyId = hierarchyId;
	}

	public String getProjId() {
		return projId;
	}

	public void setProjId(String projId) {
		this.projId = projId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getHierarchyId() {
		return hierarchyId;
	}

	public void setHierarchyId(String hierarchyId) {
		this.hierarchyId = hierarchyId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hierarchyId == null) ? 0 : hierarchyId.hashCode());
		result = prime * result + ((projId == null) ? 0 : projId.hashCode());
		result = prime * result + ((staffId == null) ? 0 : staffId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RtrvStaffAuthInfoRequest other = (RtrvStaffAuthInfoRequest) obj;
		if (hierarchyId == null) {
			if (other.hierarchyId != null)
				return false;
		} else if (!hierarchyId.equals(other.hierarchyId))
			return false;
		if (projId == null) {
			if (other.projId != null)
				return false;
		} else if (!projId.equals(other.projId))
			return false;
		if (staffId == null) {
			if (other.staffId != null)
				return false;
		} else if (!staffId.equals(other.staffId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RtrvStaffAuthInfoRequest [projId=" + projId + ", staffId=" + staffId + ", hierarchyId=" + hierarchyId
				+ "]";
	}
	
}
