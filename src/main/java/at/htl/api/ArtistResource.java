package at.htl.api;

import at.htl.model.artist.CreateArtistRequest;
import at.htl.workloads.artist.ArtistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/artists")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ArtistResource {

    private final ArtistService artistService;

    public ArtistResource(ArtistService artistService) {
        this.artistService = artistService;
    }

    @POST
    public Response createArtist(@NotNull @Valid CreateArtistRequest request) {
        return Response.ok(artistService.createArtist(request)).build();
    }

    @GET
    public Response getArtists() {
        return Response.ok(artistService.getAllArtists()).build();
    }

    @GET
    @Path("/top")
    public Response getTopArtists(@QueryParam("minSeconds") Long minSeconds,
                                  @QueryParam("page") Integer page,
                                  @QueryParam("size") Integer size) {
        return Response.ok(artistService.getTopArtists(minSeconds, page, size)).build();
    }
}
