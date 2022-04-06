package com.example.demo.service.imp;

import com.example.demo.model.Category;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public Category findOne(String categoryId) {
        return categoryRepo.getById(categoryId);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    @Override
    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public void remove(String categoryId) {
        categoryRepo.deleteById(categoryId);
    }
}
