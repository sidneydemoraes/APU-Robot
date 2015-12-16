package br.ibm.bsope.main.java.model.services.infrastructure;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import br.ibm.bsope.main.java.model.FeePaymentItem;
import br.ibm.bsope.main.java.model.exceptions.EmptyFileException;
import br.ibm.bsope.main.java.model.exceptions.InvalidSpreadsheetFormatException;
import br.ibm.bsope.main.java.model.services.domain.ExcelFile;
import br.ibm.bsope.main.java.model.services.domain.ApuServices.PaymentType;

public class FeePaymentItemRepository {
	
	private ArrayList<FeePaymentItem> items;
	private ExcelFile excel;
	private PaymentType payType;
	private int currentIndex;
	
	private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
	
	private final int USCOL_SUPPNUM = 1;
	private final int USCOL_FEE = 2;
	private final int USCOL_DESC = 3;
	private final int USCOL_PAYID = 4;
	
	private final int CACOL_SUPPNUM = 5;
	private final int CACOL_FEE = 22;
	private final int CACOL_COA = 0;
	private final int CACOL_CUSTNAME = 1;
	private final int CACOL_PROVINCE = 8;
	private final int CACOL_FEERATE = 20;
	private final int CACOL_PAYID = 28;

	private final String titleSupplierNumber = "supplier number";
	private final String titleFee = "fee";
	private final String titleCoa = "coa";
	private final String titleCustomerName = "customer";
	private final String titleProvince = "prov";
	private final String titleFeeRate = "fee rate";
	private final String titleComments = "comments";
	private final String titleRequestId = "request id";
	
	
	
