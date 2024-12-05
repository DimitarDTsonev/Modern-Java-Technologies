package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidOrderEntityException extends InvalidOrderException {

    public InvalidOrderEntityException(String message) {

        super(message);

    }

    public InvalidOrderEntityException(String message, Throwable cause) {

        super(message, cause);

    }

}