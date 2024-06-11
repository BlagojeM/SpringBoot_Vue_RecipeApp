package com.example.recipe_service.controller;

import com.example.recipe_service.model.Recipe;
import com.example.recipe_service.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @CrossOrigin(origins = "http://localhost:3000") // Add your frontend domain here
    @GetMapping("/getAllRecipes")
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:3000") // Add your frontend domain here
    @PostMapping("/addRecipe")
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @CrossOrigin(origins = "http://localhost:3000") // Add your frontend domain here
    @PutMapping("/{id}")
    public Recipe updateRecipe(@PathVariable Long id, @RequestBody Recipe recipeDetails) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
        recipe.setTitle(recipeDetails.getTitle());
        recipe.setDescription(recipeDetails.getDescription());
        recipe.setIngredients(recipeDetails.getIngredients());
        recipe.setImage(recipeDetails.getImage());
        return recipeRepository.save(recipe);
    }

    @CrossOrigin(origins = "http://localhost:3000") // Add your frontend domain here
    @DeleteMapping("deleteRecipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
        recipeRepository.delete(recipe);
        return ResponseEntity.ok().build();
    }
}