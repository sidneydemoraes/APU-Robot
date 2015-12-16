package br.ibm.bsope.main.java.model;

public class ProvincePE implements IProvince {

	public String getCanadaTaxOption() {
		return "5% GST Payable/10% PST Payable (3rd party billingAgency) Prince Edward Island";
	}

	public String getApplicableTaxRegistration() {
		return "gst";
	}

	public String getProvinceAcronym() {
		return "PE";
	}

}
