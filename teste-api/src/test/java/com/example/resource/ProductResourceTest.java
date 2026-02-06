package com.example.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class ProductResourceTest {

    @Test
    void testListAllProducts() {
        given()
            .when().get("/products")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("content", notNullValue())
            .body("pageNumber", is(0))
            .body("pageSize", is(10));
    }

    @Test
    void testListAllProductsWithPagination() {
        given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .when().get("/products")
            .then()
            .statusCode(200)
            .body("pageSize", is(5));
    }

    @Test
    void testListAllProductsWithSearch() {
        given()
            .queryParam("search", "test")
            .when().get("/products")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void testCreateProduct() {
        String requestBody = """
            {
                "name": "Test Product",
                "value": 100.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/products")
            .then()
            .statusCode(201)
            .body("name", is("Test Product"))
            .body("value", is(100.0f))
            .body("id", notNullValue());
    }

    @Test
    void testCreateProductInvalidData() {
        String requestBody = """
            {
                "name": "",
                "value": -10.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/products")
            .then()
            .statusCode(400);
    }

    @Test
    void testGetProductByIdNotFound() {
        given()
            .when().get("/products/99999")
            .then()
            .statusCode(404);
    }

    @Test
    void testUpdateProductNotFound() {
        String requestBody = """
            {
                "name": "Updated Product",
                "value": 200.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().put("/products/99999")
            .then()
            .statusCode(404);
    }

    @Test
    void testDeleteProductNotFound() {
        given()
            .when().delete("/products/99999")
            .then()
            .statusCode(404);
    }
}
