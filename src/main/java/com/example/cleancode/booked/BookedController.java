package com.example.cleancode.booked;

import com.example.cleancode.enums.TimeSlots;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/api/ava")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookedController {

    private final BookedService bookedService;

    public BookedController(BookedService bookedService) {
        this.bookedService = bookedService;
    }

    @GetMapping("/freeSlots/{date}")
    public List<TimeSlots> getFreeSlotsForDate(@PathVariable String date) throws ParseException {
        return bookedService.getFreeSlotsForDate(date);
    }
}
