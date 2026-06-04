package concept.com.example.club.common.exception;

public class NoAvailableSpotsException extends RuntimeException{
    public NoAvailableSpotsException(String message) {
        super(message);
    }
}
