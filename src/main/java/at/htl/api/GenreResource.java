package at.htl.api;

import at.htl.model.genre.CreateGenreRequest;
import at.htl.workloads.genre.GenreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/genres")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GenreResource {

    private final GenreService genreService;

    public GenreResource(GenreService genreService) {
        this.genreService = genreService;
    }

    @POST
    public Response createGenre(@NotNull @Valid CreateGenreRequest request) {
        return Response.ok(genreService.createGenre(request)).build();
    }

    @GET
    public Response getGenres() {
        return Response.ok(genreService.getAllGenres()).build();
    }
}
