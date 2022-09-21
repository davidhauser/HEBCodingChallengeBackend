package dev.davidhauser.heb.challenge.backend.service;

import dev.davidhauser.heb.challenge.backend.entity.ImageRecord;
import dev.davidhauser.heb.challenge.backend.repository.ImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<ImageRecord> getAll() {
        Iterable<ImageRecord> records = this.imageRepository.findAll();
        return StreamSupport.stream(records.spliterator(), false).collect(Collectors.toList());
    }

    public Optional<ImageRecord> getByImageId(Long imageId) {
        return this.imageRepository.findById(imageId);
    }

    public List<ImageRecord> getByObjects(String[] objects) {
        List<ImageRecord> imageRecords = getAll();

        return imageRecords.stream().filter((ImageRecord record) -> {
            boolean result = true;
            for (String object : objects) {
                if (!record.getObjects().contains(object)) {
                    result = false;
                    break;
                }
            }
            return result;
        }).collect(Collectors.toList());
    }

    public ImageRecord add(ImageRecord imageRecord) {
        return this.imageRepository.save(imageRecord);
    }
}
