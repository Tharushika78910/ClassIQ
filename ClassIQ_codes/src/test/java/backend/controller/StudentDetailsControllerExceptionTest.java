package backend.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class StudentDetailsControllerExceptionTest {

    @Test
    void constructorWithMessage_shouldStoreMessage() {
        StudentDetailsControllerException exception =
                new StudentDetailsControllerException("Controller error");

        assertEquals("Controller error", exception.getMessage());
    }

    @Test
    void constructorWithMessageAndCause_shouldStoreCause() {
        Throwable cause = new RuntimeException("Root cause");

        StudentDetailsControllerException exception =
                new StudentDetailsControllerException("Controller error", cause);

        assertEquals("Controller error", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}