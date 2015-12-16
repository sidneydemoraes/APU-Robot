package br.ibm.bsope.main.java.model;

public class ProvinceAB implements IProvince {

	public String getCanadaTaxOption() {
		return "5% GST Payable/0% PST Payable (3rd party billingAgency) Alberta";
	}

	public String getApplicableTaxRegistration() {
		return "gst";
	}

	public String getProvinceAcronym() {
		return "AB";
	}

}
