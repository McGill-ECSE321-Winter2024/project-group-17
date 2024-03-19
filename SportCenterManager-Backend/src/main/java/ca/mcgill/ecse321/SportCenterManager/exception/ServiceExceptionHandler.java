package ca.mcgill.ecse321.SportCenterManager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ca.mcgill.ecse321.SportCenterManager.dto.ErrorDto;

@ControllerAdvice
public class ServiceExceptionHandler {
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ErrorDto> handleServiceException(ServiceException e){
		return new ResponseEntity<ErrorDto>(new ErrorDto(e.getMessage()), e.getStatus());
	}
}
