package ar.com.meli.coupon.unit;

import ar.com.meli.coupon.dto.CalculateCouponRequestDto;
import ar.com.meli.coupon.dto.CalculateCouponResponseDto;
import ar.com.meli.coupon.exceptions.InsufficientAmountException;
import ar.com.meli.coupon.service.CouponService;
import ar.com.meli.coupon.utils.ConfigurationConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = ConfigurationConstants.TEST_PROPERTIES_URL)
public class CouponTestU {

    @Autowired
    private CouponService couponService;

    @Test
    void findIncorrectItem(){
        CalculateCouponRequestDto calculateCouponRequestDto = new CalculateCouponRequestDto();
        calculateCouponRequestDto.setItem_ids(new ArrayList<String>(Arrays.asList
                ("SARASA-MLA-656039997")));
        calculateCouponRequestDto.setAmount(5F);
        try {
            couponService.calculateProducts(calculateCouponRequestDto);
        }catch (Exception e){
            assertThat("InsufficientAmountException").isEqualTo(e.getClass().getSimpleName());
        }

    }

    @Test
    void exactValueItem() throws InsufficientAmountException {
        CalculateCouponRequestDto calculateCouponRequestDto = new CalculateCouponRequestDto();
        calculateCouponRequestDto.setItem_ids(new ArrayList<String>(Arrays.asList
                ("MLA-656039997")));
        calculateCouponRequestDto.setAmount(141F);
        CalculateCouponResponseDto response = couponService.calculateProducts(calculateCouponRequestDto);
        assertThat(141F).isEqualTo(response.getTotal());
    }
}