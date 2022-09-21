package dev.davidhauser.heb.challenge.backend.data.imagga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImaggaResponse {
    @JsonProperty
    ImaggaResult result;

    public ImaggaResult getResult() {
        return result;
    }

    public void setResult(ImaggaResult result) {
        this.result = result;
    }
}
