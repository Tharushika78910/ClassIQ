package backend.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StudentDetailsServiceExceptionTest {

    @Test
    void constructorWithMessage_shouldStoreMessage() {
        StudentDetailsServiceException exception =
                new StudentDetailsServiceException("Test message");

        assertEquals("Test message", exception.getMessage());
    }

    @Test
    void constructorWithMessageAndCause_shouldStoreValues() {
        Throwable cause = new RuntimeException("Root cause");

        StudentDetailsServiceException exception =
                new StudentDetailsServiceException("Wrapped message", cause);

        assertEquals("Wrapped message", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(cause, exception.getCause());
    }
}