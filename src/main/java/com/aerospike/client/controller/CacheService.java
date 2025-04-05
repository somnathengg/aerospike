package com.aerospike.client.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.bean.UserMaster;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.aerospike.client.task.IndexTask;

@Service
public class CacheService {

	@Autowired
	private AerospikeClient aerospikeClient;

	public void saveToCache() {
		System.out.println("Going to store now");
		WritePolicy writePolicy = new WritePolicy();
		writePolicy.expiration = 10 * 60; // 10 minutes

		IntStream.range(0, 5000).forEach(d -> {
			
			String uniqueKeyToFetch="somnath.shirsat_"+d+"~somnath.shirsat@gmail.com_"+d+"~9987307590_"+d;
			
			Key key = new Key("test", "user_master",uniqueKeyToFetch);
			Bin bin_primarykey = new Bin("id", d);
			Bin bin_username = new Bin("username", "somnath.shirsat_"+d);
			Bin bin_email = new Bin("email", "somnath.shirsat@gmail.com_"+d);
			Bin bin_mobile = new Bin("mobile", "9987307590_"+d);
			aerospikeClient.put(writePolicy, key, bin_primarykey, bin_username, bin_email, bin_mobile);
			System.out.println("Stored");
		});
		System.out.println("Stored");
	}
	
	
	
	
	public void updateData() {
		
		try {
		
		System.out.println("Going to update now");
		WritePolicy writePolicy = new WritePolicy();
		writePolicy.expiration = 10 * 60; // 10 minutes
		
		long currentTimeMillis = System.currentTimeMillis();
		
		String uniqueKeyToFetch="somnath.shirsat_2~somnath.shirsat@gmail.com_2~9987307590_2";
		Key key = new Key("test", "user_master",uniqueKeyToFetch);
		Bin bin_staus = new Bin("status", "A");
		Bin bin_createdBy = new Bin("created_by", "hitesh");
		Bin bin_createdDate = new Bin("created_date", currentTimeMillis);
		Bin bin_modfiedBy = new Bin("modified_by", "somnath");
		Bin bin_modifiedDate = new Bin("modified_date", currentTimeMillis);
		aerospikeClient.put(writePolicy, key, bin_staus, bin_createdBy,bin_createdDate,bin_modfiedBy, bin_modifiedDate);
		System.out.println("Updated");
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	
	
	
	public List<UserMaster> fetchDataByFilter()
	{
		List<UserMaster> userlist = new ArrayList<>();

		WritePolicy writePolicy = new WritePolicy();
		writePolicy.expiration = 10 * 60; // 5 minutes

		IndexTask task = aerospikeClient.createIndex(writePolicy, "test", "user_master", "index_idbin", "id",
				IndexType.NUMERIC);
		task.waitTillComplete();
		System.out.println("Index created successfully!");

		QueryPolicy queryPolicy = new QueryPolicy();
		Statement stmt = new Statement();
		stmt.setNamespace("test");
		stmt.setSetName("user_master");
		stmt.setFilter(Filter.range("id", 4000, Integer.MAX_VALUE));
		RecordSet rs = aerospikeClient.query(queryPolicy, stmt);
		UserMaster usrMst = null;
		while (rs.next()) {
			usrMst = new UserMaster();
			Record record = rs.getRecord();
			usrMst.setId(Integer.parseInt(record.bins.get("id").toString()));
			usrMst.setUsername(record.bins.get("username").toString());
			usrMst.setEmail(record.bins.get("email").toString());
			usrMst.setMobile(record.bins.get("mobile").toString());
			userlist.add(usrMst);
		}
		rs.close();

		return userlist;
	}
	
	public UserMaster fetchDataByKey(String key)
	{
		Key fetchKey = new Key("test", "user_master", key);
		WritePolicy writePolicy = new WritePolicy();
		writePolicy.expiration = 10 * 60; // 10 minutes
		Record userrecord = aerospikeClient.get(writePolicy, fetchKey);
		UserMaster usrMst = new UserMaster();
		usrMst.setId(Integer.parseInt(userrecord.bins.get("id").toString()));
		usrMst.setUsername(userrecord.bins.get("username").toString());
		usrMst.setEmail(userrecord.bins.get("email").toString());
		usrMst.setMobile(userrecord.bins.get("mobile").toString());
		
		
		long createdtimestamp = userrecord.getLong("created_date");
		Date createdconverteddate = new Date(createdtimestamp);
		
		long modifedtimestamp = userrecord.getLong("modified_date");
		Date modifiedconverteddata = new Date(modifedtimestamp);
		
		usrMst.setCreatedBy(userrecord.bins.get("created_by").toString());
		usrMst.setCreatedDate(createdconverteddate);
		usrMst.setModifiedBy(userrecord.bins.get("modified_by").toString());
		usrMst.setModifiedDate(modifiedconverteddata);
		
		System.out.println(usrMst.toString());
		
		return usrMst;
	}
	
}
