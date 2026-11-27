package at.htl.api;

import at.htl.model.audioItem.CreateTrackRequest;
import at.htl.model.audioItem.UpdateTrackRequest;
import at.htl.workloads.audioItem.AudioItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/tracks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TrackResource {

    private final AudioItemService audioItemService;

    public TrackResource(AudioItemService audioItemService) {
        this.audioItemService = audioItemService;
    }

    @POST
    public Response createTrack(@NotNull @Valid CreateTrackRequest request) {
        return Response.ok(audioItemService.createTrack(request)).build();
    }

    @GET
    public Response getTracks() {
        return Response.ok(audioItemService.getAllTracks()).build();
    }

    @GET
    @Path("/{id}")
    public Response getTrack(@PathParam("id") Long id) {
        return Response.ok(audioItemService.getTrack(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTrack(@PathParam("id") Long id, @NotNull @Valid UpdateTrackRequest request) {
        return Response.ok(audioItemService.updateTrack(id, request)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTrack(@PathParam("id") Long id) {
        audioItemService.deleteTrack(id);
        return Response.noContent().build();
    }
}
