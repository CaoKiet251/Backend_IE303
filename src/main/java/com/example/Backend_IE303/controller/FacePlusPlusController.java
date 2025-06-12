package com.example.Backend_IE303.controller;

import io.jsonwebtoken.io.IOException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faceplusplus")
public class FacePlusPlusController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String FACEPP_API = "https://api-us.faceplusplus.com/facepp/v3/faceset/create";
    private final String API_KEY = "HPRXgJFOBJZDnte-QfYpMgHwKx82kFX0";
    private final String API_SECRET = "EABo3rKKhCdaIcln-ooohkAMUTndJW66";

    @PostMapping("/create-faceset")
    public ResponseEntity<?> createFaceSet(@RequestBody Map<String, String> request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "Mozilla/5.0"); // Avoid being blocked

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("api_key", API_KEY);
        params.add("api_secret", API_SECRET);

        // Optional fields with null check
        if (request.containsKey("display_name")) {
            params.add("display_name", request.get("display_name"));
        }
        if (request.containsKey("outer_id")) {
            params.add("outer_id", request.get("outer_id"));
        }
        if (request.containsKey("tags")) {
            params.add("tags", request.get("tags"));
        }
        if (request.containsKey("face_tokens")) {
            params.add("face_tokens", request.get("face_tokens"));
        }
        if (request.containsKey("user_data")) {
            params.add("user_data", request.get("user_data"));
        }
        if (request.containsKey("force_merge")) {
            params.add("force_merge", request.get("force_merge"));
        }

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(FACEPP_API, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            // Return actual error response from Face++
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/detect-face")
    public ResponseEntity<?> detectFace(@RequestBody Map<String, String> request) {
        String imageBase64 = request.get("image_base64");
        String imageUrl = request.get("image_url");

        if ((imageBase64 == null || imageBase64.isEmpty()) && (imageUrl == null || imageUrl.isEmpty())) {
            return ResponseEntity.badRequest().body("You must provide either image_base64 or image_url.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("api_key", API_KEY);
        params.add("api_secret", API_SECRET);

        if (imageBase64 != null && !imageBase64.isEmpty()) {
            params.add("image_base64", imageBase64);
        } else {
            params.add("image_url", imageUrl);
        }

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api-us.faceplusplus.com/facepp/v3/detect",
                    entity,
                    String.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/add-face")
    public ResponseEntity<?> addFaceToFaceSet(@RequestBody Map<String, String> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "Mozilla/5.0");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("api_key", API_KEY);
        params.add("api_secret", API_SECRET);

        if (request.containsKey("outer_id")) {
            params.add("outer_id", request.get("outer_id"));
        } else if (request.containsKey("faceset_token")) {
            params.add("faceset_token", request.get("faceset_token"));
        } else {
            return ResponseEntity.badRequest().body("outer_id or faceset_token is required.");
        }

        if (!request.containsKey("face_tokens") || request.get("face_tokens").isEmpty()) {
            return ResponseEntity.badRequest().body("face_tokens is required.");
        }
        params.add("face_tokens", request.get("face_tokens"));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api-us.faceplusplus.com/facepp/v3/faceset/addface",
                    entity,
                    String.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/register-face")
    public ResponseEntity<?> registerFace(@RequestBody Map<String, String> request) {
        String imageBase64 = request.get("image_base64");
        String imageUrl = request.get("image_url");
        String outerId = request.get("outer_id");
        String userId = request.get("user_id");

        if ((imageBase64 == null || imageBase64.isEmpty()) && (imageUrl == null || imageUrl.isEmpty())) {
            return ResponseEntity.badRequest().body("You must provide either image_base64 or image_url.");
        }

        if (outerId == null || outerId.isEmpty()) {
            return ResponseEntity.badRequest().body("outer_id is required.");
        }

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("user_id is required.");
        }

        // Step 1: Detect face and get face_token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> detectParams = new LinkedMultiValueMap<>();
        detectParams.add("api_key", API_KEY);
        detectParams.add("api_secret", API_SECRET);

        if (imageBase64 != null && !imageBase64.isEmpty()) {
            detectParams.add("image_base64", imageBase64);
        } else {
            detectParams.add("image_url", imageUrl);
        }

        HttpEntity<MultiValueMap<String, String>> detectEntity = new HttpEntity<>(detectParams, headers);

        try {
            ResponseEntity<Map> detectResponse = restTemplate.postForEntity(
                    "https://api-us.faceplusplus.com/facepp/v3/detect", detectEntity, Map.class);

            List<Map<String, Object>> faces = (List<Map<String, Object>>) detectResponse.getBody().get("faces");
            if (faces == null || faces.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No face detected in the image.");
            }

            String faceToken = (String) faces.get(0).get("face_token");

            // Step 2: Add face_token to FaceSet
            MultiValueMap<String, String> addParams = new LinkedMultiValueMap<>();
            addParams.add("api_key", API_KEY);
            addParams.add("api_secret", API_SECRET);
            addParams.add("outer_id", outerId);
            addParams.add("face_tokens", faceToken);

            HttpEntity<MultiValueMap<String, String>> addEntity = new HttpEntity<>(addParams, headers);
            ResponseEntity<String> addResponse = restTemplate.postForEntity(
                    "https://api-us.faceplusplus.com/facepp/v3/faceset/addface", addEntity, String.class);

            // Step 3: Set user_id for face_token
            MultiValueMap<String, String> userIdParams = new LinkedMultiValueMap<>();
            userIdParams.add("api_key", API_KEY);
            userIdParams.add("api_secret", API_SECRET);
            userIdParams.add("face_token", faceToken);
            userIdParams.add("user_id", userId);

            HttpEntity<MultiValueMap<String, String>> userIdEntity = new HttpEntity<>(userIdParams, headers);
            ResponseEntity<String> setUserIdResponse = restTemplate.postForEntity(
                    "https://api-us.faceplusplus.com/facepp/v3/face/setuserid", userIdEntity, String.class);

            return ResponseEntity.ok(Map.of(
                    "message", "Face registered successfully",
                    "face_token", faceToken,
                    "faceset_response", addResponse.getBody(),
                    "set_user_id_response", setUserIdResponse.getBody()
            ));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    @PostMapping("/search-face")
    public ResponseEntity<?> searchFace(@RequestParam("image") MultipartFile image) {
        try {
            String url = "https://api-us.faceplusplus.com/facepp/v3/search";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("api_key", API_KEY);
            body.add("api_secret", API_SECRET);
            body.add("outer_id", "my_faceset");
            body.add("image_file", new MultipartInputStreamFileResource(image.getInputStream(), image.getOriginalFilename()));

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


}

