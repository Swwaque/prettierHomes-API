package com.ph.controller;

import com.ph.payload.request.ImageUpdateRequest;
import com.ph.payload.response.ImageResponse;
import com.ph.service.ImageService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping
     public ResponseEntity<?> uploadImage(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("advert") Long advertId
    ) {
        List<ImageResponse> response = imageService.createImage(images, advertId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        return imageService.getImageById(id);
    }

    @GetMapping
    public ResponseEntity<List<ImageResponse>> getAllImages() {
        List<ImageResponse> responses = imageService.getAllImages();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{image_ids}")
    public ResponseEntity<Void> deleteImage(@PathVariable List<Long> image_ids) {
        imageService.deleteImage(image_ids);
        return ResponseEntity.ok().build();
    }


    @PutMapping()
    public ImageResponse updateImage(@RequestBody @Valid ImageUpdateRequest request) {

        return imageService.updateImage(request);
    }


    @GetMapping("/advert/{id}")
    public ResponseEntity<List<ImageResponse>> getAllImagesByAdvertId(@PathVariable Long id) {
        List<ImageResponse> responses = imageService.getAllImagesByAdvertId(id);
        return ResponseEntity.ok(responses);
    }

}
