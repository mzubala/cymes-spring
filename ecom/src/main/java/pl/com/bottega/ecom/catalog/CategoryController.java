package pl.com.bottega.ecom.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    ResponseEntity<CreateCategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        var id = categoryService.create(new CategoryService.CreateCategoryCommand(request.name));
        return new ResponseEntity<>(new CreateCategoryResponse(id), HttpStatus.CREATED);
    }

    @GetMapping
    List<CategoryDto> searchCategories(@RequestParam("phrase") String searchPhrase) {
        return categoryService.search(searchPhrase);
    }

    @PutMapping("/{categoryId}")
    void updateCategory(@PathVariable UUID categoryId, @Valid @RequestBody UpdateCategoryRequest request) {
        categoryService.update(new CategoryService.UpdateCategoryCommand(categoryId, request.name));
    }
}

@Data
class CreateCategoryRequest {
    @NotBlank
    String name;
}

@Data
@AllArgsConstructor
class CreateCategoryResponse {
    UUID categoryId;
}

@Data
class UpdateCategoryRequest {
    @NotBlank
    String name;
}