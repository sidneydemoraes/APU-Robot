package br.ibm.bsope.main.java.model;

public class ProvinceSK implements IProvince {

	public String getCanadaTaxOption() {
		return "5% GST Payable/5% PST Payable (3rd party billingAgency) Saskatchewan";
	}

	public String getApplicableTaxRegistration() {
		return "gst";
	}

	public String getProvinceAcronym() {
		return "SK";
	}

}
