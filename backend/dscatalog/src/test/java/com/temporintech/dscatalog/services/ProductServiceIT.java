package com.temporintech.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.temporintech.dscatalog.repositories.ProductRepository;
import com.temporintech.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class ProductServiceIT {

	@Autowired
	private ProductService service;

	@Autowired
	private ProductRepository repository;

	private long nonExistingId;
	private long exintingId;
	private long countTotalProduct;

	@BeforeEach
	void setUp() throws Exception {
		exintingId = 1L;
		nonExistingId = 1000L;
		countTotalProduct = 25L;
	}

	@Test

	public void deleteShouldDeleteResourceWhenIdExists() {
		service.delete(exintingId);
		Assertions.assertEquals(countTotalProduct - 1, repository.count());
	}

	@Test
	public void deleteShoulThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

	}
}