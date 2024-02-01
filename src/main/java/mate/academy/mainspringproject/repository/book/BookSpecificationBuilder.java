package mate.academy.mainspringproject.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.book.BookSearchParameters;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.repository.SpecificationBuilder;
import mate.academy.mainspringproject.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(searchParameters.authors()));
        }
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(searchParameters.titles()));
        }
        return spec;
    }
}
