package pl.com.bottega.ecom.catalog;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import pl.com.bottega.ecom.UserCommand;
import pl.com.bottega.ecom.infrastructure.Audit;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Audit
class CategoryService {

    private final CategoryRepository categoryRepository;

    public UUID create(CreateCategoryCommand createCategoryCommand) {
        var category = new Category(UUID.randomUUID(), createCategoryCommand.getName());
        categoryRepository.save(category);
        return category.getId();
    }

    public List<CategoryDto> search(String searchPhrase) {
        return categoryRepository.findByNameLike("%" + searchPhrase + "%").stream()
            .map(category -> new CategoryDto(category.getId(), category.getName()))
            .collect(Collectors.toList());
    }

    public void update(UpdateCategoryCommand updateCategoryCommand) {
        var category = categoryRepository.getById(updateCategoryCommand.getCategoryId());
        category.setName(updateCategoryCommand.getName());
        categoryRepository.save(category);
    }

    @Value
    static class CreateCategoryCommand extends UserCommand  {
        String name;
    }

    @Value
    static class UpdateCategoryCommand extends UserCommand {
        UUID categoryId;
        String name;
    }
}
