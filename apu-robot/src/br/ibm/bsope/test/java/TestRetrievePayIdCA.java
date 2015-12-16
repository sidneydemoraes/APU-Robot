package br.ibm.bsope.test.java;

import br.ibm.bsope.main.java.model.FeePaymentItem;
import br.ibm.bsope.main.java.model.services.domain.ApuServices;
import br.ibm.bsope.main.java.model.services.domain.Browser;
import br.ibm.bsope.main.java.model.services.domain.ApuServices.PaymentType;

public class TestRetrievePayIdCA {

	public static void main(String[] args) {
		String supplierNumber = "1000050171";
		Double fee = 6392.36;
		String descStart = "IGF BP FOR ";
		String coa = "0117236ST";
		String customer = "HEALTH SHARED SERVICES BC";
		String province = "BC";
		String feeRate = "1.25%";
		String description = descStart + coa + " END USER " + customer;
		FeePaymentItem currentItem = null;
		try {
			currentItem = FeePaymentItem.getCAInstance(supplierNumber, fee, description, province, feeRate);
		} catch (ClassNotFoundException e) {
			System.out.println("Province does not exist in the application.");
			System.exit(0);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		currentItem.setNoteToApprover(description);
		Browser browser = Browser.getInstance();
		String[] approversNames = {"Lucia", "Accetta", "Aline", "Pimentel"};
		String login = "bpfee2@ca.ibm.com";
		String password = "can08can";
		
		ApuServices apu = ApuServices.getInstance(PaymentType.CANADA, browser, approversNames);
		
		apu.enterApu();
		apu.login(login, password);
		
		apu.setPayItem(currentItem);
		
		apu.startPaymentRequest();
		apu.searchForSupplier();
		apu.enterCommodityCode();
		apu.enterNotesToApprovers();
		apu.editAccountingData();
		apu.chooseApprovalPath();
		apu.changeManagerApprovers();
		String paymentId = apu.submitPayment();
		
		System.out.println(paymentId);
	}

}
