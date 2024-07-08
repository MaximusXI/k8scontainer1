package com.example.entry.util;

import com.example.entry.exception.ServiceException;
import com.example.entry.model.FileRequest;
import com.example.entry.model.Request;
import com.example.entry.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Harshil Makwana
 */
@Slf4j
@Configuration
public class checkUtil {
    //final static String uri = "http://localhost:8080/fileService/welcome";
//
//    @Value("${fileLocation:}")
//    private static String fileLocation;

    public static ResponseEntity<Response> checkAndGetResultFromOtherContainer(Request request, String uri,String fileLocation) throws Exception {
        if (Objects.isNull(request.getFile())) {
            log.error("Exception occurred in checkUtil inside the entry container. The file name is null");
            throw new ServiceException(HttpStatus.BAD_REQUEST, request.getFile(), "Invalid JSON input.");
        }
        String fileName = request.getFile();
        try {
            log.info("Validating if the file exists in the entry container");
            InputStream in = new FileInputStream(fileLocation+ fileName);
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException Exception occurred inside the entry container : {}", e.getMessage());
            throw new ServiceException(HttpStatus.NOT_FOUND, fileName, "File not found.");
        }
        try {
            log.info("Making the rest call to the second container for the file data");
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Request> requestEntity = new HttpEntity<Request>(request);
            ResponseEntity<Response> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Response.class);
            return responseEntity;
        } catch (ResourceAccessException e) {
            log.error("Exception occurred in checkUtil in the entry container. Resource Access Exception : {}", e.getMessage());
            throw new ServiceException(HttpStatus.NOT_FOUND, request.getFile(), "Second Service is unreachable");
        } catch (Exception e) {
            log.error("General Exception occurred in checkUtil. Exception : {}", e.getMessage());
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, request.getFile(), e.getMessage().substring(e.getMessage().indexOf('"') + 1, e.getMessage().lastIndexOf('"')));
        }

    }

    public static Map<String, String> storeFile(FileRequest request,String fileLocation) throws ServiceException {
        log.info("Entered into storeFile method inside the checkUtil");
        if(Objects.isNull(request.getFile())){
            throw new ServiceException(HttpStatus.BAD_REQUEST, request.getFile(),"Invalid JSON input.");
        }
        String fileName = request.getFile();
        String data = request.getData();
        log.info("The fileLocation in properties File is: {}", fileLocation);
        File file = new File(fileLocation + fileName);
        try {
            StringBuilder stringBuilder = new StringBuilder();
            FileWriter fileWriter = new FileWriter(fileLocation + fileName);
            String[] rows = data.split("\n");
            for(String row: rows){
                String[] values = row.split(",");
                stringBuilder.append(values[0].trim()).append(",").append(values[1].trim()).append("\n");
            }
            fileWriter.write(stringBuilder.toString());
            fileWriter.close();
            log.info("Successfully stored the file in location: {}",fileLocation);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("file", request.getFile());
            successResponse.put("message", "Success.");
            return successResponse;
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("file", request.getFile());
            errorResponse.put("error", "Error while storing the file to the storage.");
            return errorResponse;
        }
    }
}
