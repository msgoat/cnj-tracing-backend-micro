package group.msg.at.cloud.cloudtrain.core.boundary;

import group.msg.at.cloud.cloudtrain.adapter.persistence.jpa.repository.GenericRepository;
import group.msg.at.cloud.cloudtrain.core.control.UserPermissionVerifier;
import group.msg.at.cloud.cloudtrain.core.entity.Task;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Simple {@code Boundary} that manages {@code Task} entities.
 */
@Stateless
@RolesAllowed("CLOUDTRAIN_USER")
public class TaskManagement {

    @Inject
    private GenericRepository repository;

    @Inject
    private UserPermissionVerifier verifier;

    @NotNull
    public UUID addTask(@NotNull @Valid Task newTask) {
        verifier.requirePermission("TASK_CREATE");
        newTask.setId(UUID.randomUUID());
        this.repository.addEntity(newTask);
        return newTask.getId();
    }

    public void modifyTask(@NotNull @Valid Task modifiedTask) {
        verifier.requirePermission("TASK_UPDATE");
        this.repository.setEntity(modifiedTask);
    }

    public Task getTaskById(@NotNull UUID taskId) {
        verifier.requirePermission("TASK_READ");
        return this.repository.getEntityById(Task.class, taskId);
    }

    public void removeTask(@NotNull UUID taskId) {
        verifier.requirePermission("TASK_DELETE");
        this.repository.removeEntityById(Task.class, taskId);
    }

    public List<Task> getAllTasks() {
        verifier.requirePermission("TASK_READ");
        return this.repository.queryEntities(Task.class, Task.QUERY_ALL, null);
    }
}
