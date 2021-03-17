package ar.com.meli.coupon.controller;

import ar.com.meli.coupon.dto.CalculateCouponRequestDto;
import ar.com.meli.coupon.dto.CalculateCouponResponseDto;
import ar.com.meli.coupon.dto.CombinationDto;
import ar.com.meli.coupon.dto.ResponseErrorDto;
import ar.com.meli.coupon.exceptions.InsufficientAmountException;
import ar.com.meli.coupon.service.CouponService;
import ar.com.meli.coupon.utils.Messages;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try {
            return new ResponseEntity<>(couponService.calculateProducts(calculateCouponRequestDto), HttpStatus.OK);
        } catch (InsufficientAmountException e) {
            logger.error(messages.get("couponcontroller.insufficient.amount.code"));
            return new ResponseEntity(new ResponseErrorDto(
                    messages.get("couponcontroller.insufficient.amount.code"),
                    messages.get("couponcontroller.insufficient.amount.message")), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Expongo servicio para hacer pruebas de carga sobre la operacion de calcular")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna listado ideal de items a comprar",
                    content = @Content(schema = @Schema(implementation = CalculateCouponResponseDto.class)) ),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ResponseErrorDto.class)) )
    })
    @PostMapping("/stress-test")
    public ResponseEntity<Object> stressTest() throws InsufficientAmountException, JsonProcessingException {
        CombinationDto maxCombination = new CombinationDto();
        Map<String, Float> items = new HashMap<>();
        //Genero 50 items con valores random menores al amount
        for (int i = 0; i<50; i++){
            items.put("MLA"+String.valueOf(i), (float) (Math.round( (Math.random()*(600-1)+1) * 100 ) / 100d) );
        }

        List<String> itemsResponse = couponService.stressTest(items, 600F, maxCombination);
        return new ResponseEntity<>(itemsResponse, HttpStatus.OK);
    }

}
