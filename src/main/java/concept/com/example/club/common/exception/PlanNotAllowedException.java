package concept.com.example.club.common.exception;

public class PlanNotAllowedException extends RuntimeException{

    public PlanNotAllowedException(String message){
        super(message);
    }
}
