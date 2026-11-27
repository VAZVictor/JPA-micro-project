package at.htl.api;

import at.htl.model.device.CreateDeviceRequest;
import at.htl.model.favorite.FavoriteRequest;
import at.htl.model.user.CreateUserRequest;
import at.htl.workloads.listeningSession.ListeningSessionService;
import at.htl.workloads.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService userService;
    private final ListeningSessionService sessionService;

    public UserResource(UserService userService, ListeningSessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @POST
    public Response createUser(@NotNull @Valid CreateUserRequest request) {
        return Response.ok(userService.createUser(request)).build();
    }

    @GET
    public Response getUsers() {
        return Response.ok(userService.getAllUsers()).build();
    }

    @GET
    @Path("/{id}/devices")
    public Response getDevices(@PathParam("id") Long id) {
        return Response.ok(userService.getDevices(id)).build();
    }

    @POST
    @Path("/{id}/devices")
    public Response addDevice(@PathParam("id") Long id, @NotNull @Valid CreateDeviceRequest request) {
        return Response.ok(userService.addDevice(id, request)).build();
    }

    @GET
    @Path("/{id}/favorites")
    public Response getFavorites(@PathParam("id") Long id) {
        return Response.ok(userService.getFavorites(id)).build();
    }

    @POST
    @Path("/{id}/favorites")
    public Response addFavorite(@PathParam("id") Long id, @NotNull @Valid FavoriteRequest request) {
        return Response.ok(userService.addFavorite(id, request)).build();
    }

    @DELETE
    @Path("/{id}/favorites/{audioItemId}")
    public Response removeFavorite(@PathParam("id") Long id, @PathParam("audioItemId") Long audioItemId) {
        userService.removeFavorite(id, audioItemId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/listening-sessions")
    public Response getListeningSessions(@PathParam("id") Long id) {
        return Response.ok(sessionService.getSessions(id)).build();
    }
}
