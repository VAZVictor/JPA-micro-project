package at.htl.api;

import at.htl.model.listeningSession.CreateListeningSessionRequest;
import at.htl.workloads.listeningSession.ListeningSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/listening-sessions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ListeningSessionResource {

    private final ListeningSessionService sessionService;

    public ListeningSessionResource(ListeningSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @POST
    public Response recordSession(@NotNull @Valid CreateListeningSessionRequest request) {
        return Response.ok(sessionService.recordSession(request)).build();
    }
}
