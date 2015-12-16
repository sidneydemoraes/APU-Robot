package br.ibm.bsope.main.java.model.exceptions;

public class EmptyFileException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public EmptyFileException(){
		super("File has no inputs");
	}

}
