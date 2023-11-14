package com.example.cleancode.booked;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ava")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookedController {

    private final BookedService bookedService;

    public BookedController(BookedService bookedService) {
        this.bookedService = bookedService;
    }

}
