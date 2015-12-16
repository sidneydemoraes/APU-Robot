package br.ibm.bsope.main.java.model.exceptions;

public class InvalidSpreadsheetFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidSpreadsheetFormatException(){
		super("Spreadsheet header is invalid according to the business process.");
	}

}
