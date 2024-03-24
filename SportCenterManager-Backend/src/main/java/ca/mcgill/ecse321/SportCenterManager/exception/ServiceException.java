package ca.mcgill.ecse321.SportCenterManager.exception;

import org.springframework.http.HttpStatus;

import io.micrometer.common.lang.NonNull;

@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {
	@NonNull
	private HttpStatus httpStatus;
	
	public ServiceException(@NonNull HttpStatus status, String msg) {
		super(msg);
		this.httpStatus = status;
	}
	
	@NonNull
	public HttpStatus getStatus() {
		return this.httpStatus;
	}
}
