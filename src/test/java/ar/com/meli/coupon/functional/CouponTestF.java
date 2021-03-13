package ar.com.meli.coupon.functional;

import ar.com.meli.coupon.controller.CouponController;
import ar.com.meli.coupon.dto.CalculateCouponRequestDto;
import ar.com.meli.coupon.dto.ItemDto;
import ar.com.meli.coupon.utils.ConfigurationConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
@TestPropertySource(locations = ConfigurationConstants.TEST_PROPERTIES_URL)
public class CouponTestF {

    @Autowired
    private CouponController couponController;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${meli.url.base}")
    private String meliUrlBase;

    private MockRestServiceServer mockServer;

    //Creo dos listas distintas para poder usar la lista de items y al mismo tiempo mockear los valores
    private List<String> items = new ArrayList<String>(Arrays.asList
            ("MLA-656039997", "MLA-839687279", "MLA-821267729", "MLA-863737328", "MLA-886815016", "MLA-865753229"));
    private List<Float> values = new ArrayList<Float>(Arrays.asList(300F, 205F, 108F, 55F, 165F, 85F));

    @BeforeEach
    public void init() throws URISyntaxException, JsonProcessingException {
        //Mockeo el servicio para asegurarme que no van a cambiar los valores y asegurar la correcta ejecucion de los test
        mockServer = MockRestServiceServer.createServer(restTemplate);

        for (int i=0; i<items.size(); i++) {
            String id = items.get(i).replace("-", "");
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(meliUrlBase + "/items/" + id)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                            .body(objectMapper.writeValueAsString(new ItemDto(id, "title", values.get(i), "site_id"))));
        }
    }

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
        calculateCouponRequestDto.setAmount(505F);
        ResponseEntity<Object> response = couponController.calculateProductsForCoupon(calculateCouponRequestDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void correctCombination() throws JSONException {
        CalculateCouponRequestDto calculateCouponRequestDto = new CalculateCouponRequestDto();
        calculateCouponRequestDto.setItem_ids(items);
        calculateCouponRequestDto.setAmount(456F);
        ResponseEntity<Object> response = couponController.calculateProductsForCoupon(calculateCouponRequestDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}