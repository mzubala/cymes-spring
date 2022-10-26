package pl.com.bottega.ecom.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Log
class CategoryController {

    CategoryController() {
        log.info("Creating controller");
    }

    @PostMapping
    ResponseEntity<CreateCategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) {
        log.info(String.format("Create category %s", request));
        var response = new CreateCategoryResponse();
        response.setCategoryId(UUID.randomUUID());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    List<SearchedCategoryResponse> searchCategories(@RequestParam("phrase") String searchPhrase) {
        log.info(String.format("Search category, phrase: %s", searchPhrase));
        return List.of(
            new SearchedCategoryResponse(UUID.randomUUID(), "Jedzenie"),
            new SearchedCategoryResponse(UUID.randomUUID(), "Moda"),
            new SearchedCategoryResponse(UUID.randomUUID(), "Uroda")
        );
    }

    @PutMapping("/{categoryId}")
    void updateCategory(@PathVariable UUID categoryId, @RequestBody UpdateCategoryRequest request) {
        log.info(String.format("Updating category %s, %s", categoryId, request));
    }
}

@Data
class CreateCategoryRequest {
    String name;
}

@Data
class CreateCategoryResponse {
    UUID categoryId;
}

@Data
@AllArgsConstructor
class SearchedCategoryResponse {
    UUID categoryId;
    String name;
}

@Data
class UpdateCategoryRequest {
    String name;
}