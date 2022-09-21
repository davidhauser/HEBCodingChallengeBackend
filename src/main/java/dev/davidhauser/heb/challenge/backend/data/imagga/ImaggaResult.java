package dev.davidhauser.heb.challenge.backend.data.imagga;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ImaggaResult {
    @JsonProperty
    List<ImaggaTag> tags;

    @JsonProperty
    public List<ImaggaTag> getTags() {
        return tags;
    }

    public void setTags(List<ImaggaTag> tags) {
        this.tags = tags;
    }
}
