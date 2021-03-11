package ar.com.meli.cupon.controller;

import ar.com.meli.cupon.dto.CalculateCouponRequestDto;
import ar.com.meli.cupon.dto.CalculateCouponResponseDto;
import ar.com.meli.cupon.dto.ResponseErrorDto;
import ar.com.meli.cupon.service.CouponService;
import ar.com.meli.cupon.utils.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;

    @Autowired
    private Messages messages;

    @Operation(summary = "Se envia la lista de itemsId y el monto del cupon. Retorna los items a comprar por el usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna listado ideal de items a comprar",
                    content = @Content(schema = @Schema(implementation = CalculateCouponResponseDto.class)) ),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ResponseErrorDto.class)) )
    })
    @PostMapping
    public ResponseEntity<Object> calculateProductsForCoupon(@RequestBody CalculateCouponRequestDto calculateCouponRequestDto) {
        //couponService.metodo
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
