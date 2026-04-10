package com.shyamsarkar.buildingmaterials.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.shyamsarkar.buildingmaterials.dto.DistanceResult;
import com.shyamsarkar.buildingmaterials.exception.BadRequestException;

import jakarta.annotation.PostConstruct;

@Service
public class GoogleMapsService {

    private final String apiKey;

    public GoogleMapsService() {
        this.apiKey = System.getenv("GOOGLE_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("GOOGLE_API_KEY not found");
        }
    }

    @PostConstruct
    public void debugEnv() {
        System.out.println("MAIL_USERNAME=" + System.getenv("MAIL_USERNAME"));
        System.out.println("MAIL_PASSWORD=" + System.getenv("MAIL_PASSWORD"));
        System.out.println("GOOGLE_API_KEY=" + System.getenv("GOOGLE_API_KEY"));
    }

    private final RestTemplate restTemplate = new RestTemplate();

    public DistanceResult calculateDistance(
        double originLat,
        double originLng,
        double destinationLat,
        double destinationLng
) {
    try {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://maps.googleapis.com/maps/api/distancematrix/json")
                .queryParam("origins", originLat + "," + originLng)
                .queryParam("destinations", destinationLat + "," + destinationLng)
                .queryParam("units", "metric")
                .queryParam("key", apiKey)
                .toUriString();

        ResponseEntity<Map> response =
                restTemplate.getForEntity(url, Map.class);

        Map body = response.getBody();

        List rows = (List) body.get("rows");
        Map row = (Map) rows.get(0);
        List elements = (List) row.get("elements");
        Map element = (Map) elements.get(0);

        if (!"OK".equals(element.get("status"))) {
            throw new BadRequestException("Invalid coordinates");
        }

        Map distance = (Map) element.get("distance");
        Map duration = (Map) element.get("duration");

        double distanceKm =
                ((Number) distance.get("value")).doubleValue() / 1000;

        long durationMinutes =
                ((Number) duration.get("value")).longValue() / 60;

        return new DistanceResult(distanceKm, durationMinutes);

    } catch (Exception e) {
        throw new BadRequestException("Unable to calculate distance");
    }
}
}