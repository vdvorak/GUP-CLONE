package ua.com.itproekt.gup.exception;

import java.util.HashMap;
import java.util.Map;

public class BaseException extends RuntimeException {
	private static final long serialVersionUID = -8961905267911341174L;
	
	protected String                  message;
	protected ErrorCode             errorCode;
	protected String             debugMessage;
	protected Map<String, String> messageArgs = new HashMap<String, String>();

    @Override
	public String getMessage(){
		return this.message;
	}
	public void setMessage(String message){
		this.message = message;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getDebugMessage() {
		return debugMessage;
	}
	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}
	
	public Map<String, String> getMessageArgs() {
		return messageArgs;
	}
	public void setMessageArgs(Map<String, String> messageArgs) {
		this.messageArgs = messageArgs;
	}

	public RestError transformException(int httpStatus) {
		RestError restError = new RestError();

		restError.setHttpStatus( httpStatus );
		restError.setServiceId( errorCode.getServiceId() );
		restError.setErrorCode( errorCode.getErrorCode() );
		restError.setDebugMessage( debugMessage );
		restError.setMessageArgs( messageArgs );

		return restError;
	}
}
