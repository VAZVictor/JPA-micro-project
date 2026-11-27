package at.htl.error;

public abstract sealed class DomainException extends RuntimeException
        permits NotFoundException, DuplicateException, InvalidRequestException, ConflictException {

    protected DomainException(String message) {
        super(message);
    }
}
