package com.example.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class ProductionSuggestionResourceTest {

    @Test
    void testGetProductionSuggestions() {
        given()
            .when().get("/production-suggestions")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("content", notNullValue())
            .body("totalElements", notNullValue())
            .body("totalPages", notNullValue());
    }

    @Test
    void testProductionSuggestionsStructure() {
        given()
            .when().get("/production-suggestions")
            .then()
            .statusCode(200)
            .body("content", instanceOf(java.util.List.class))
            .body("pageNumber", instanceOf(Number.class))
            .body("pageSize", instanceOf(Number.class))
            .body("totalElements", instanceOf(Number.class))
            .body("totalPages", instanceOf(Number.class));
    }

    @Test
    void testGetProductionSuggestionsWithPagination() {
        given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .when().get("/production-suggestions")
            .then()
            .statusCode(200)
            .body("pageNumber", equalTo(0))
            .body("pageSize", equalTo(5));
    }

    @Test
    void testGetProductionSuggestionsWithSearch() {
        given()
            .queryParam("searchName", "test")
            .when().get("/production-suggestions")
            .then()
            .statusCode(200)
            .body("content", notNullValue());
    }
}
