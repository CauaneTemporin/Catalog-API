package com.temporintech.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.temporintech.dscatalog.DTO.ProductDTO;
import com.temporintech.dscatalog.services.ProductService;
import com.temporintech.dscatalog.services.exceptions.ResourceNotFoundException;
import com.temporintech.dscatalog.tests.Factory;

@WebMvcTest(ProductResourceTests.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	private long nonExistingId;
	private long exintingId;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;

	@BeforeEach
	void setUp() throws Exception {
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		exintingId = 1L;
		nonExistingId = 1000L;

		Mockito.when(productService.findAllPaged(any())).thenReturn(page);

		Mockito.when(productService.findById(exintingId)).thenReturn(productDTO);
		Mockito.when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/products").accept(org.springframework.http.MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}

	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/products/{id}", exintingId).accept(org.springframework.http.MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(
				get("/products/{id}", nonExistingId).accept(org.springframework.http.MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

}