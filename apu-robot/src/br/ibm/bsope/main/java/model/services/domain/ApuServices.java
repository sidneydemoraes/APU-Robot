package br.ibm.bsope.main.java.model.services.domain;

import java.util.Set;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import br.ibm.bsope.main.java.model.FeePaymentItem;

public class ApuServices {

	private Browser browser;
	private final String APU_URL = "https://w3-01.sso.ibm.com/procurement/pesapu/americas/protect/pesapu.wss?id=CreatePR";
	private PaymentType payType;
	private final int DUEDATE_MODIFIER = 0;
	private String firstApproverFirstName;
	private String firstApproverLastName;
	private String secondApproverFirstName;
	private String secondApproverLastName;
	private FeePaymentItem payItem;
	
	
	public enum PaymentType {US, CANADA};

	/**
	 * Factory method to return a ApuServices instance.
	 * @param payType
	 * @param browser
	 * @param approversNames is a array of Strings containing in this order: first name of first approver,
	 *        last name of first approver, first name of second approver, last name of second approver
	 * @return
	 */
	public static ApuServices getInstance(PaymentType payType, Browser browser, String[] approversNames){
		ApuServices apu = new ApuServices(payType, browser);
		apu.setFirstApproverFirstName(approversNames[0]);
		apu.setFirstApproverLastName(approversNames[1]);
		apu.setSecondApproverFirstName(approversNames[2]);
		apu.setSecondApproverLastName(approversNames[3]);
		return apu;
	}



	private ApuServices(PaymentType payType, Browser browser){
		this.payType = payType;
		this.browser = browser;
	}


	public Browser getBrowser() {
		return browser;
	}


	public void setBrowser(Browser browser) {
		this.browser = browser;
	}


	public PaymentType getPayType() {
		return payType;
	}


	public void setPayType(PaymentType payType) {
		this.payType = payType;
	}


	public String getFirstApproverFirstName() {
		return firstApproverFirstName;
	}


	public void setFirstApproverFirstName(String firstApproverFirstName) {
		this.firstApproverFirstName = firstApproverFirstName;
	}


	public String getFirstApproverLastName() {
		return firstApproverLastName;
	}


	public void setFirstApproverLastName(String firstApproverLastName) {
		this.firstApproverLastName = firstApproverLastName;
	}


	public String getSecondApproverFirstName() {
		return secondApproverFirstName;
	}


	public void setSecondApproverFirstName(String secondApproverFirstName) {
		this.secondApproverFirstName = secondApproverFirstName;
	}


	public String getSecondApproverLastName() {
		return secondApproverLastName;
	}


	public void setSecondApproverLastName(String secondApproverLastName) {
		this.secondApproverLastName = secondApproverLastName;
	}



	public FeePaymentItem getPayItem() {
		return payItem;
	}



	public void setPayItem(FeePaymentItem payItem) {
		this.payItem = payItem;
	}



	public void enterApu(){
		browser.get(APU_URL);		
	}


	public void login(String intranetId, String intranetPw){
		browser.findElement(By.id("username")).sendKeys(intranetId);		
		browser.findElement(By.id("password")).sendKeys(intranetPw);		
		browser.findElement(By.name("submitButton")).click();

		browser.waitUntilPageIsLoaded();
		browser.findElement(By.linkText("Create payment request")).click();
	}


	public void startPaymentRequest(){
		browser.findElement(By.name("continueToNextStep")).click();
		browser.findElement(By.id("confirmRestrictions")).click();
		browser.findElement(By.xpath("(//input[@id='isInvRefDoc'])[2]")).click();
		if(payType.equals(PaymentType.US))
			browser.findElement(By.xpath("(//input[@id='isPayeeToFormer'])[2]")).click();		
		browser.findElement(By.name("continueToNextStep")).click();
	}


