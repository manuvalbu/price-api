package com.inditex.infrastructure.in.adapter;

import com.inditex.application.dto.PriceQuery;
import com.inditex.application.dto.PriceResponse;
import com.inditex.application.port.in.FindPriceUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RequestMapping("${rest.base-url}")
@RestController
@Validated
@Tag(name = "Price API", description = "Operations related to prices")
@RequiredArgsConstructor
public class PriceController {

    private final FindPriceUseCase findPriceService;

    @GetMapping("${rest.endpoints.price}")
    @Operation(
            summary = "Retrieve the applicable price",
            description = """
                    Returns the price applicable for a product and brand at a given date.
                    
                    The service searches all matching prices and returns the one with the highest priority
                    whose date range contains the provided application date.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Price found successfully. The response contains the applicable tariff, validity dates and amount.",
                    content = @Content(schema = @Schema(implementation = PriceResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No price exists for the given product, brand and date"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            )
    })
    public ResponseEntity<PriceResponse> searchPrice(
            @RequestParam("product_id")
            @Parameter(
                    description = "Unique identifier of the product",
                    example = "35455"
            )
            Long productId,

            @RequestParam("brand_id")
            @Parameter(
                    description = "Unique identifier of the brand",
                    example = "1"
            )
            Long brandId,

            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(
                    description = "Date and time when the price must be applied",
                    example = "2020-06-14T16:00:00"
            )
            LocalDateTime date) {
        log.info("searching price");
        PriceQuery priceQuery = PriceQuery.builder().productId(productId).brandId(brandId).date(date).build();
        PriceResponse priceResponse = findPriceService.execute(priceQuery);
        log.info("price retrieved successfully");
        return ResponseEntity.ok(priceResponse);
    }
}
