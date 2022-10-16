package com.example.client.controller;

import com.example.client.data.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Random;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ClientController {
    private final RestTemplate restTemplate;

    @Scheduled(cron = "${cron.request.period}")
    public void getCreateDocument() {
        log.info(String.format("[%s] Client generated request", ClientController.class.getName()));
        Document document = Document.builder()
                .number(new Random().nextInt(1000))
                .type(new Random().nextInt(5))
                .callbackUrl("http://localhost:8080/callback/document/create")
                .build();
        restTemplate.postForObject("http://localhost:8030/document/create", document, String.class);
    }

    @PostMapping("/callback/document/create")
    public ResponseEntity<String> createOKStatus(@RequestBody String req) {
        log.info(req);
        log.info("HTTP Response Code ".concat(HttpStatus.OK.toString()));
        return ResponseEntity.ok().build();
    }
}
