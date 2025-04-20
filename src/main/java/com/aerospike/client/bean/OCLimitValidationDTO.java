package com.aerospike.client.bean;

public class OCLimitValidationDTO {

	
	private String mccCode;
	private String payerVpa;
	private String payeeVpa;
	private Double amount;
	private String txnId;
	private long recordKey;
	
	public String getMccCode() {
		return mccCode;
	}
	public void setMccCode(String mccCode) {
		this.mccCode = mccCode;
	}
	public String getPayerVpa() {
		return payerVpa;
	}
	public void setPayerVpa(String payerVpa) {
		this.payerVpa = payerVpa;
	}
	public String getPayeeVpa() {
		return payeeVpa;
	}
	public void setPayeeVpa(String payeeVpa) {
		this.payeeVpa = payeeVpa;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public long getRecordKey() {
		return recordKey;
	}
	public void setRecordKey(long recordKey) {
		this.recordKey = recordKey;
	}
	@Override
	public String toString() {
		return "OCLimitValidationDTO [mccCode=" + mccCode + ", payerVpa=" + payerVpa + ", payeeVpa=" + payeeVpa
				+ ", amount=" + amount + ", txnId=" + txnId + ", recordKey=" + recordKey + "]";
	}
	
}
