package com.example.resource;

import com.example.dto.PageResponse;
import com.example.dto.rawmaterial.RawMaterialRequestDTO;
import com.example.dto.rawmaterial.RawMaterialResponseDTO;
import com.example.service.RawMaterialService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialResource {

    @Inject
    RawMaterialService rawMaterialService;

    @GET
    public PageResponse<RawMaterialResponseDTO> listAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("search") String search,
            @QueryParam("sortBy") String sortBy,
            @QueryParam("sortDirection") String sortDirection) {
        return rawMaterialService.findAll(page, size, search, sortBy, sortDirection);
    }

    @GET
    @Path("/{id}")
    public RawMaterialResponseDTO getById(@PathParam("id") Long id) {
        return rawMaterialService.findById(id);
    }

    @POST
    public Response create(@Valid RawMaterialRequestDTO dto) {
        RawMaterialResponseDTO created = rawMaterialService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public RawMaterialResponseDTO update(@PathParam("id") Long id, @Valid RawMaterialRequestDTO dto) {
        return rawMaterialService.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        rawMaterialService.delete(id);
        return Response.noContent().build();
    }
}
