package RomConcept.com.example.Club.Rom.Concept.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
}
