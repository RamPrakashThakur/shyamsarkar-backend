package com.shyamsarkar.buildingmaterials.controller;

import com.shyamsarkar.buildingmaterials.dto.DriverLocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DriverTrackingController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/driver/location")
    public void receiveLocation(DriverLocationDto dto) {

        messagingTemplate.convertAndSend(
                "/topic/location/" + dto.getOrderId(),
                dto
        );
    }
}