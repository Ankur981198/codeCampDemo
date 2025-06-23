package com.nashTech;

import org.testng.annotations.Test;
import utils.TestDataBuilder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TaskApiTest extends BaseTest {

    @Test(priority = 1, groups = {"smoke"})
    public void testGetAllTasks() {
        System.out.println("🧪 Test: Get All Tasks");

        given()
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].title", notNullValue())
                .body("[0].id", notNullValue());

        System.out.println("✅ Retrieved tasks successfully");
    }

    @Test(priority = 2, groups = {"smoke"})
    public void testGetSingleTask() {
        System.out.println("🧪 Test: Get Single Task");

        given()
                .when()
                .get("/tasks/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", notNullValue())
                .body("description", notNullValue());

        System.out.println("✅ Retrieved single task successfully");
    }

    @Test(priority = 3, groups = {"api"})
    public void testCreateTask() {
        System.out.println("🧪 Test: Create New Task");

        String taskJson = TestDataBuilder.createTask()
                .withTitle("Docker Demo Task")
                .withDescription("Created during Docker testing demo")
                .toJson();

        given()
                .contentType("application/json")
                .body(taskJson)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("title", equalTo("Docker Demo Task"))
                .body("description", equalTo("Created during Docker testing demo"))
                .body("id", notNullValue())
                .body("completed", equalTo(false));

        System.out.println("✅ Created task successfully");
    }

    @Test(priority = 4, groups = {"api"})
    public void testUpdateTask() {
        System.out.println("🧪 Test: Update Task");

        String updateJson = TestDataBuilder.createTask()
                .withTitle("Updated Docker Task")
                .withDescription("Updated during testing")
                .withCompleted(true)
                .toJson();

        given()
                .contentType("application/json")
                .body(updateJson)
                .when()
                .put("/tasks/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Docker Task"))
                .body("completed", equalTo(true));

        System.out.println("✅ Updated task successfully");
    }

    @Test(priority = 5, groups = {"api"})
    public void testDeleteTask() {
        System.out.println("🧪 Test: Delete Task");

        given()
                .when()
                .delete("/tasks/2")
                .then()
                .statusCode(204);

        System.out.println("✅ Deleted task successfully");
    }

    @Test(groups = {"negative"})
    public void testGetNonExistentTask() {
        System.out.println("🧪 Test: Get Non-existent Task");

        given()
                .when()
                .get("/tasks/999")
                .then()
                .statusCode(404);

        System.out.println("✅ Handled non-existent task correctly");
    }

    @Test(groups = {"api"})
    public void testTaskLifecycle() {
        System.out.println("🧪 Test: Complete Task Lifecycle");

        // Create
        String createJson = TestDataBuilder.createTask()
                .withTitle("Lifecycle Test Task")
                .withDescription("Testing full CRUD operations")
                .toJson();

        String taskId = given()
                .contentType("application/json")
                .body(createJson)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .extract()
                .path("id")
                .toString();

        // Read
        given()
                .when()
                .get("/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("title", equalTo("Lifecycle Test Task"));

        // Update
        String updateJson = TestDataBuilder.createTask()
                .withTitle("Updated Lifecycle Task")
                .withCompleted(true)
                .toJson();

        given()
                .contentType("application/json")
                .body(updateJson)
                .when()
                .put("/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("completed", equalTo(true));

        // Delete
        given()
                .when()
                .delete("/tasks/" + taskId)
                .then()
                .statusCode(204);

        // Verify deletion
        given()
                .when()
                .get("/tasks/" + taskId)
                .then()
                .statusCode(404);

        System.out.println("✅ Complete lifecycle test passed");
    }
}