package com.nashTech;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class BaseTest {

    @BeforeClass
    public void setup() {
        // HARDCODE localhost - no environment variables, no complexity
        RestAssured.baseURI = "http://localhost:8090/api";

        System.out.println("🌐 Testing against: http://localhost:8090/api");
        System.out.println("☕ Java Version: " + System.getProperty("java.version"));

        // Wait for API - simple sleep
        System.out.println("⏳ Waiting 5 seconds for API...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testApiIsWorking() {
        System.out.println("🧪 Test: API Health Check");

        given()
                .when()
                .get("/tasks/health")
                .then()
                .statusCode(200);

        System.out.println("✅ API Health Check Passed!");
    }

    @Test
    public void testGetTasks() {
        System.out.println("🧪 Test: Get All Tasks");

        given()
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));

        System.out.println("✅ Get Tasks Passed!");
    }

    @Test
    public void testCreateSimpleTask() {
        System.out.println("🧪 Test: Create Task");

        String simpleTask = "{\"title\":\"Demo Task\",\"description\":\"Simple test\",\"completed\":false}";

        given()
                .contentType("application/json")
                .body(simpleTask)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("title", equalTo("Demo Task"));

        System.out.println("✅ Create Task Passed!");
    }
}