	/**
	 * Factory method to get an instance of a {@link FeePaymentItem}.
	 * @param excelFile
	 * @param payType
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EmptyFileException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws InvalidSpreadsheetFormatException 
	 */
	public static FeePaymentItemRepository getRepoInstance(ExcelFile excelFile, PaymentType payType) throws IOException, InvalidFormatException, EmptyFileException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvalidSpreadsheetFormatException{
		return new FeePaymentItemRepository(excelFile, payType);
	}
	
	
	private FeePaymentItemRepository(ExcelFile excelFile, PaymentType payType) throws IOException, InvalidFormatException, EmptyFileException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvalidSpreadsheetFormatException{
		this.payType = payType;
		this.items = excelToItemsArray(excelFile);
	}
	
	
	/**
	 * Gets current item from list.
	 * @return
	 */
	public FeePaymentItem getCurrentItem(){
		return currentIndex < items.size() ? items.get(currentIndex) : null;
	}
	
	
	/**
	 * Skips current item from list and gets the next one.
	 * @return
	 */
	public FeePaymentItem getNextItem(){
		return currentIndex + 1 < items.size() ? items.get(++currentIndex) : null;
	}
	
	
	public void concludeCurrentItem(){
		updatePaymentId();
		currentIndex++;
	}
	
	
	/**
	 * Receives a {@link File} and returns a {@link ArrayList} of {@link FeePaymentItem}
	 * @param excelFile
	 * @param payType
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EmptyFileException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws InvalidSpreadsheetFormatException 
	 */
	private ArrayList<FeePaymentItem> excelToItemsArray(ExcelFile excelFile) throws IOException, InvalidFormatException, EmptyFileException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvalidSpreadsheetFormatException{
		excel = excelFile;
		Sheet sheet = excel.getWorkbook().getSheetAt(0);
		
		Row header = sheet.getRow(0);
		if(!isSpreadsheetContentValid(header)) 
			throw new InvalidSpreadsheetFormatException();
		
		int lastRow = sheet.getLastRowNum();
		if(lastRow == 0) throw new EmptyFileException();
		
		ArrayList<FeePaymentItem> list = new ArrayList<FeePaymentItem>();
		
		for(int rowline = 1; rowline < lastRow; rowline++){
			
			FeePaymentItem item;
			Row row = sheet.getRow(rowline);
			
			if(this.payType.equals(PaymentType.US)){
				
				Cell cell = row.getCell(USCOL_SUPPNUM);
				
				if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
					break;
				
				String supplierNumber = (cell.getCellType() == Cell.CELL_TYPE_STRING) ?
						cell.getStringCellValue() :
						new BigDecimal(cell.getNumericCellValue()).toString();
								
				cell = row.getCell(USCOL_FEE);
				Double fee = (cell.getCellType() == Cell.CELL_TYPE_STRING) ?
						new Double(cell.getStringCellValue()) :
						new Double(decimalFormat.format(cell.getNumericCellValue()));
						
				cell = row.getCell(USCOL_DESC);
				String description = cell.getStringCellValue();
				
				item = FeePaymentItem.getUSInstance(supplierNumber, fee, description);
				item.setNoteToApprover(description);
				
			} else {
				
				Cell cell = row.getCell(CACOL_SUPPNUM);	
				
				if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
					break;
						
				String supplierNumber = (cell.getCellType() == Cell.CELL_TYPE_STRING) ?
						cell.getStringCellValue() :
							new BigDecimal(cell.getNumericCellValue()).toString();
										
				cell = row.getCell(CACOL_FEE);
				Double fee = (cell.getCellType() == Cell.CELL_TYPE_STRING) ?
						new Double(cell.getStringCellValue()) :
						new Double(decimalFormat.format(cell.getNumericCellValue()));
						
				String descStart = "IGF BP FOR ";
				String coa = row.getCell(CACOL_COA).getStringCellValue();
				String customer = row.getCell(CACOL_CUSTNAME).getStringCellValue();
				String description = descStart + coa + " END USER " + customer;
				String province = row.getCell(CACOL_PROVINCE).getStringCellValue();
				
				cell = row.getCell(CACOL_FEERATE);
				String feeRate = (cell.getCellType() == Cell.CELL_TYPE_STRING) ?
						new Double(cell.getStringCellValue()).toString() :
						decimalFormat.format(cell.getNumericCellValue() * 100);
								
				item = FeePaymentItem.getCAInstance(supplierNumber, fee, description, province, feeRate);
				item.setNoteToApprover("IGF BP FEE OF " + feeRate + " FOR " + coa + " END USER " + customer);
				
			}
			
			list.add(item);
			
		}

		return list;
		
	}
	
	
	private void updatePaymentId(){
		Sheet sheet = excel.getWorkbook().getSheetAt(0);
		int currentRow = currentIndex + 1;
		Row row = sheet.getRow(currentRow);
		Cell cell;
		if(this.payType.equals(PaymentType.US)){
			cell = row.getCell(USCOL_PAYID);
			if(cell == null) cell = row.createCell(USCOL_PAYID);
		} else {
			cell = row.getCell(CACOL_PAYID);
			if(cell == null) cell = row.createCell(CACOL_PAYID);
		}
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(this.items.get(currentIndex).getPaymentId());
		try {
			excel.saveFile();
		} catch (IOException e) {
			// TODO log error
		}
	}
	
	
	private boolean isSpreadsheetContentValid(Row header){		
		
		if(this.payType.equals(PaymentType.US)){
			
			return header.getCell(USCOL_SUPPNUM).getStringCellValue().equalsIgnoreCase(titleSupplierNumber) &&
				   header.getCell(USCOL_FEE).getStringCellValue().equalsIgnoreCase(titleFee) &&
				   header.getCell(USCOL_DESC).getStringCellValue().equalsIgnoreCase(titleComments) &&
				   header.getCell(USCOL_PAYID).getStringCellValue().equalsIgnoreCase(titleRequestId);
			
		} else {
			
			return header.getCell(CACOL_SUPPNUM).getStringCellValue().equalsIgnoreCase(titleSupplierNumber) &&
				   header.getCell(CACOL_FEE).getStringCellValue().equalsIgnoreCase(titleFee) &&
				   header.getCell(CACOL_COA).getStringCellValue().equalsIgnoreCase(titleCoa) &&
				   header.getCell(CACOL_CUSTNAME).getStringCellValue().equalsIgnoreCase(titleCustomerName) &&
				   header.getCell(CACOL_PROVINCE).getStringCellValue().equalsIgnoreCase(titleProvince) &&
				   header.getCell(CACOL_FEERATE).getStringCellValue().equalsIgnoreCase(titleFeeRate) &&
				   header.getCell(CACOL_PAYID).getStringCellValue().equalsIgnoreCase(titleRequestId);
			
		}
		
	}
	
}
