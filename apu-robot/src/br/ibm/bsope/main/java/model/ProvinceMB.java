package br.ibm.bsope.main.java.model;

public class ProvinceMB implements IProvince {

	public String getCanadaTaxOption() {
		return "5% GST Payable/7% PST Payable (3rd party billingAgency) Manitoba";
	}

	public String getApplicableTaxRegistration() {
		return "gst";
	}

	public String getProvinceAcronym() {
		return "MB";
	}

}
