package br.ibm.bsope.main.java.controller;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import br.ibm.bsope.main.java.model.FeePaymentItem;
import br.ibm.bsope.main.java.model.exceptions.EmptyFileException;
import br.ibm.bsope.main.java.model.exceptions.InvalidSpreadsheetFormatException;
import br.ibm.bsope.main.java.model.services.domain.ApuServices;
import br.ibm.bsope.main.java.model.services.domain.Browser;
import br.ibm.bsope.main.java.model.services.domain.ExcelFile;
import br.ibm.bsope.main.java.model.services.domain.ApuServices.PaymentType;
import br.ibm.bsope.main.java.model.services.infrastructure.FeePaymentItemRepository;

public class APUFeePayment {
	
	public static void paymentUS(File excelFile, String login, String password, String[] approversNames) throws InvalidFormatException, IOException, EmptyFileException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvalidSpreadsheetFormatException{
		apuPayment(PaymentType.US, excelFile, login, password, approversNames);
	}
	
	public static void paymentCA(File excelFile, String login, String password, String[] approversNames) throws InvalidFormatException, IOException, EmptyFileException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvalidSpreadsheetFormatException{
		apuPayment(PaymentType.CANADA, excelFile, login, password, approversNames);
	}
	
	
	private static void apuPayment(PaymentType payType, File excelFile, String login, String password,
			                       String[] approversNames) throws InvalidFormatException, IOException, EmptyFileException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvalidSpreadsheetFormatException{
		ExcelFile excel = ExcelFile.openExistingExcelFile(excelFile.getAbsolutePath());
		FeePaymentItemRepository repo = FeePaymentItemRepository.getRepoInstance(excel, payType);
		FeePaymentItem currentItem = repo.getCurrentItem();
		Browser browser = Browser.getInstance();
		ApuServices apu = ApuServices.getInstance(payType, browser, approversNames);
		apu.enterApu();
		apu.login(login, password);
		
		
		while(currentItem != null){

			apu.setPayItem(currentItem);
			apu.startPaymentRequest();
			apu.searchForSupplier();
			apu.enterCommodityCode();
			apu.enterNotesToApprovers();
			apu.editAccountingData();
			apu.chooseApprovalPath();
			apu.changeManagerApprovers();
			apu.submitPayment();
			repo.concludeCurrentItem();
			currentItem = repo.getCurrentItem();
			
		}
		
		excel.saveFile();
				
	}

}
