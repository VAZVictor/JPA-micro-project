package at.htl.api;

import at.htl.model.audioItem.CreatePodcastEpisodeRequest;
import at.htl.workloads.audioItem.AudioItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/podcast-episodes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PodcastEpisodeResource {

    private final AudioItemService audioItemService;

    public PodcastEpisodeResource(AudioItemService audioItemService) {
        this.audioItemService = audioItemService;
    }

    @POST
    public Response createPodcastEpisode(@NotNull @Valid CreatePodcastEpisodeRequest request) {
        return Response.ok(audioItemService.createPodcastEpisode(request)).build();
    }

    @GET
    public Response getPodcastEpisodes() {
        return Response.ok(audioItemService.getAllPodcastEpisodes()).build();
    }
}
