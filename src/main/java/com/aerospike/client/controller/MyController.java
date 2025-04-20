package com.aerospike.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aerospike.client.bean.UserMasterBean;
import com.aerospike.client.service.AerospikeService;

@RestController
@RequestMapping("test")
public class MyController {

	@Autowired
	private AerospikeService aerospikeService;

	@GetMapping(value = "/save-data", produces = "application/json")
	public void testme() {
		aerospikeService.saveToCache();
	}

	@GetMapping(value = "/fetch-data", produces = "application/json")
	public List<UserMasterBean> fetchData() {
		return aerospikeService.fetchDataByFilter();
	}

	@GetMapping(value = "/fetch-data-by-key/{record-key}", produces = "application/json")
	public UserMasterBean fetchDataByKey(@PathVariable("record-key") String recordKey) {
		return aerospikeService.fetchDataByKey(recordKey);
	}

	@GetMapping(value = "/update-data")
	public void updateBinCall() {
		aerospikeService.updateData();
	}
	
	@GetMapping(value = "/oc-limit-insert")
	public void ocLimitInsert() {
		aerospikeService.ocLimitInsert();
	}
	
	@GetMapping(value = "/fetch-data-by-key")
	public void fetchDataByKey() {
		aerospikeService.fetchDataByKeyRange();
	}

}
