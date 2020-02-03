package it.easytechs.difacile.communication.core.exception;

public class CommunicationException extends Exception {

    private static final long serialVersionUID = 1L;

    public CommunicationException(String message){
    	super(message);
    }
    
    public CommunicationException(){
    	super();
    }
    
}