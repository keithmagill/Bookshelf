package magill.keith.bookshelf.repositories;

import magill.keith.bookshelf.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String book);
}
