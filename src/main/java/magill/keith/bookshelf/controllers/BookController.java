package magill.keith.bookshelf.controllers;

import magill.keith.bookshelf.model.Book;
import magill.keith.bookshelf.services.BookService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
class BookController {

    private BookService bookService;

    private BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/api/v1/book/{bookId}")
    Book getBook(@PathVariable(value = "bookId") Long bookID){
        return bookService.getBook(bookID);
    }

    @GetMapping("/api/v1/books")
    List<Book> getBooks(){
        return bookService.getBooks();
    }

    @PostMapping("/api/v1/book")
    ResponseEntity<String> addBook(@RequestBody Book bookRequest, UriComponentsBuilder ucBuilder){
        bookService.addBook(bookRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(bookRequest.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}