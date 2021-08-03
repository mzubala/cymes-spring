package pl.com.bottega.cymes.cinemas.dataaccess;

import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;

@RequiredArgsConstructor
public class PSQLExceptionWrapper {
    /**
     * Source: https://www.postgresql.org/docs/current/errcodes-appendix.html
     */
    private static final String UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE = "23505";

    private final PSQLException psqlException;

    public boolean isUniqueConstraintViolation() {
        Throwable exception = psqlException;
        do {
            if (exception instanceof PSQLException && hasErrorCode((PSQLException) exception, UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE)) {
                return true;
            }
            exception = exception.getCause();
        } while (exception != null);
        return false;
    }

    private boolean hasErrorCode(PSQLException exception, String errorCode) {
        return exception.getServerErrorMessage() != null && errorCode.equals(exception.getServerErrorMessage().getSQLState());
    }
}
