package group.msg.at.cloud.cloudtrain.adapter.rest;

import group.msg.at.cloud.cloudtrain.core.entity.Task;
import group.msg.at.cloud.cloudtrain.core.entity.TaskCategory;
import group.msg.at.cloud.cloudtrain.core.entity.TaskLifeCycleState;
import group.msg.at.cloud.cloudtrain.core.entity.TaskPriority;
import group.msg.at.cloud.common.test.rest.RestAssuredSystemTestFixture;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * System test that verifies that the REST endpoint works as expected.
 * <p>
 * Assumes that a remote server hosting the REST endpoint is up and running.
 * </p>
 */
public class TasksEndpointSystemTest {

    private static final RestAssuredSystemTestFixture fixture = new RestAssuredSystemTestFixture();

    private final List<String> trashBin = new ArrayList<>();

    @BeforeAll
    public static void onBeforeClass() {
        fixture.onBefore();
    }

    @AfterAll
    public static void onAfterClass() {
        fixture.onAfter();
    }

    @AfterEach
    public void onAfter() {
        for (String current : this.trashBin) {
            try {
                given().auth().oauth2(fixture.getAccessToken()).delete(current).then().assertThat().statusCode(204);
            } catch (AssertionError ex) {
                System.err.printf("failed to delete task at [%s]: got unexpected status code: %s%n", current, ex.getMessage());
            } catch (Exception ex) {
                System.err.printf("failed to delete task at [%s]: got unexpected exception: %s%n", current, ex.getMessage());
            }
        }
    }

    @Test
    void postWithValidTaskReturns201AndLocation() {
        addTask(createTask());
    }

    @Test
    void getWithValidTaskIdReturnsValidTask() {
        Task expected = createTask();
        String location = addTask(expected);
        Task actual = given().log().body(true).auth().oauth2(fixture.getAccessToken())
                .accept(ContentType.JSON)
                .get(location)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(Task.class);
        assertNotNull(actual, "GET with valid location must return non-null task");
        assertAttributesMatch(expected, actual);
        assertValid(actual);
    }

    @Test
    void getWithoutTaskIdReturnsAllTasks() {
        addTask(createTask());
        List<Task> response = given().auth().oauth2(fixture.getAccessToken())
                .accept(ContentType.JSON)
                .get("api/v1/tasks")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(new TypeRef<List<Task>>() {
                });
        assertThat(response).isNotEmpty();
    }

    private String addTask(Task newTask) {
        Response postResponse = given().auth().oauth2(fixture.getAccessToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newTask)
                .post("api/v1/tasks")
                .andReturn();
        String location = postResponse.header("location");
        if (location != null) {
            this.trashBin.add(location);
        }
        postResponse.then().assertThat()
                .statusCode(201);
        return location;
    }

    private Task createTask() {
        Task result = new Task();
        result.setSubject("test");
        result.setDescription("this is a test instance");
        result.setCategory(TaskCategory.NEW_FEATURE);
        result.setPriority(TaskPriority.MEDIUM);
        result.setAffectedProjectId("CloudTrain");
        result.setAffectedApplicationId("cnj-tracing");
        return result;
    }

    private void assertAttributesMatch(Task expected, Task actual) {
        assertEquals(expected.getSubject(), actual.getSubject(), "subject");
        assertEquals(expected.getDescription(), actual.getDescription(), "description");
        assertEquals(expected.getCategory(), actual.getCategory(), "category");
        assertEquals(expected.getPriority(), actual.getPriority(), "priority");
        assertEquals(expected.getAffectedProjectId(), actual.getAffectedProjectId(), "affectedProjectId");
        assertEquals(expected.getAffectedApplicationId(), actual.getAffectedApplicationId(), "affectedApplicationId");
    }

    private void assertValid(Task task) {
        assertThat(task.getId()).as("id of task [%s]", task).isNotNull();
        assertThat(task.getSubject()).as("subject of task [%s]", task).isNotBlank();
        assertThat(task.getDescription()).as("description of task [%s]", task).isNotBlank();
        assertThat(task.getCategory()).as("category of task [%s]", task).isNotNull();
        assertThat(task.getPriority()).as("priority of task [%s]", task).isNotNull();
        assertThat(task.getLifeCycleState()).as("lifeCycleState of task [%s]", task).isNotNull();
        if (TaskLifeCycleState.OPEN_UNDER_WORK.compareTo(task.getLifeCycleState()) <= 0) {
            assertThat(task.getSubmittedAt()).as("submittedAt of task [%s]", task).isBefore(LocalDateTime.now());
            assertThat(task.getSubmitterUserId()).as("submitterUserId of task [%s]", task).isNotBlank();
        }
        if (TaskLifeCycleState.CLOSED_COMPLETED.equals(task.getLifeCycleState())) {
            assertThat(task.getCompletionDate()).as("completionDate of task [%s]", task).isBefore(LocalDateTime.now());
            assertThat(task.getCompletedByUserId()).as("completedByUserId of task [%s]", task).isNotBlank();
        }
        assertThat(task.getCreatedAt()).as("createdAt of task [%s]", task).isBefore(LocalDateTime.now());
        assertThat(task.getCreatedBy()).as("createdBy of task [%s]", task).isNotBlank();
        assertThat(task.getLastModifiedAt()).as("lastModifiedAt of task [%s]", task).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(task.getLastModifiedBy()).as("lastModifiedBy of task [%s]", task).isNotBlank();
     }
}
