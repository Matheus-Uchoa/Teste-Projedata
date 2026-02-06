package com.example.resource;

import com.example.dto.PageResponse;
import com.example.dto.product.ProductRequestDTO;
import com.example.dto.product.ProductResponseDTO;
import com.example.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    public PageResponse<ProductResponseDTO> listAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("search") String search,
            @QueryParam("sortBy") String sortBy,
            @QueryParam("sortDirection") String sortDirection) {
        return productService.findAll(page, size, search, sortBy, sortDirection);
    }

    @GET
    @Path("/{id}")
    public ProductResponseDTO getById(@PathParam("id") Long id) {
        return productService.findById(id);
    }

    @POST
    public Response create(@Valid ProductRequestDTO dto) {
        ProductResponseDTO created = productService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public ProductResponseDTO update(@PathParam("id") Long id, @Valid ProductRequestDTO dto) {
        return productService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        productService.delete(id);
        return Response.noContent().build();
    }
}
