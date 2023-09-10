package com.temporintech.dscatalog.services;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.temporintech.dscatalog.DTO.CategoryDTO;
import com.temporintech.dscatalog.entities.Category;
import com.temporintech.dscatalog.repositories.CategoryRepository;
import com.temporintech.dscatalog.services.exceptions.EntityNotFoundException;
 
@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)//Garantia que vai ser uma transação com o BD, usando o readOnly não deixa sem travar
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}
}