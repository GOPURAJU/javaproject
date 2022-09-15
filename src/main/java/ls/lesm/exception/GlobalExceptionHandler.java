package ls.lesm.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
 
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//UMER
	@ExceptionHandler(RoleAreadyExistException.class)
	public ResponseEntity<?> roleExistHandler(RoleAreadyExistException roleExist, WebRequest webRequest){
		
		ApiErorrs apiErorrs =new ApiErorrs(new Date(),roleExist.getErrorMessage(),roleExist.getErrorCode(),roleExist.getMessage());
		
		return new ResponseEntity<>(apiErorrs, HttpStatus.ALREADY_REPORTED); 
		
	}
	//UMER
	@ExceptionHandler(DuplicateEntryException.class)
	public ResponseEntity<?> duplicateEntryHandler(DuplicateEntryException ex, WebRequest webRequest){
		ApiErorrs apiErorrs =new ApiErorrs(new Date(),ex.getErrorMessage(), ex.getErrorCode(),ex.getMessage());
		return new ResponseEntity<>(apiErorrs, HttpStatus.ALREADY_REPORTED); 
		
	}
	
	//UMER
	@ExceptionHandler(RelationNotFoundExceptions.class)
	public ResponseEntity<?> duplicateEntryHandler(RelationNotFoundExceptions ex, WebRequest webRequest){
		ApiErorrs apiErorrs =new ApiErorrs(new Date(),ex.getErrorMessage(), ex.getErrorCode(),ex.getMessage());
		return new ResponseEntity<>(apiErorrs, HttpStatus.NOT_FOUND); 
		
	}
	//UMER
		@ExceptionHandler(RecordAlredyExistException.class)
		public ResponseEntity<?> recordExistHandler(RecordAlredyExistException raee, WebRequest webRequest){
			ApiErorrs apiErorrs =new ApiErorrs(new Date(),raee.getErrorMessage(), raee.getErrorCode(),raee.getMessage());
			return new ResponseEntity<>(apiErorrs, HttpStatus.NOT_FOUND); 
			
		}
		
		
		
		
		
		
		
		
		
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RecordNotFoundException.class)
	public Map<String, String> RecordNotFoundException(RecordNotFoundException recordNotFoundException, WebRequest webRequest){
		
		Map<String, String> errorMap = new HashMap<>();
				errorMap.put("ErrorMessage", recordNotFoundException.getMessage());
		
		return errorMap;
		
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DateMissMatchException.class)
	public Map<String, String> DateMissMatchExceptionsHandler(DateMissMatchException dateMissMatchExceptions, WebRequest webRequest){
		
		Map<String, String> errorMap = new HashMap<>();
				errorMap.put("ErrorMessage", dateMissMatchExceptions.getMessage());
		
		return errorMap;
		
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserAlreadinExistException.class)
	public Map<String, String> DateMissMatchExceptionsHandler(UserAlreadinExistException userAlreadinExistException, WebRequest webRequest){
		
		Map<String, String> errorMap = new HashMap<>();
				errorMap.put("ErrorMessage", userAlreadinExistException.getMessage());
		
		return errorMap;
	
}
	}
