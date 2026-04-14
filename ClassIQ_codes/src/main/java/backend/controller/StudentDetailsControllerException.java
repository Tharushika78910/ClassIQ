package backend.controller;

public class StudentDetailsControllerException extends Exception {

    public StudentDetailsControllerException(String message) {
        super(message);
    }

    public StudentDetailsControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}