package concept.com.example.club.common.exception;

public class RegistrationNotFoundException extends RuntimeException {
    public RegistrationNotFoundException(String message){
        super(message);
    }
}
