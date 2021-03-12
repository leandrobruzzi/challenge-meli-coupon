package ar.com.meli.coupon.functional;

import ar.com.meli.coupon.controller.CouponController;
import ar.com.meli.coupon.dto.CalculateCouponRequestDto;
import ar.com.meli.coupon.utils.ConfigurationConstants;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = ConfigurationConstants.TEST_PROPERTIES_URL)
public class CouponTestF {

    @Autowired
    private CouponController couponController;

    private List<String> items = new ArrayList<String>(Arrays.asList
            ("MLA-656039997", "MLA-839687279", "MLA-821267729",
             "MLA-863737328", "MLA-886815016", "MLA-865753229"));

    @Test
    void insufficientAmount() throws JSONException {
        CalculateCouponRequestDto calculateCouponRequestDto = new CalculateCouponRequestDto();
        calculateCouponRequestDto.setItem_ids(items);
        calculateCouponRequestDto.setAmount(5F);
        ResponseEntity<Object> response = couponController.calculateProductsForCoupon(calculateCouponRequestDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void exactCombination() throws JSONException {
        CalculateCouponRequestDto calculateCouponRequestDto = new CalculateCouponRequestDto();
        calculateCouponRequestDto.setItem_ids(items);
        calculateCouponRequestDto.setAmount(600F);
        ResponseEntity<Object> response = couponController.calculateProductsForCoupon(calculateCouponRequestDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void correctCombination() throws JSONException {
        CalculateCouponRequestDto calculateCouponRequestDto = new CalculateCouponRequestDto();
        calculateCouponRequestDto.setItem_ids(items);
        calculateCouponRequestDto.setAmount(453F);
        ResponseEntity<Object> response = couponController.calculateProductsForCoupon(calculateCouponRequestDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}