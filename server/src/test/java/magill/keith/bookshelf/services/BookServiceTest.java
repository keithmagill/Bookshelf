package magill.keith.bookshelf.services;

import magill.keith.bookshelf.model.Book;
import magill.keith.bookshelf.repositories.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @InjectMocks
    private BookService subject;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void getBook_whenGivenBookID_ReturnsSingleBookObject() {
        Book book = new Book();
        book.setTitle("Chosen");

        Optional<Book> bookOptional = Optional.of(book);

        when(bookRepository.findById(anyLong())).thenReturn(bookOptional);

        Book actual = subject.getBook(123L);

        assertThat(actual.getTitle(), is("Chosen"));
    }

    @Test
    public void getBook_whenGivenBookID_ReturnsCorrectBookObject_ForGivenBookID() {
        Book book = new Book();
        book.setTitle("Game of Thrones");

        Optional<Book> bookOptional = Optional.of(book);

        when(bookRepository.findById(anyLong())).thenReturn(bookOptional);

        Book actual = subject.getBook(987L);

        assertThat(actual.getTitle(), is("Game of Thrones"));
    }

    @Test
    public void getBook_whenGivenBookIdOf456_DelegatesToBookRepository_WithGivenBookID() {
        subject.getBook(456L);

        verify(bookRepository, times(1)).findById(456L);
    }

    @Test
    public void getBook_whenGivenBookIdOf789_DelegatesToBookRepository_WithGivenBookID() {
        subject.getBook(789L);

        verify(bookRepository, times(1)).findById(789L);
    }

    @Test
    public void getBook_whenGivenAnInvalidBooKID_ReturnsNull() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Book actual = subject.getBook(123L);

        assertNull(actual);
    }

    @Test
    public void getBooks_DelegatesToBookRepository() {
        subject.getBooks();

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getBooks_WhenOneBookExistsInRepo_ThenAListOfOneBookObjectIsReturned () {
        List<Book> bookList = Collections.singletonList(new Book());
        when(bookRepository.findAll()).thenReturn(bookList);

        List<Book> actual = subject.getBooks();

        assertThat(actual, hasSize(1));
    }

    @Test
    public void getBooks_WhenMultipleBooksExistInRepo_ThenAListOfMultipleBookObjectsIsReturned () {
        List<Book> bookList = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(bookList);

        List<Book> actual = subject.getBooks();

        assertThat(actual, hasSize(2));
    }

    @Test
    public void addBook_WhenCallWithANewBookRequest_DelegatesToBookRepositoryWithBookObject () {
        Book book = new Book();
        book.setTitle("The Hobbit");
        subject.addBook(book);

        verify(bookRepository, times(1)).save(book);
    }
}