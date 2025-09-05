package com.example.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class AiAnalysisResourceTest {

    @Test
    public void testHealthEndpoint() {
        given()
          .when().get("/api/ai/health")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON)
             .body("status", equalTo("UP"))
             .body("service", equalTo("AI Analysis API"));
    }

    @Test
    public void testGetSystemMessages() {
        given()
          .when().get("/api/ai/system-messages")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON)
             .body("SystemMessage1", notNullValue())
             .body("SystemMessage2", notNullValue());
    }

    @Test
    public void testAnalyzeWithoutRequiredParameters() {
        given()
          .contentType(ContentType.MULTIPART)
          .when().post("/api/ai/analyze")
          .then()
             .statusCode(400);
    }
}