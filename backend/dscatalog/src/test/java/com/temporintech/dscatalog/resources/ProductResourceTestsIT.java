package com.temporintech.dscatalog.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.temporintech.dscatalog.repositories.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceTestsIT {

	@Autowired
	private MockMvc mockMvc;
	
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
	private void findAllShouldReturnSortedPageWhenShortByName() throws Exception {
		
	}
}