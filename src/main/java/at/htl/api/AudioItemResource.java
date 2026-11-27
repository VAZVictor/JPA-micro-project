package at.htl.api;

import at.htl.workloads.audioItem.AudioItemService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;

@Path("/api/audio-items")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AudioItemResource {

    private final AudioItemService audioItemService;

    public AudioItemResource(AudioItemService audioItemService) {
        this.audioItemService = audioItemService;
    }

    @GET
    public Response getAudioItems() {
        return Response.ok(audioItemService.getAllAudioItems()).build();
    }

    @GET
    @Path("/unused")
    public Response getUnused(@QueryParam("from") LocalDate from, @QueryParam("to") LocalDate to) {
        return Response.ok(audioItemService.getUnused(from, to)).build();
    }

    @GET
    @Path("/{id}")
    public Response getAudioItem(@PathParam("id") Long id) {
        return Response.ok(audioItemService.getAudioItem(id)).build();
    }
}
