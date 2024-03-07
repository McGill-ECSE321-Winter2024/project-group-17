package ca.mcgill.ecse321.SportCenterManager.dto;

public class ErrorDto {
	
	private String message;
	
	@SuppressWarnings("unused")
	public ErrorDto() {
		
	}
	
	public ErrorDto(String msg) {
		message = msg;
	}
	
	public String getMessage() {
		return message;
	}
}
