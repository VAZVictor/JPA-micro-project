package at.htl.api;

import at.htl.error.ConflictException;
import at.htl.error.DomainException;
import at.htl.error.DuplicateException;
import at.htl.error.InvalidRequestException;
import at.htl.error.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {

    @Override
    public Response toResponse(DomainException exception) {
        var status = switch (exception) {
            case NotFoundException _ -> Response.Status.NOT_FOUND;
            case DuplicateException _ -> Response.Status.BAD_REQUEST;
            case InvalidRequestException _ -> Response.Status.BAD_REQUEST;
            case ConflictException _ -> Response.Status.CONFLICT;
        };
        return Response.status(status).build();
    }
}
