package com.temporintech.dscatalog.tests;

import java.time.Instant;

import com.temporintech.dscatalog.DTO.ProductDTO;
import com.temporintech.dscatalog.entities.Category;
import com.temporintech.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png",
				Instant.parse("2020-07-13T20:50:07.12345Z"));
		product.getCategories().add(createCategory());
		return product;
	}

	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

	public static Category createCategory() {
		return new Category(1L, "Eletrônicos");
	}

}
