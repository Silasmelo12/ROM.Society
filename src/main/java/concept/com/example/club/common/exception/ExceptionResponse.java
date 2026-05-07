package concept.com.example.club.common.exception;

import java.util.Date;

public record ExceptionResponse (
        Date timestamp,
        String message,
        String details
) {
}
