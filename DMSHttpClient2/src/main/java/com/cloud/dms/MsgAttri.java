package com.cloud.dms;

public class MsgAttri {
	private String queueId;
	private String projectId;
	private String endpointUrl;
	private String serviceName;
	private String region;
	private String aKey;
	private String sKey;
	private String groupId;
	private String msgLimit;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getMsgLimit() {
		return msgLimit;
	}
	public void setMsgLimit(String msgLimit) {
		this.msgLimit = msgLimit;
	}
	public String getQueueId() {
		return queueId;
	}
	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getEndpointUrl() {
		return endpointUrl;
	}
	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getaKey() {
		return aKey;
	}
	public void setaKey(String aKey) {
		this.aKey = aKey;
	}
	public String getsKey() {
		return sKey;
	}
	public void setsKey(String sKey) {
		this.sKey = sKey;
	}
	
	
}
