package br.ibm.bsope.main.java.model;

public class ProvinceNB implements IProvince {

	public String getCanadaTaxOption() {
		return "13% HST Payable (3rd party billing or Agency) New Brunswick";
	}

	public String getApplicableTaxRegistration() {
		return "hst";
	}

	public String getProvinceAcronym() {
		return "NB";
	}

}
