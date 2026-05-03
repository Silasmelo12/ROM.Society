package concept.com.example.club.exception;

public class TokenExpiradoException extends RuntimeException {
    public TokenExpiradoException(String message){
        super(message);
    }
}
