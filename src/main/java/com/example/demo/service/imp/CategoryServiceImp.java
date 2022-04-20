package com.example.demo.service.imp;

import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.service.CategoryService;
import com.example.demo.util.exceptions.ResourceMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImp implements CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    @Override
    public Category findOne(String categoryId) {
        log.info("Retrieving a category with the requested id {}", categoryId);
        Optional<Category> cat = categoryRepo.findById(categoryId);
        if (cat.isEmpty()) {
            throw new ResourceMissingException();
        }
        return cat.get();

    }

    @Override
    public List<Category> findAll() {
        log.info("Retrieving a list of all categories...");
        return categoryRepo.findAll();
    }

    @Override
    public Category save(Category category) {
        log.info("Saving the category to the database...");
        return categoryRepo.save(category);
    }

    @Override
    public void remove(String categoryId) {
        log.info("Removing the category from the database...");
        categoryRepo.deleteById(categoryId);
    }
}
