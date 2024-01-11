package com.temporintech.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.temporintech.dscatalog.DTO.ProductDTO;
import com.temporintech.dscatalog.entities.Product;
import com.temporintech.dscatalog.repositories.ProductRepository;
import com.temporintech.dscatalog.services.exceptions.DatabaseException;
import com.temporintech.dscatalog.services.exceptions.ResourceNotFoundException;
import com.temporintech.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private Product product;
	private long nonExistingId;
	private long exintingId;
	private long dependentId;
	private PageImpl<Product> page;

	@BeforeEach
	void setUp() throws Exception {
		product = Factory.createProduct();
		exintingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		page = new PageImpl<>(List.of(product));

		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		Mockito.when(repository.findById(exintingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		Mockito.doNothing().when(repository).deleteById(exintingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.findById(exintingId);
		Assertions.assertNotNull(result);
	}

	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findAll(pageable);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(exintingId);
		});

		// Mockito.verify(repository, Mockito.never()).deleteById(exintingId);
		Mockito.verify(repository, Mockito.times(1)).deleteById(exintingId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

		// Mockito.verify(repository, Mockito.never()).deleteById(exintingId);
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});

		// Mockito.verify(repository, Mockito.never()).deleteById(exintingId);
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}
}