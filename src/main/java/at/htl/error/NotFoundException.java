package at.htl.error;

public final class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(message);
    }
}
