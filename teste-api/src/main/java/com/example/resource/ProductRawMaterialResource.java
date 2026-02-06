package com.example.resource;

import com.example.dto.PageResponse;
import com.example.dto.productrawmaterial.ProductRawMaterialRequestDTO;
import com.example.dto.productrawmaterial.ProductRawMaterialResponseDTO;
import com.example.service.ProductRawMaterialService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products/{productId}/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRawMaterialResource {

    @Inject
    ProductRawMaterialService productRawMaterialService;

    @GET
    public PageResponse<ProductRawMaterialResponseDTO> listByProduct(
            @PathParam("productId") Long productId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        return productRawMaterialService.findByProductId(productId, page, size);
    }

    @GET
    @Path("/{rawMaterialId}")
    public ProductRawMaterialResponseDTO getByProductAndRawMaterial(
            @PathParam("productId") Long productId,
            @PathParam("rawMaterialId") Long rawMaterialId) {
        return productRawMaterialService.findByProductIdAndRawMaterialId(productId, rawMaterialId);
    }

    @POST
    public Response addRawMaterial(
            @PathParam("productId") Long productId,
            @Valid ProductRawMaterialRequestDTO dto) {
        ProductRawMaterialResponseDTO created = productRawMaterialService.addRawMaterialToProduct(productId, dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{rawMaterialId}")
    public ProductRawMaterialResponseDTO updateQuantity(
            @PathParam("productId") Long productId,
            @PathParam("rawMaterialId") Long rawMaterialId,
            @Valid ProductRawMaterialRequestDTO dto) {
        return productRawMaterialService.updateQuantity(productId, rawMaterialId, dto);
    }

    @DELETE
    @Path("/{rawMaterialId}")
    public Response removeRawMaterial(
            @PathParam("productId") Long productId,
            @PathParam("rawMaterialId") Long rawMaterialId) {
        productRawMaterialService.removeRawMaterialFromProduct(productId, rawMaterialId);
        return Response.noContent().build();
    }
}
