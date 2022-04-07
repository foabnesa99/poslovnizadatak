package com.example.demo.controller;


import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.util.exceptions.ResourceMissingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Categories Rest Controller", description = "REST API for categories")
@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "Add a new category", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "Internal server error"),
    }
    )

    @PostMapping(value="/add", consumes="application/json")
    public ResponseEntity<Category> saveCategory(@RequestBody Category category) {
        try {
            categoryService.save(category);
            return new ResponseEntity<>(category, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @ApiOperation(value = "Get a list of all categories", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK")
    }
    )
    @GetMapping(value = "/")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @ApiOperation(value = "Get a single category", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message="Not found"),
            @ApiResponse(code = 500, message = "Internal server error"),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") Integer id){
        try {
            Category category = categoryService.findOne(id.toString());
            return new ResponseEntity<>(category, HttpStatus.OK);

        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }



    @ApiOperation(value = "Editing a category", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK"),
            @ApiResponse(code = 404, message="Not found"),
            @ApiResponse(code = 500, message = "Internal server error"),
    }
    )

    @PutMapping(value="/edit/{id}", consumes="application/json")
    public ResponseEntity<Category> updateCategory(@RequestBody Category editCategory, @PathVariable("id") Integer id) {
        try {
            Category category = categoryService.findOne(id.toString());
            category.setName(editCategory.getName());
            categoryService.save(category);
            return new ResponseEntity<>(category, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @ApiOperation(value = "Removing a category", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "Not found"),
    }
    )
    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Integer id){

        try {
            Category category = categoryService.findOne(id.toString());
            categoryService.remove(category.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (ResourceMissingException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
