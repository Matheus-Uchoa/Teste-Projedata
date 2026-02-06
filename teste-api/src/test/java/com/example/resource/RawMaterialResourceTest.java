package com.example.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class RawMaterialResourceTest {

    @Test
    void testGetAllRawMaterials() {
        given()
            .when().get("/raw-materials")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("content", notNullValue())
            .body("totalElements", notNullValue())
            .body("totalPages", notNullValue());
    }

    @Test
    void testGetAllRawMaterialsWithPagination() {
        given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .when().get("/raw-materials")
            .then()
            .statusCode(200)
            .body("pageNumber", equalTo(0))
            .body("pageSize", equalTo(5))
            .body("content", instanceOf(java.util.List.class));
    }

    @Test
    void testGetAllRawMaterialsWithSearch() {
        given()
            .queryParam("search", "test")
            .when().get("/raw-materials")
            .then()
            .statusCode(200)
            .body("content", notNullValue());
    }

    @Test
    void testGetAllRawMaterialsWithSort() {
        given()
            .queryParam("sortBy", "stockQuantity")
            .queryParam("sortDirection", "desc")
            .when().get("/raw-materials")
            .then()
            .statusCode(200)
            .body("content", notNullValue());
    }

    @Test
    void testCreateRawMaterial() {
        String requestBody = """
            {
                "name": "Test Material",
                "stockQuantity": 100.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/raw-materials")
            .then()
            .statusCode(201)
            .body("name", is("Test Material"))
            .body("stockQuantity", is(100.00f));
    }

    @Test
    void testCreateRawMaterialWithInvalidData() {
        String requestBody = """
            {
                "name": "",
                "stockQuantity": -10.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/raw-materials")
            .then()
            .statusCode(400);
    }

    @Test
    void testGetRawMaterialById() {
        // First create a raw material
        String requestBody = """
            {
                "name": "Material for Get Test",
                "stockQuantity": 50.00
            }
            """;

        Integer id = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/raw-materials")
            .then()
            .statusCode(201)
            .extract().path("id");

        // Then get it by ID
        given()
            .when().get("/raw-materials/" + id)
            .then()
            .statusCode(200)
            .body("id", is(id))
            .body("name", is("Material for Get Test"))
            .body("stockQuantity", is(50.00f));
    }

    @Test
    void testGetRawMaterialByIdNotFound() {
        given()
            .when().get("/raw-materials/99999")
            .then()
            .statusCode(404);
    }

    @Test
    void testUpdateRawMaterial() {
        // First create a raw material
        String createBody = """
            {
                "name": "Material to Update",
                "stockQuantity": 75.00
            }
            """;

        Integer id = given()
            .contentType(ContentType.JSON)
            .body(createBody)
            .when().post("/raw-materials")
            .then()
            .statusCode(201)
            .extract().path("id");

        // Then update it
        String updateBody = """
            {
                "name": "Updated Material",
                "stockQuantity": 150.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(updateBody)
            .when().put("/raw-materials/" + id)
            .then()
            .statusCode(200)
            .body("name", is("Updated Material"))
            .body("stockQuantity", is(150.00f));
    }

    @Test
    void testUpdateRawMaterialNotFound() {
        String updateBody = """
            {
                "name": "Non-existent Material",
                "stockQuantity": 100.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(updateBody)
            .when().put("/raw-materials/99999")
            .then()
            .statusCode(404);
    }

    @Test
    void testDeleteRawMaterial() {
        // First create a raw material
        String createBody = """
            {
                "name": "Material to Delete",
                "stockQuantity": 25.00
            }
            """;

        Integer id = given()
            .contentType(ContentType.JSON)
            .body(createBody)
            .when().post("/raw-materials")
            .then()
            .statusCode(201)
            .extract().path("id");

        // Then delete it
        given()
            .when().delete("/raw-materials/" + id)
            .then()
            .statusCode(204);

        // Verify it's deleted
        given()
            .when().get("/raw-materials/" + id)
            .then()
            .statusCode(404);
    }

    @Test
    void testDeleteRawMaterialNotFound() {
        given()
            .when().delete("/raw-materials/99999")
            .then()
            .statusCode(404);
    }

    @Test
    void testRawMaterialResponseStructure() {
        given()
            .when().get("/raw-materials")
            .then()
            .statusCode(200)
            .body("content", instanceOf(java.util.List.class))
            .body("pageNumber", instanceOf(Number.class))
            .body("pageSize", instanceOf(Number.class))
            .body("totalElements", instanceOf(Number.class))
            .body("totalPages", instanceOf(Number.class));
    }
}
