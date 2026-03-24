package com.aaronhuang.medintel;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import com.aaronhuang.medintel.service.RxNavClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RxNavClientTest {
    private static final String DEFAULT_BASE_URL = "http://localhost:4000/REST";

    @Test
    void lookupReturnsResults() {
        String baseUrl = System.getProperty("rxnav.base-url", DEFAULT_BASE_URL);
        RxNavClient rxNavClient = new RxNavClient(RestClient.builder(), baseUrl);

        List<String> ids = rxNavClient.getRxCuisByName("aspirin");
        assertFalse(ids.isEmpty(), "Expected RxCUI results from " + baseUrl);
        assertTrue(ids.contains("1191"), "Expected RxCUI 1191 for aspirin from " + baseUrl);
    }

}
