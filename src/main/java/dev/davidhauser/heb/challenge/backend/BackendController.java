package dev.davidhauser.heb.challenge.backend;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.davidhauser.heb.challenge.backend.data.ImageRequest;
import dev.davidhauser.heb.challenge.backend.data.imagga.ImaggaResponse;
import dev.davidhauser.heb.challenge.backend.data.imagga.ImaggaTag;
import dev.davidhauser.heb.challenge.backend.entity.ImageRecord;
import dev.davidhauser.heb.challenge.backend.service.ImageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class BackendController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackendController.class);

    private static final String IMAGGA_ENDPOINT_BASE = "https://api.imagga.com/v2/tags?image_url=";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int IMAGGA_CONFIDENCE_THRESHOLD = 30;

    @Value("${imagga.credentials}")
    private String imaggaCredentials;

    private final ImageService imageService;

    @Autowired
    public BackendController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images")
    public ResponseEntity<Object> getAllWithTags(@RequestParam(required = false) String objects) {
        if (objects == null) {
            List<ImageRecord> imageRecords = this.imageService.getAll();
            LOGGER.info("/images was called without specified objects, returning {} records!", imageRecords.size());
            return ResponseEntity.ok(imageRecords);
        } else {
            String[] objectsToRetrieve = objects.split(",");
            LOGGER.info("objectsToRetrieve: {}", Arrays.asList(objectsToRetrieve));
            List<ImageRecord> imageRecords = this.imageService.getByObjects(objectsToRetrieve);
            LOGGER.info("/images was called for objects {}, returning {} records!",
                    Arrays.asList(objectsToRetrieve), imageRecords.size());
            return ResponseEntity.ok(imageRecords);
        }
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<Object> getByImageId(@PathVariable Long imageId) {
        Optional<ImageRecord> imageRecordOptional = this.imageService.getByImageId(imageId);

        ResponseEntity<Object> response;
        if (imageRecordOptional.isPresent()) {
            LOGGER.info("/images/{} found a record to return!", imageId);
            response = ResponseEntity.ok(imageRecordOptional.get());
        } else {
            LOGGER.warn("/images/{} did not find a record to return!", imageId);
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @PostMapping("/images")
    public ResponseEntity<Object> postImage(@RequestBody ImageRequest imageRequest) {
        ImageRecord imageRecord = new ImageRecord();
        imageRecord.setImageUrl(imageRequest.getImageUrl());

        List<String> objects = new ArrayList<>();
        if (imageRequest.isImageDetection()) {
            try {
                objects = getObjectsForImageUrl(imageRequest.getImageUrl());
            } catch (IOException ioe) {
                LOGGER.error(ioe.toString());
            }
        }
        imageRecord.setObjects(objects);

        if (imageRequest.getLabel() != null) {
            imageRecord.setLabel(imageRequest.getLabel());
        } else if (objects.size() > 0){
            imageRecord.setLabel("An image that might contain: " + objects.get(0));
        } else {
            imageRecord.setLabel("An image for which image detection wasn't run.");
        }

        return ResponseEntity.ok(this.imageService.add(imageRecord));
    }

    private List<String> getObjectsForImageUrl(String imageUrl) throws IOException {
        String url = IMAGGA_ENDPOINT_BASE + imageUrl;
        LOGGER.info("Sending 'GET' request to Imagga at URL {}", url);

        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        String basicAuth = Base64.getEncoder().encodeToString(imaggaCredentials.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();
        LOGGER.info("Imagga request responded with code {}", responseCode);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String jsonResponse = connectionInput.readLine();
        LOGGER.info("Imagga response: {}", jsonResponse);
        connectionInput.close();

        ImaggaResponse imaggaResponse = OBJECT_MAPPER.readValue(jsonResponse, ImaggaResponse.class);
        List<ImaggaTag> imaggaTags = imaggaResponse.getResult().getTags();

        List<String> objectTags = new ArrayList<>();
        for (ImaggaTag imaggaTag : imaggaTags) {
            if (imaggaTag.getConfidence() > IMAGGA_CONFIDENCE_THRESHOLD) {
                objectTags.add(imaggaTag.getTag().getEn());
            }
        }
        return objectTags;
    }
}
