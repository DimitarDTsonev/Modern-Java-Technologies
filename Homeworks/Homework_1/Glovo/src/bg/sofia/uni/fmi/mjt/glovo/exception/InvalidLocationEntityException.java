package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidLocationEntityException extends RuntimeException {

    public InvalidLocationEntityException(String message) {

        super(message);

    }

    public InvalidLocationEntityException(String message, Throwable cause) {

        super(message, cause);

    }

}