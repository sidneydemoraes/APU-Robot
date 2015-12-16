package br.ibm.bsope.main.java.model;

public class FeePaymentItem {
	
	private String supplierNumber;
	private Double fee;
	private String feeRate;
	private String description;
	private String noteToApprover;
	private IProvince province;
	private String zipcode;
	private String paymentId;
	
	
	public enum PayStatus { PENDING, CONCLUDED }
	

	/**
	 * Factory method to return a US FeePayment instance.
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public static FeePaymentItem getUSInstance(String supplierNumber, Double fee, String description){
		FeePaymentItem item = null;
		try {
			return new FeePaymentItem(supplierNumber, fee, description, null, null);
		} catch (Exception e) {
			// No handle is necessary because the methods that throw exceptions are related to Canada instances.
		} 
		return item;
	}
	
	/**
	 * Factory method to return a CA FeePayment instance.
	 * @param supplierName
	 * @param supplierNumber
	 * @param fee
	 * @param description
	 * @param province
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public static FeePaymentItem getCAInstance(String supplierNumber, Double fee, String description, String province, String feeRate) throws IllegalAccessException, InstantiationException, ClassNotFoundException{
		return new FeePaymentItem(supplierNumber, fee, description, province, feeRate);
	}
	
	
	
	
	private FeePaymentItem(String supplierNumber, Double fee, String description, String province, String feeRate) throws IllegalAccessException, InstantiationException, ClassNotFoundException{
		this.supplierNumber = supplierNumber;
		this.fee = fee;
		this.description = description;
		this.province = (province == null) ? null : (IProvince) Class.forName("br.ibm.bsope.main.java.model.Province" + province).newInstance();
		this.feeRate = feeRate;
	}	
	
	


	public String getSupplierNumber() {
		return supplierNumber;
	}


	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}


	public Double getFee() {
		return fee;
	}


	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(String feeRate) {
		this.feeRate = feeRate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNoteToApprover() {
		return noteToApprover;
	}

	public void setNoteToApprover(String noteToApprover) {
		this.noteToApprover = noteToApprover;
	}

	public IProvince getProvince() {
		return province;
	}

	public void setProvince(IProvince province) {
		this.province = province;
	}
	
	public String getProvinceAcronym(){
		return province.getProvinceAcronym();
	}
	
	public String getCanadaTaxOption(){
		return province.getCanadaTaxOption();
	}
	
	public String getApplicableTaxRegistration(){
		return province.getApplicableTaxRegistration();
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public boolean isOver5k(){
		return this.fee > 5000;
	}
	
}
