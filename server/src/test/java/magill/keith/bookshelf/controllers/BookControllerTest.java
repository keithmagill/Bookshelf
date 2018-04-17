package magill.keith.bookshelf.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import magill.keith.bookshelf.model.Book;
import magill.keith.bookshelf.services.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.theInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookController controller;

    @MockBean
    private BookService bookService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getBooks_Returns200OK() throws Exception {
        this.mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk());
    }

    @Test
    public void getBooks_delegatesToBookService () throws Exception {
        this.mockMvc.perform(get("/api/v1/books"));

        verify(bookService, times(1)).getBooks();
    }

    @Test
    public void getBooks_WhenOneBookExists_ThenAListOfOneBookObjectIsReturned() throws Exception {
        when(bookService.getBooks()).thenReturn(Collections.singletonList(new Book()));
        this.mockMvc.perform(get("/api/v1/books"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getBooks_WhenMultipleBooksExist_ThenAListOfMultipleBooksIsReturned () throws Exception {
        List<Book> bookList = Arrays.asList(new Book(), new Book());
        when(bookService.getBooks()).thenReturn(bookList);

        this.mockMvc.perform(get("/api/v1/books"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getBook_GivenABookID_Returns200OK() throws Exception {
        this.mockMvc.perform(get("/api/v1/book/123"))
                .andExpect(status().isOk());
    }

    @Test
    public void getBook_GivenABookID_DelegatesToBookService_WithBookID() throws Exception {
        this.mockMvc.perform((get("/api/v1/book/456")));

        verify(bookService, times(1)).getBook(456);
    }

    @Test
    public void getBook_GivenABookID_ReturnsASingleBookObject() throws Exception {
        Book book = new Book();
        book.setTitle("The Magic Mountain");

        when(bookService.getBook(anyLong())).thenReturn(book);

        this.mockMvc.perform(get("/api/v1/book/456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.title").value("The Magic Mountain"));
    }

    @Test
    public void getBook_GivenABookID_ReturnsASingleBookObject_ForADifferentBook() throws Exception {

        Book book = new Book();
        book.setTitle("Quiet");

        when(bookService.getBook(anyLong())).thenReturn(book);

        this.mockMvc.perform(get("/api/v1/book/456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.title").value("Quiet"));
    }

    @Test
    public void addBook_WhenInvokedWithANewBookRequest_ThenAStatus201CreatedIsReturned () throws Exception {
        Book book = new Book();
        book.setTitle("Beyond Black");

        ObjectMapper objectMapper = new ObjectMapper();
        String addBookRequest = objectMapper.writeValueAsString(book);

        this.mockMvc.perform(post("/api/v1/book")
                .content(addBookRequest)
                .contentType("application/json"))
                .andExpect(status().isCreated());
    }

    @Test
    public void addBook_WhenInvokedWithANewBookRequest_ThenItDelegatesToBookServiceWithNewBookObject () throws Exception {
        Book book = new Book();
        book.setTitle("Inverting the Pyramid");

        ObjectMapper objectMapper = new ObjectMapper();
        String addBookRequest = objectMapper.writeValueAsString(book);

        this.mockMvc.perform(post("/api/v1/book")
                .contentType("application/json")
                .content(addBookRequest));

        verify(bookService, times(1)).addBook(any(Book.class));

    }


}