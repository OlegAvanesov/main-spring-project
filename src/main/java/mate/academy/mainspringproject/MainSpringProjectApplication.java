package mate.academy.mainspringproject;

import java.math.BigDecimal;
import java.util.List;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainSpringProjectApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(MainSpringProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
                    @Override
                    public void run(String... args) throws Exception {
                        Book firstBook = new Book();
                        firstBook.setTitle("The Adventures of Tom Sawyer");
                        firstBook.setAuthor("Mark Twain");
                        firstBook.setIsbn("123456789");
                        firstBook.setPrice(BigDecimal.valueOf(50));

                        Book secondBook = new Book();
                        secondBook.setTitle("Robinson Crusoe");
                        secondBook.setAuthor("Daniel Defoe");
                        secondBook.setIsbn("123456781");
                        secondBook.setPrice(BigDecimal.valueOf(60));

                        bookService.save(firstBook);
                        bookService.save(secondBook);

                        List<Book> bookList = bookService.findAll();
                    }
        };
    }
}
