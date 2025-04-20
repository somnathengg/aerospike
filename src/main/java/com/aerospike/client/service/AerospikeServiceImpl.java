package com.aerospike.client.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Operation;
import com.aerospike.client.Record;
import com.aerospike.client.Value;
import com.aerospike.client.bean.OCLimitValidationDTO;
import com.aerospike.client.bean.UserMasterBean;
import com.aerospike.client.cdt.MapOperation;
import com.aerospike.client.cdt.MapOrder;
import com.aerospike.client.cdt.MapPolicy;
import com.aerospike.client.cdt.MapReturnType;
import com.aerospike.client.cdt.MapWriteMode;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.aerospike.client.task.IndexTask;

@Service
public class AerospikeServiceImpl implements AerospikeService {

	@Autowired
	private AerospikeClient aerospikeClient;

	@Override
	public void saveToCache() {
		System.out.println("Going to store now");
		WritePolicy writePolicy = new WritePolicy();
		writePolicy.expiration = 10 * 60; // 10 minutes

		IntStream.range(0, 5000).forEach(d -> {

			String uniqueKeyToFetch = "somnath.shirsat_" + d + "~somnath.shirsat@gmail.com_" + d + "~9987307590_" + d;

			Key key = new Key("test", "user_master", uniqueKeyToFetch);
			Bin bin_primarykey = new Bin("id", d);
			Bin bin_username = new Bin("username", "somnath.shirsat_" + d);
			Bin bin_email = new Bin("email", "somnath.shirsat@gmail.com_" + d);
			Bin bin_mobile = new Bin("mobile", "9987307590_" + d);
			aerospikeClient.put(writePolicy, key, bin_primarykey, bin_username, bin_email, bin_mobile);
			System.out.println("Stored");
		});
		System.out.println("Stored");
	}

