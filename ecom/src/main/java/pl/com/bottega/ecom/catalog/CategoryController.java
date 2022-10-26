package pl.com.bottega.ecom.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    ResponseEntity<CreateCategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) {
        var id = categoryService.create(new CreateCategoryCommand(request.name));
        return new ResponseEntity<>(new CreateCategoryResponse(id), HttpStatus.CREATED);
    }

    @GetMapping
    List<CategoryDto> searchCategories(@RequestParam("phrase") String searchPhrase) {
        return categoryService.search(searchPhrase);
    }

    @PutMapping("/{categoryId}")
    void updateCategory(@PathVariable UUID categoryId, @RequestBody UpdateCategoryRequest request) {
        categoryService.update(new UpdateCategoryCommand(categoryId, request.name));
    }
}

@Data
class CreateCategoryRequest {
    String name;
}

@Data
@AllArgsConstructor
class CreateCategoryResponse {
    UUID categoryId;
}

@Data
class UpdateCategoryRequest {
    String name;
}