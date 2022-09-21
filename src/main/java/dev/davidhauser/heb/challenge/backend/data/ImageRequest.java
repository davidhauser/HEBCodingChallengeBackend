package dev.davidhauser.heb.challenge.backend.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageRequest {
    @JsonProperty
    private String imageUrl;

    @JsonProperty
    private String label;

    @JsonProperty
    private boolean imageDetection;

    @JsonCreator
    public ImageRequest(String imageUrl, String label) {
        this.imageUrl = imageUrl;
        this.label = label;
        this.imageDetection = true;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isImageDetection() {
        return this.imageDetection;
    }

    public void setImageDetection(boolean imageDetection) {
        this.imageDetection = imageDetection;
    }
}
