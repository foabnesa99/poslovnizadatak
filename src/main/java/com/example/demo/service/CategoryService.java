package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Channel;

import java.util.List;

public interface CategoryService {

    Category findOne(String categoryId);

    List<Category> findAll();

    Category save(Category category);

    void remove(String categoryId);

}
