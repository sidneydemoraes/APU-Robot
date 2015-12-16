package br.ibm.bsope.main.java.model;

public class ProvinceQC implements IProvince {

	public String getCanadaTaxOption() {
		return "5% GST Payable/9.5% PST Payable (3rd party billingAgency) Quebec";
	}

	public String getApplicableTaxRegistration() {
		return "gst";
	}

	public String getProvinceAcronym() {
		return "QC";
	}

}
