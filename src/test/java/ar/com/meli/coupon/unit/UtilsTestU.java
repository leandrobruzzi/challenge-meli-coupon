package ar.com.meli.coupon.unit;

import ar.com.meli.coupon.config.CorrelationInterceptor;
import ar.com.meli.coupon.controller.HealthcheckController;
import ar.com.meli.coupon.utils.ConfigurationConstants;
import ar.com.meli.coupon.utils.Messages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@TestPropertySource(locations = ConfigurationConstants.TEST_PROPERTIES_URL)
public class UtilsTestU {

    @Autowired
    private HealthcheckController healthcheckController;

    @Autowired
    private Messages messages;

    @Autowired
    private CorrelationInterceptor correlationInterceptor;

    @Test
    void changeLanguage() {
        messages.changeLenguage(new Locale("es", "ES"));
    }

    @Test
    void constantsCoverage() {
        String testUrl = ConfigurationConstants.testCoverage();
        assertThat(testUrl).isEqualTo("/application-test.properties");
    }

    @Test
    void healthcheck() {
        String response = healthcheckController.healthCheck();
        assertThat(response).isEqualTo("OK");
    }

    @Test
    void correlationInterceptor() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        correlationInterceptor.preHandle(request, null, null);
        correlationInterceptor.afterCompletion(null, null, null, new Exception());
    }

}
