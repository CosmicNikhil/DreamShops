package com.nikhil.dreamshops.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nikhil.dreamshops.dto.ImageDto;
import com.nikhil.dreamshops.exceptions.ResourceNotFoundException;
import com.nikhil.dreamshops.model.Image;
import com.nikhil.dreamshops.model.Product;
import com.nikhil.dreamshops.repository.ImageRepository;
import com.nikhil.dreamshops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

	private final ImageRepository imageRepository;
	private final IProductService productService;


	@Override
	public Image getImageById(Long id) {
		return imageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
	}

	@Override
	public void deleteImageById(Long id) {
		imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
			throw new ResourceNotFoundException("No image found with id: " + id);
		});

	}

	@Override
	public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
		Product product = productService.getProductById(productId);

		List<ImageDto> savedImageDto = new ArrayList<>();
		for (MultipartFile file : files) {
			try {
				// Create and set image attributes
				Image image = new Image();
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));
				image.setProduct(product);

				// Save image to generate the ID
				Image savedImage = imageRepository.save(image);

				// Set the download URL based on the generated image ID
				String downloadUrl = "/api/v1/images/image/download/" + savedImage.getId();
				savedImage.setDownloadUrl(downloadUrl);

				// Save the image again with the updated download URL
				imageRepository.save(savedImage);

				// Create the ImageDto and add to the list
				ImageDto imageDto = new ImageDto();
				imageDto.setId(savedImage.getId());
				imageDto.setFileName(savedImage.getFileName());
				imageDto.setDownloadUrl(savedImage.getDownloadUrl());
				savedImageDto.add(imageDto);

			} catch (IOException | SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return savedImageDto;
	}



	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		Image image = getImageById(imageId);
		try {
			image.setFileName(file.getOriginalFilename());
			image.setFileType(file.getContentType());
			image.setImage(new SerialBlob(file.getBytes()));
			imageRepository.save(image);
		} catch (IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}

	}
}