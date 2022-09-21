package dev.davidhauser.heb.challenge.backend.data.imagga;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImaggaTag {
    @JsonProperty
    double confidence;

    @JsonProperty
    ImaggaEnTag tag;

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public ImaggaEnTag getTag() {
        return tag;
    }

    public void setTag(ImaggaEnTag tag) {
        this.tag = tag;
    }
}
