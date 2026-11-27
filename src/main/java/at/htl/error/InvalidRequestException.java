package at.htl.error;

public final class InvalidRequestException extends DomainException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
