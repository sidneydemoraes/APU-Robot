package br.ibm.bsope.main.java.model;

public class ProvinceBC implements IProvince {

	public String getCanadaTaxOption() {
		return "12% HST Payable (3rd party billing or Agency) British Columbia";
	}

	public String getApplicableTaxRegistration() {
		return "hst";
	}

	public String getProvinceAcronym() {
		return "BC";
	}

}
