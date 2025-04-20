package com.aerospike.client.service;

import java.util.List;

import com.aerospike.client.bean.UserMasterBean;

public interface AerospikeService {

	
	public void saveToCache();
	public void updateData();
	public List<UserMasterBean> fetchDataByFilter();
	public UserMasterBean fetchDataByKey(String key);
	public void ocLimitInsert();
	public void fetchDataByKeyRange();
}
