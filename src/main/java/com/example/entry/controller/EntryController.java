package com.example.entry.controller;

import com.example.entry.exception.ServiceException;
import com.example.entry.model.FileRequest;
import com.example.entry.model.Request;
import com.example.entry.model.Response;
import com.example.entry.util.checkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * @author Harshil Makwana
 */
@RestController
@RequestMapping("/")
@Slf4j
public class EntryController {

    @Value("${hostUrl}")
    private String secondServiceUrl;

    @Value("${fileLocation}")
    private String fileLocation;

    @PostMapping("store-file")
    public ResponseEntity<Map<String,String>> storeFileData(@RequestBody FileRequest request) throws Exception {
        System.out.println(secondServiceUrl);
        log.info("A simple log 10");
        log.info("Inside the EntryController in the store-file endpoint");
        log.info("The second service url is: {}", secondServiceUrl);
        Map<String,String> result = checkUtil.storeFile(request,fileLocation);
        return ResponseEntity.ok(result);
    }

    @PostMapping("calculate")
    public ResponseEntity<Response> checkAndGetFileData(@RequestBody Request request) throws Exception {
        System.out.println(secondServiceUrl);
        log.info("Inside the EntryController in the calculate endpoint");
        log.info("The second service url is: {}", secondServiceUrl);
        ResponseEntity<Response> responseEntity = checkUtil.checkAndGetResultFromOtherContainer(request, secondServiceUrl,fileLocation);
        return responseEntity;
    }
}
