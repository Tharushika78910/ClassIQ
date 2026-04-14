package backend.service;

public class StudentDetailsServiceException extends Exception {

    public StudentDetailsServiceException(String message) {
        super(message);
    }

    public StudentDetailsServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}