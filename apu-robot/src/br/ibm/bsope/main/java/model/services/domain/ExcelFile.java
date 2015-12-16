package br.ibm.bsope.main.java.model.services.domain;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelFile {
	
	private Workbook workbook;
	private String fileFullPath;
	
	/**
	 * Factory method to get instance of existing Excel file.
	 * @param inputFile
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	public static ExcelFile openExistingExcelFile(String inputFile) throws IOException, InvalidFormatException{
		ExcelFile excelFile = new ExcelFile();
		excelFile.setFileFullPath(inputFile);
		FileInputStream in = new FileInputStream(inputFile);
		excelFile.setWorkbook(WorkbookFactory.create(in));
		in.close();
		
		return excelFile;
	}
	
	/**
	 * Avoid instance generation from outside this class.
	 */
	private ExcelFile(){
		
	}
	
	/**
	 * Save the Excel file to the file path and name declared in the instance construction.
	 * @throws IOException 
	 */
	public void saveFile() throws IOException{
		FileOutputStream out = new FileOutputStream(this.fileFullPath);
		this.workbook.write(out); 
		out.flush();
		out.close();
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public String getFileFullPath() {
		return fileFullPath;
	}

	private void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}
	
	

}
