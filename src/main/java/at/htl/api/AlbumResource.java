package at.htl.api;

import at.htl.model.album.CreateAlbumRequest;
import at.htl.workloads.album.AlbumService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/albums")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlbumResource {

    private final AlbumService albumService;

    public AlbumResource(AlbumService albumService) {
        this.albumService = albumService;
    }

    @POST
    public Response createAlbum(@NotNull @Valid CreateAlbumRequest request) {
        return Response.ok(albumService.createAlbum(request)).build();
    }

    @GET
    public Response getAlbums() {
        return Response.ok(albumService.getAllAlbums()).build();
    }
}
