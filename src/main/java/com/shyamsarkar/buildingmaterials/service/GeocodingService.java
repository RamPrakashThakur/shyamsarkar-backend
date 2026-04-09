package com.shyamsarkar.buildingmaterials.service;

import com.shyamsarkar.buildingmaterials.dto.GeocodeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {

    @Value("${google.maps.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public GeocodeResult geocode(String address) {

        String url =
            "https://maps.googleapis.com/maps/api/geocode/json" +
            "?address=" + UriUtils.encode(address + ", Neemuch, MP",
                    StandardCharsets.UTF_8) +
            "&key=" + apiKey;

        Map res = restTemplate.getForObject(url, Map.class);
        List results = (List) res.get("results");

        if (results.isEmpty())
            throw new RuntimeException("Invalid address");

        Map first = (Map) results.get(0);
        Map loc = (Map) ((Map) first.get("geometry")).get("location");

        double lat = ((Number) loc.get("lat")).doubleValue();
        double lng = ((Number) loc.get("lng")).doubleValue();

        List comps = (List) first.get("address_components");

        String district = "", state = "";

        for (Object o : comps) {
            Map c = (Map) o;
            List types = (List) c.get("types");

            if (types.contains("administrative_area_level_2"))
                district = (String) c.get("long_name");

            if (types.contains("administrative_area_level_1"))
                state = (String) c.get("long_name");
        }

        if (!district.equalsIgnoreCase("Neemuch") ||
            !state.equalsIgnoreCase("Madhya Pradesh")) {
            throw new RuntimeException("Delivery only in Neemuch");
        }

        return new GeocodeResult(lat, lng, district, state);
    }
}