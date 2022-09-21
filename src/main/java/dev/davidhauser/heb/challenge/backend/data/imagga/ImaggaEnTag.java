package dev.davidhauser.heb.challenge.backend.data.imagga;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImaggaEnTag {
    @JsonProperty
    String en;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
