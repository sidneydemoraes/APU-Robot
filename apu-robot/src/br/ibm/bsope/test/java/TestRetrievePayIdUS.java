package br.ibm.bsope.test.java;

import br.ibm.bsope.main.java.model.FeePaymentItem;
import br.ibm.bsope.main.java.model.services.domain.ApuServices;
import br.ibm.bsope.main.java.model.services.domain.Browser;
import br.ibm.bsope.main.java.model.services.domain.ApuServices.PaymentType;

public class TestRetrievePayIdUS {


	public static void main(String[] args) {
		String supplierNumber = "1000307495";
		Double fee = 9170.13;
		String description = "IGF Distributor Fees";
		FeePaymentItem currentItem = null;
		currentItem = FeePaymentItem.getUSInstance(supplierNumber, fee, description);
		currentItem.setNoteToApprover(description);
		Browser browser = Browser.getInstance();
		String[] approversNames = {"Lucia", "Accetta", "Aline", "Pimentel"};
		String login = "boca3@us.ibm.com";
		String password = "bra900bra";
		
		ApuServices apu = ApuServices.getInstance(PaymentType.US, browser, approversNames);
		
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
