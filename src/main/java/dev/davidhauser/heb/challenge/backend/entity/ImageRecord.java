package dev.davidhauser.heb.challenge.backend.entity;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "images")
public class ImageRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String imageUrl;

    private String label;

    @ElementCollection
    @CollectionTable(name = "image_objects", joinColumns = @JoinColumn(name = "image_id"))
    private List<String> objects;

    public ImageRecord() {
    }

    public ImageRecord(String imageUrl, String label) {
        this.imageUrl = imageUrl;
        this.label = label;
    }

    public Long getImageId() {
        return this.imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
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

    public List<String> getObjects() {
        return this.objects;
    }

    public void setObjects(List<String> objects) {
        this.objects = objects;
    }
}
