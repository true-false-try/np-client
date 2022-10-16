package com.example.client.controller;

import com.example.client.data.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${url.callback.document.create}")
    private String URL_CALLBACK_DOCUMENT_CREATE;
    @Value("${url.document.create}")
    private String URL_DOCUMENT_CREATE;

    @Scheduled(cron = "${cron.request.period}")
    public void generateClientRequest() {
        log.info("Client generated request");
        Document document = Document.builder()
                .number(new Random().nextInt(1000))
                .type(new Random().nextInt(5))
                .callbackUrl(URL_CALLBACK_DOCUMENT_CREATE)
                .build();
        restTemplate.postForObject(URL_DOCUMENT_CREATE, document, String.class);
    }

    @PostMapping("/callback/document/create")
    public ResponseEntity<String> createDocumentCallback(@RequestBody Document document) {
        log.info(document.toString());
        return ResponseEntity.ok().build();
    }
}
