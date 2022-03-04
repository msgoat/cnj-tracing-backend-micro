package group.msg.at.cloud.cloudtrain.adapter.rest;

import group.msg.at.cloud.cloudtrain.core.boundary.TaskManagement;
import group.msg.at.cloud.cloudtrain.core.entity.Task;
import group.msg.at.cloud.common.rest.uri.RouterAwareUriBuilderFactory;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST endpoint managing {@link Task} entities.
 * <p>
 * Handles only the mapping of a REST invocation to a Java method invocation;
 * all transactional business logic is encapsulated in a {@code Boundary} this resource class delegates to.
 * </p>
 */
@RequestScoped
@Path("v1/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("CLOUDTRAIN_USER")
public class TasksResource {

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    @Inject
    private TaskManagement boundary;

    @GET
    public Response getAllTasks() {
        Response result;
        List<Task> found = this.boundary.getAllTasks();
        result = Response.ok(found).build();
        return result;
    }

    @GET
    @Path("{taskId}")
    public Response getTask(@PathParam("taskId") UUID taskId) {
        Response result;
        Task found = this.boundary.getTaskById(taskId);
        if (found != null) {
            result = Response.ok(found).build();
        } else {
            result = Response.status(Response.Status.NOT_FOUND).build();
        }
        return result;
    }

    @POST
    public Response addTask(Task task) {
        Response result;
        UUID taskId = this.boundary.addTask(task);
        URI location = RouterAwareUriBuilderFactory.from(uriInfo, httpHeaders).path("{taskId}").build(taskId);
        result = Response.created(location).build();
        return result;
    }

    @PUT
    @Path("{taskId}")
    public Response modifyTask(@PathParam("taskId") UUID taskId, Task task) {
        Response result;
        this.boundary.modifyTask(task);
        result = Response.noContent().build();
        return result;
    }

    @DELETE
    @Path("{taskId}")
    public Response removeTask(@PathParam("taskId") UUID taskId) {
        Response result;
        this.boundary.removeTask(taskId);
        result = Response.noContent().build();
        return result;
    }
}
