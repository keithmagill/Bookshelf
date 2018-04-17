package magill.keith.bookshelf.services;

import magill.keith.bookshelf.model.Book;
import magill.keith.bookshelf.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private BookRepository bookRepository;

    BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book getBook(long bookID) {
        Optional<Book> bookOptional = bookRepository.findById(bookID);
        if(bookOptional.isPresent()){
            return bookOptional.get();
        }
        return null;
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }
}