	public void searchForSupplier(){
		String mainHandle = browser.getDriver().getWindowHandle();
		Set<String> currHandles = browser.getDriver().getWindowHandles();		
		browser.findElement(By.linkText("Search for supplier")).click();		
		browser.switchToWindow(currHandles);

		browser.findElement(By.xpath("(//input[@id='searchType'])[2]")).click();
		browser.findElement(By.id("supplierNbr")).clear();
		browser.findElement(By.id("supplierNbr")).sendKeys(this.payItem.getSupplierNumber());
		browser.findElement(By.name("go")).click();
		browser.findElement(By.id("payeeSearchResultsTbl_LeftChoice")).click();
		browser.findElement(By.name("designatePayee")).click();

		browser.switchToWindow(mainHandle);
		String zipcode;
		if(payType.equals(PaymentType.US))
			zipcode = browser.findElement(By.id("partyBlk_postalCode")).getAttribute("value");
		else
			zipcode = null;
		browser.findElement(By.name("continueToNextStep")).click();
		this.payItem.setZipcode(zipcode);
	}
	
	
	public void enterCommodityCode(){
		browser.findSelect(By.id("commodityCd")).selectByVisibleText("BUS PARTNER FEE/INCENTIVE DISBURSEM - Z40");
		browser.findElement(By.name("addItemAction")).click();
		
		browser.findElement(By.id("itemDetails_itemDescripField")).sendKeys(this.payItem.getDescription());
		browser.findElement(By.id("itemDetails_amtExcludingTaxField")).sendKeys(this.payItem.getFee().toString());
		if(payType.equals(PaymentType.US))
			browser.findSelect(By.id("itemDetails_taxDescripField")).selectByVisibleText("Non Taxable");
		else
			browser.findSelect(By.id("itemDetails_taxDescripField")).selectByVisibleText(getCanadaTaxOption());
		browser.findElement(By.name("addItemReturnToPayReqst")).click();
		if(payType.equals(PaymentType.CANADA) && !applicableTaxRegistration().equals(""))
			browser.findElement(By.name("rfTaxIds_" + applicableTaxRegistration() + "RegnNbr")).sendKeys("0");
		browser.findElement(By.name("continueToNextStep")).click();
	}
	
	
	/**
	 * Inputs a note to approvers and the duedate for payment.
	 * @param defaultNoteToApprover
	 */
	public void enterNotesToApprovers(){
		browser.findElement(By.id("notesToApprover")).sendKeys(this.payItem.getNoteToApprover());
		browser.findElement(By.id("notesToPayee")).sendKeys(this.payItem.getNoteToApprover());
		browser.findElement(By.xpath("//input[contains(@id, 'paymentTerms') and contains(@value, 'DATE')]")).click();
		
		DateTime pmtDueDate = new DateTime().plusDays(DUEDATE_MODIFIER);
		Integer year = pmtDueDate.getYear();
		Integer adaptedMonth = pmtDueDate.getMonthOfYear() - 1;
		Integer day = pmtDueDate.getDayOfMonth();
		
		browser.findSelect(By.id("dueDateMonth")).selectByValue(adaptedMonth.toString());
		browser.findSelect(By.id("dueDateDay")).selectByValue(day.toString());
		browser.findSelect(By.id("dueDateYear")).selectByValue(year.toString());
		
		if(browser.isElementPresent(By.id("isPayForProvidedTo"))){
			browser.findElement(By.xpath("//input[contains(@id, 'isPayForProvidedTo') and contains(@value, '0')]")).click();
			browser.findElement(By.xpath("//input[contains(@id, 'wasServicePhysPerf') and contains(@value, '0')]")).click();
			browser.findElement(By.xpath("//input[contains(@id, 'isThereCAWithHTaxWaiver') and contains(@value, '0')]")).click();
		}
		
		browser.findElement(By.name("continueToNextStep")).click();
		browser.waitUntilPageIsLoaded();
		
		browser.findElement(By.name("continueToNextStep")).click();
	}
	
	
	public void editAccountingData(){
		browser.findElement(By.id("specifyItemsAcctingTbl_LeftChoice")).click();
		browser.findElement(By.name("editAccting")).click();
		
		Keys[] backspaces = {Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE};
		if(payType.equals(PaymentType.US)){	
			String dpt = "UBI";
			String majorDpt = "610";
			String minor = "0595";
			String subminor = "000";		
			browser.findElement(By.id("acctingBlk_deptField")).sendKeys(Keys.chord(backspaces) + dpt);
			browser.findElement(By.id("acctingBlk_deptMajorField")).sendKeys(majorDpt);
			browser.findSelect(By.id("acctingBlk_minorField")).selectByValue(minor);
			browser.findSelect(By.id("acctingBlk_subminorField")).selectByValue(subminor);
			browser.findElement(By.id("acctingBlk_postalCode")).sendKeys(this.payItem.getZipcode());
		} else {
			String dpt = "Q07";
			String AGENTS_COMMISSIONS = "6990595000";
			browser.findElement(By.id("acctingBlk_deptField")).sendKeys(Keys.chord(backspaces) + dpt);
			browser.findSelect(By.id("acctingBlk_glAcctField")).selectByValue(AGENTS_COMMISSIONS);
			//browser.findElement(By.name("chngGLAccntGo")).click();
			browser.waitUntilPageIsLoaded();
		}
		browser.findElement(By.name("continueToNextStep")).click();
		browser.waitUntilPageIsLoaded();
		
		browser.findElement(By.name("continueToNextStep")).click();
	}
	
	
	public void chooseApprovalPath(){
		browser.findElement(By.xpath("//input[contains(@id, 'approvalPathTbl') and contains(@value, 'CHRG')]")).click();
		browser.findElement(By.name("continueToNextStep")).click();
	}
	
	
	public void changeManagerApprovers(){
		browser.findElement(By.name("chngFirstMgrApprover")).click();
		browser.findElement(By.id("lastName")).sendKeys(this.firstApproverLastName);
		browser.findElement(By.id("firstName")).sendKeys(this.firstApproverFirstName);
		browser.findElement(By.name("go")).click();
		browser.findElement(By.id("mgrSearchResults_LeftChoice")).click();
		browser.findElement(By.name("changeFirstLineToSelectedApprover")).click();
		if(this.payItem.isOver5k()){
			browser.findElement(By.name("chngSecondMgrApprover")).click();
			browser.findElement(By.id("lastName")).sendKeys(this.secondApproverLastName);
			browser.findElement(By.id("firstName")).sendKeys(this.secondApproverFirstName);
			browser.findElement(By.name("go")).click();
			browser.findElement(By.id("mgrSearchResults_LeftChoice")).click();
			browser.findElement(By.name("changeFirstLineToSelectedApprover")).click();
		}
	}
	
	
	public String submitPayment(){
		String payId = retrievePayId();
		this.payItem.setPaymentId(payId);
		// *** Uncomment next and comment submit for tests.
		// browser.findElement(By.name("saveAndClose")).click();
		// *** Uncomment next and comment save and close for production.
		browser.findElement(By.name("sbmt")).click();
		return payId;
	}
	
	
	private String retrievePayId(){
		browser.waitUntilPageIsLoaded();
		return browser.findElement(By.xpath("/html/body/div[6]/div[2]/form/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]")).getText();
	}
	
	
	private String getCanadaTaxOption(){
		return this.payItem.getCanadaTaxOption();
	}
	
	
	private String applicableTaxRegistration(){
		return this.payItem.getApplicableTaxRegistration();
	}

}
