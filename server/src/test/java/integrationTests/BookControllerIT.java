package integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import magill.keith.bookshelf.BookshelfApplication;
import magill.keith.bookshelf.model.Book;
import magill.keith.bookshelf.repositories.BookRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.theInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BookshelfApplication.class})
@WebAppConfiguration
public class BookControllerIT {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    private Book book2;

    private MockMvc mockMvc;

    private final String TEST_TITLE = "The Magic Mountain";

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        book = new Book();
        book.setTitle("Slowness");
        bookRepository.save(book);

        book2 = new Book();
        book2.setTitle("Chosen");
        bookRepository.save(book2);
    }

    @Test
    public void givenWebApplicationContext_whenServletContext_thenItProvidesBookController() {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("bookController"));
    }

    @Test
    public void givenGetBookURI_whenBookIdIs123_thenReturnStatus200OKAndBookObjectForId() throws Exception {
        this.mockMvc.perform(get("/api/v1/book/{bookId}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Slowness"));
    }

    @Test
    public void givenGetBooksURI_whenAPIIsInvoked_thenReturnStatus200OKAndAListOfBookObjects() throws Exception {
        this.mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Slowness"))
                .andExpect(jsonPath("$[1].title").value("Chosen"));
    }

    @Test
    public void givenAddBookURI_whenCalledWithABookRequest_thenReturnStatus201Created() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Book book = new Book();
        book.setTitle(TEST_TITLE);

        String addBookRequest = objectMapper.writeValueAsString(book);

        this.mockMvc.perform(post("/api/v1/book")
                .contentType("application/json")
                .content(addBookRequest))
                .andExpect(status().isCreated());

        assertNotNull(bookRepository.findByTitle(TEST_TITLE));
    }

    @After
    public void tearDown () {
        bookRepository.delete(book);
        bookRepository.delete(book2);
        Book addedBook = bookRepository.findByTitle(TEST_TITLE);
        if(addedBook != null) {
            bookRepository.delete(addedBook);
        }
    }


}
