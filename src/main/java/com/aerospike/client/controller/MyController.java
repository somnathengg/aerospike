package com.aerospike.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aerospike.client.bean.UserMaster;

@RestController
@RequestMapping("test")
public class MyController {

	@Autowired
	private CacheService cacheService;

	@GetMapping(value = "/save-cache", produces = "application/json")
	public void testme() {
		cacheService.saveToCache();
	}

	@GetMapping(value = "/fetch-data", produces = "application/json")
	public List<UserMaster> fetchData() {
		return cacheService.fetchDataByFilter();
	}

	@GetMapping(value = "/fetch-data-by-key/{record-key}", produces = "application/json")
	public UserMaster fetchDataByKey(@PathVariable("record-key") String recordKey) {
		return cacheService.fetchDataByKey(recordKey);
	}
	
	
	@GetMapping(value = "/update-data")
	public void updateBinCall() {
		cacheService.updateData();
	}

}
