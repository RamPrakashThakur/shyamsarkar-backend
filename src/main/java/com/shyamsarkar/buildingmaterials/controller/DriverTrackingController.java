package com.shyamsarkar.buildingmaterials.controller;

import com.shyamsarkar.buildingmaterials.dto.DriverLocationDto;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DriverTrackingController {

    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger logger =
        LoggerFactory.getLogger(DriverTrackingController.class);

    @MessageMapping("/driver/location")
    public void receiveLocation(DriverLocationDto dto) {

        

logger.info("Driver location received: {} {} {}",
        dto.getOrderId(),
        dto.getLat(),
        dto.getLng());

        messagingTemplate.convertAndSend(
                "/topic/location/" + dto.getOrderId(),
                dto
        );
    }
}