	@Override
	public void updateData() {

		try {

			LocalDateTime cd = LocalDateTime.now();

			String custom_date = cd.getDayOfMonth() + "-" + cd.getMonthValue() + "-" + cd.getYear() + " " + cd.getHour()
					+ ":" + cd.getMinute() + ":" + cd.getSecond();

			System.out.println("Going to update now");
			WritePolicy writePolicy = new WritePolicy();
			writePolicy.expiration = 10 * 60; // 10 minutes
			long currentTimeMillis = System.currentTimeMillis();
			String uniqueKeyToFetch = "somnath.shirsat_1~somnath.shirsat@gmail.com_1~9987307590_1";
			Key key = new Key("test", "user_master", uniqueKeyToFetch);
			Bin bin_staus = new Bin("status", "A");
			Bin bin_createdBy = new Bin("created_by", "hitesh");
			Bin bin_createdDate = new Bin("created_date", new Timestamp(new Date().getTime()));
			Bin bin_modfiedBy = new Bin("modified_by", "somnath");
			Bin bin_modifiedDate = new Bin("modified_date", custom_date);

			System.out.println("key to update data >>" + uniqueKeyToFetch);

			aerospikeClient.put(writePolicy, key, bin_staus, bin_createdBy, bin_createdDate, bin_modfiedBy,
					bin_modifiedDate);
			System.out.println("Updated");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public List<UserMasterBean> fetchDataByFilter() {
		List<UserMasterBean> userlist = new ArrayList<>();

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
		UserMasterBean usrMst = null;
		while (rs.next()) {
			usrMst = new UserMasterBean();
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

	@Override
	public UserMasterBean fetchDataByKey(String key) {
		Key fetchKey = new Key("test", "user_master", key);
		WritePolicy writePolicy = new WritePolicy();
		writePolicy.expiration = 10 * 60; // 10 minutes
		Record userrecord = aerospikeClient.get(writePolicy, fetchKey);
		UserMasterBean usrMst = new UserMasterBean();
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

	@Override
	public void ocLimitInsert() {

		try {
			WritePolicy writePolicy = new WritePolicy();
			writePolicy.expiration = 10 * 60; 
			MapPolicy mapPolicy = new MapPolicy(MapOrder.KEY_ORDERED, MapWriteMode.UPDATE);
			String uniqueKeyToFetch = "12380152777991";

			Random random = new Random();
			int x = random.nextInt(900) + 100;

			Key key = new Key("test", "AERO_VTUTOCTRNVAL", uniqueKeyToFetch);
			String txnId = String.valueOf("SBIAERO6357434" + x);
			String binName = "OC_VERSION";
			
			long halfday = 1745124448388l;//getCutoffTimeHalfDay(new Date().getTime());
			
			long start=0l;
			long end=1745171016330l;
			
			long txnTimeStamp = System.currentTimeMillis() + random.nextInt(1000);
			Map<Value, Value> entries = new HashMap();
			entries.put(Value.get(txnTimeStamp), Value.get(generateList(txnId)));

			Operation removeOperation = MapOperation.removeByKeyRange("OC_VERSION", Value.get(start), Value.get(end), MapReturnType.NONE);
			Operation putOperation = MapOperation.putItems(mapPolicy, binName,entries);

			Record data = aerospikeClient.operate(writePolicy, key,removeOperation,putOperation);
			System.out.println("inserted data " + data.toString());
			
			
			

		} catch (Exception e) {
			System.out.println("Exception in inserting data in VTUTOCTRNVAL >>" + e.getMessage());
		}
	}

	@Override
	public void fetchDataByKeyRange() {
		try {
			System.out.println("inside fetchDataByKeyRange");
			WritePolicy writePolicy = new WritePolicy();
			writePolicy.expiration = 10 * 60; // 10 minutes
			writePolicy.sendKey = true;
			String uniqueKeyToFetch = "12380152777991";
			Key key = new Key("test", "AERO_VTUTOCTRNVAL", uniqueKeyToFetch);
			String binName = "OC_VERSION";
			long end = System.currentTimeMillis();

			Record record = aerospikeClient.operate(null, key, MapOperation.getByKeyRange(binName,
					Value.get((Long) null), Value.get(end), MapReturnType.KEY_VALUE));

			System.out.println("data size " + record.bins.size());

			List<Entry<Long, List<String>>> dataList = null;
			dataList = (List<Entry<Long, List<String>>>) record.bins.get(binName);

			List<OCLimitValidationDTO> pojolist = new ArrayList<>();

			dataList.forEach(l -> {
				OCLimitValidationDTO dataset = new OCLimitValidationDTO();
				dataset.setMccCode(l.getValue().get(0));
				dataset.setPayerVpa(l.getValue().get(1));
				dataset.setPayeeVpa(l.getValue().get(2));
				dataset.setAmount(Double.parseDouble(l.getValue().get(3)));
				dataset.setTxnId(l.getValue().get(4));
				dataset.setRecordKey(l.getKey());
				pojolist.add(dataset);
			});
			
			
			System.out.println("***************data list size****************"+dataList.size());
			
			pojolist.forEach(d->{
				System.out.println(d.toString());
			});
			

		} catch (Exception e) {
			System.out.println("Exception in fetchDataByKeyRange from VTUTOCTRNVAL >>" + e.getMessage());
		}

	}

	private long getCutoffTimeOneDay(long startTime) {
		return startTime - 864000000;
	}

	private long getCutoffTimeHalfDay(long startTime) {
		return startTime - 432000000;
	}

	private List<String> generateList(String txnId) {
		List<String> keyvallist = new ArrayList();
		Random random = new Random();
		int x = random.nextInt(900) + 100;
		keyvallist.add("0000");
		keyvallist.add("somnath8383@ybl");
		keyvallist.add("8482891131@icici");
		keyvallist.add(String.valueOf((double) x));
		keyvallist.add(txnId);
		return keyvallist;
	}

}
