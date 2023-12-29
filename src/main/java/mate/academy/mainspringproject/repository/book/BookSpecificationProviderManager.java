package mate.academy.mainspringproject.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.repository.SpecificationProvider;
import mate.academy.mainspringproject.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> specificationProviderList;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviderList.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Can't find specification provider for key: " + key));
    }
}
