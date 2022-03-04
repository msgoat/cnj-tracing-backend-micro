package group.msg.at.cloud.cloudtrain.adapter.rest;

import group.msg.at.cloud.common.test.json.JsonpAssertions;
import group.msg.at.cloud.common.test.rest.RestAssuredSystemTestFixture;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * System test that verifies that the REST endpoint works as expected.
 * <p>
 * Assumes that a remote server hosting the REST endpoint is up and running.
 * </p>
 */
public class TasksEndpointSystemTest {

    private static final RestAssuredSystemTestFixture fixture = new RestAssuredSystemTestFixture();

    private static final String TEST_USER_ID = "cnj-tester";

    private static final String UNKNOWN_USER_ID = "anonymous";

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
                System.err.println(String.format("failed to delete task at [%s]: got unexpected status code: %s", current, ex.getMessage()));
            } catch (Exception ex) {
                System.err.println(String.format("failed to delete task at [%s]: got unexpected exception: %s", current, ex.getMessage()));
            }
        }
    }

    @Test
    public void postWithValidTaskReturns201AndLocation() {
        addTask(createTask());
    }

    @Test
    public void getWithValidTaskIdReturnsValidTask() {
        JsonObject expected = createTask();
        String location = addTask(expected);
        ExtractableResponse response = given().log().body(true).auth().oauth2(fixture.getAccessToken())
                .accept(ContentType.JSON)
                .get(location)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract();
        JsonObject actual = asJsonObject(response);
        assertNotNull(actual, "GET with valid location must return non-null task");
        assertAttributesMatch(expected, actual);
        assertValid(actual);
    }

    @Test
    public void getWithoutTaskIdReturnsAllTasks() {
        addTask(createTask());
        ExtractableResponse response = given().auth().oauth2(fixture.getAccessToken())
                .accept(ContentType.JSON)
                .get("api/v1/tasks")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract();
        JsonArray tasks = asJsonArray(response);
        assertFalse(tasks.isEmpty(), "GET must return at least one task");
    }

    // @Test
    public void addTaskWithForwardedHeadersMustReturnExpectedLocation() {
        Response postResponse = given().auth().oauth2(fixture.getAccessToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("X-Forwarded-Host", "apps.cloudtrain.aws.msgoat.eu")
                .header("X-Forwarded-Proto", "https")
                .header("X-Forwarded-Prefix", "/cloudtrain-int/cnj-tracing-backend-micro")
                .body(createTask().toString())
                .post("api/v1/tasks")
                .andReturn();
        String location = postResponse.header("location");
        System.out.println("location: [" + location + "]");
        if (location != null) {
            this.trashBin.add(location);
        }
        postResponse.then().assertThat()
                .statusCode(201);
    }

    private String addTask(JsonObject newTask) {
        Response postResponse = given().auth().oauth2(fixture.getAccessToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(createTask().toString())
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


    private JsonObject createTask() {
        return Json.createObjectBuilder()
                .add("subject", "test")
                .add("description", "this is a test instance")
                .add("category", "NEW_FEATURE")
                .add("priority", "MEDIUM")
                .add("affectedProjectId", "fwpss2019")
                .add("affectedApplicationId", "cnj-tracing")
                .build();
    }

    private JsonObject asJsonObject(ExtractableResponse response) {
        JsonObject result = null;
        try (InputStream in = response.body().asInputStream()) {
            result = Json.createReader(in).readObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    private JsonArray asJsonArray(ExtractableResponse response) {
        JsonArray result = null;
        try (InputStream in = response.body().asInputStream()) {
            result = Json.createReader(in).readArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    private void assertAttributesMatch(JsonObject expected, JsonObject actual) {
        assertAttributeMatches("subject", expected, actual);
        assertAttributeMatches("description", expected, actual);
        assertAttributeMatches("category", expected, actual);
        assertAttributeMatches("priority", expected, actual);
        assertAttributeMatches("affectedProjectId", expected, actual);
        assertAttributeMatches("affectedApplicationId", expected, actual);
    }

    private void assertValid(JsonObject task) {
        JsonpAssertions.assertIsUuid(task, "/id");
        JsonpAssertions.assertNotEmpty(task, "/subject");
        JsonpAssertions.assertNotEmpty(task, "/description");
        JsonpAssertions.assertNotEmpty(task, "/category");
        JsonpAssertions.assertNotEmpty(task, "/priority");
        JsonpAssertions.assertIsLocalDateTimeIfPresent(task, "/submittedAt");
        JsonpAssertions.assertNotEqualsIfPresent(task, "/submitterUserId", UNKNOWN_USER_ID);
        JsonpAssertions.assertIsLocalDateTimeIfPresent(task, "/dueDate");
        JsonpAssertions.assertIsLocalDateTimeIfPresent(task, "/completionDate");
        JsonpAssertions.assertNotEqualsIfPresent(task, "/completedByUserId", UNKNOWN_USER_ID);
        JsonpAssertions.assertNotEqualsIfPresent(task, "/responsibleUserId", UNKNOWN_USER_ID);
        JsonpAssertions.assertNotEmpty(task, "/affectedProjectId");
        JsonpAssertions.assertNotEmpty(task, "/affectedApplicationId");
        JsonpAssertions.assertNotEmptyIfPresent(task, "/affectedModule");
        JsonpAssertions.assertNotEmptyIfPresent(task, "/affectedResource");
        JsonpAssertions.assertIsLocalDateTime(task, "/createdAt");
        JsonpAssertions.assertNotEquals(task, "/createdBy", UNKNOWN_USER_ID);
        JsonpAssertions.assertIsLocalDateTime(task, "/lastModifiedAt");
        JsonpAssertions.assertNotEquals(task, "/lastModifiedBy", UNKNOWN_USER_ID);
    }

    private void assertAttributeMatches(String name, JsonObject expected, JsonObject actual) {
        assertEquals(expected.get(name), actual.get(name), String.format("Task.%s must match", name));
    }
}
