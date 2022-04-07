package com.example.demo.service.imp;

import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.service.CategoryService;
import com.example.demo.util.exceptions.ResourceMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public Category findOne(String categoryId) {

        Optional<Category> cat = categoryRepo.findById(categoryId);
        if (cat.isEmpty()) {
            throw new ResourceMissingException();
        }
        return cat.get();

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
