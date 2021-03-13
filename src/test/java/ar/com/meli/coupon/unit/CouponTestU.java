package ar.com.meli.coupon.unit;

import ar.com.meli.coupon.dto.CalculateCouponRequestDto;
import ar.com.meli.coupon.dto.CalculateCouponResponseDto;
import ar.com.meli.coupon.dto.ItemDto;
import ar.com.meli.coupon.exceptions.InsufficientAmountException;
import ar.com.meli.coupon.service.CouponService;
import ar.com.meli.coupon.utils.ConfigurationConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
@TestPropertySource(locations = ConfigurationConstants.TEST_PROPERTIES_URL)
public class CouponTestU {

    @Autowired
    private CouponService couponService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${meli.url.base}")
    private String meliUrlBase;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init(){
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void findIncorrectItem() throws JsonProcessingException, URISyntaxException {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(meliUrlBase + "/items/MLA656039997TEST")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString("{\"error\": \"resource not found\", \"message\": \"mensaje\"}")));

        CalculateCouponRequestDto calculateCouponRequestDto = new CalculateCouponRequestDto();
        calculateCouponRequestDto.setItem_ids(new ArrayList<String>(Arrays.asList
                ("MLA-656039997TEST")));
        calculateCouponRequestDto.setAmount(5F);
        try {
            couponService.calculateProducts(calculateCouponRequestDto);
        }catch (Exception e){
            assertThat("InsufficientAmountException").isEqualTo(e.getClass().getSimpleName());
        }

    }

    @Test
    void exactValueItem() throws InsufficientAmountException, URISyntaxException, JsonProcessingException {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(meliUrlBase + "/items/MLA656039998")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(new ItemDto("MLA656039998", "title", 141F, "site_id"))));

        CalculateCouponRequestDto calculateCouponRequestDto = new CalculateCouponRequestDto();
        calculateCouponRequestDto.setItem_ids(new ArrayList<String>(Arrays.asList
                ("MLA-656039998")));
        calculateCouponRequestDto.setAmount(141F);
        CalculateCouponResponseDto response = couponService.calculateProducts(calculateCouponRequestDto);
        assertThat(141F).isEqualTo(response.getTotal());
    }
}