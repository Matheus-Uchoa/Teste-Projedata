package com.example.resource;

import com.example.dto.PageResponse;
import com.example.dto.productionsuggestion.ProductionSuggestionResponseDTO;
import com.example.service.ProductionSuggestionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/production-suggestions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductionSuggestionResource {

    @Inject
    ProductionSuggestionService productionSuggestionService;

    @GET
    public PageResponse<ProductionSuggestionResponseDTO> getProductionSuggestions(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("searchName") String searchName,
            @QueryParam("sortDirection") String sortDirection) {
        return productionSuggestionService.getProductionSuggestions(page, size, searchName, sortDirection);
    }
}
