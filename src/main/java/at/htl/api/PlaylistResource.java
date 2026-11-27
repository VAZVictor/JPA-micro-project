package at.htl.api;

import at.htl.model.playlist.AddEntryRequest;
import at.htl.model.playlist.CreatePlaylistRequest;
import at.htl.model.playlist.ReorderPlaylistRequest;
import at.htl.model.playlist.UpdatePlaylistRequest;
import at.htl.workloads.playlist.PlaylistService;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/playlists")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlaylistResource {

    private final PlaylistService playlistService;

    public PlaylistResource(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @POST
    public Response createPlaylist(@NotNull @Valid CreatePlaylistRequest request) {
        return Response.ok(playlistService.createPlaylist(request)).build();
    }

    @GET
    public Response getPlaylists(@QueryParam("userId") Long userId) {
        return Response.ok(playlistService.getOverviews(userId)).build();
    }

    @GET
    @Path("/{id}")
    public Response getPlaylist(@PathParam("id") Long id) {
        return Response.ok(playlistService.getPlaylist(id)).build();
    }

    @GET
    @Path("/{id}/overview")
    public Response getOverview(@PathParam("id") Long id) {
        return Response.ok(playlistService.getOverview(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePlaylist(@PathParam("id") Long id, @NotNull @Valid UpdatePlaylistRequest request) {
        return Response.ok(playlistService.updatePlaylist(id, request)).build();
    }

    @POST
    @Path("/{id}/entries")
    public Response addEntry(@PathParam("id") Long id, @NotNull @Valid AddEntryRequest request) {
        return Response.ok(playlistService.addEntry(id, request)).build();
    }

    @DELETE
    @Path("/{id}/entries/{position}")
    public Response removeEntry(@PathParam("id") Long id, @PathParam("position") Integer position) {
        return Response.ok(playlistService.removeEntry(id, position)).build();
    }

    @PUT
    @Path("/{id}/order")
    public Response reorder(@PathParam("id") Long id, @NotNull @Valid ReorderPlaylistRequest request) {
        return Response.ok(playlistService.reorder(id, request)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePlaylist(@PathParam("id") Long id) {
        playlistService.deletePlaylist(id);
        return Response.noContent().build();
    }
}
