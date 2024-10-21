package com.nikhil.dreamshops.service.product;

import java.util.List;

import com.nikhil.dreamshops.dto.ProductDto;
import com.nikhil.dreamshops.model.Product;
import com.nikhil.dreamshops.request.AddProductRequest;
import com.nikhil.dreamshops.request.ProductUpdateRequest;

public interface IProductService {

	Product addProduct(AddProductRequest product);
	Product getProductById(Long id);
	void deleteProductById(Long id);
	public Product updateProduct(ProductUpdateRequest request, Long productId);

	List<Product> getAllProducts();
	List<Product> getProductsByCategory(String category);
	List<Product> getProductsByBrand(String brand);
	List<Product> getProductsByCategoryAndBrand(String category, String brand);
	List<Product> getProductsByName(String name);
	List<Product> getProductsByBrandAndName(String category, String name);
	Long countProductsByBrandAndName(String brand, String name);

	List<ProductDto> getConvertedProducts(List<Product> products);

	ProductDto convertToDto(Product product);

}
