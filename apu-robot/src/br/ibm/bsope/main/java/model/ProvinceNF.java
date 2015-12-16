package br.ibm.bsope.main.java.model;

public class ProvinceNF implements IProvince {

	public String getCanadaTaxOption() {
		return "13% HST Payable (3rd party billing or Agency) New Foundland/Labrador-Old code use NL";
	}

	public String getApplicableTaxRegistration() {
		return "hst";
	}

	public String getProvinceAcronym() {
		return "NF";
	}

}
