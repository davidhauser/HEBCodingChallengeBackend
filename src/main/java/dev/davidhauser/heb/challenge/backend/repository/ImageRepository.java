package dev.davidhauser.heb.challenge.backend.repository;

import dev.davidhauser.heb.challenge.backend.entity.ImageRecord;

import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<ImageRecord, Long> {}
