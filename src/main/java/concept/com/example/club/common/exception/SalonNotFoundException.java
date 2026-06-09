package concept.com.example.club.common.exception;

public class SalonNotFoundException extends RuntimeException{
    public SalonNotFoundException(String message){
        super(message);
    }
}
