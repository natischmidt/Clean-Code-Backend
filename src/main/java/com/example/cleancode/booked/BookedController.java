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

    //Svart magii nerråt
    /*@GetMapping("/getfull")
    public Map<LocalDateTime, List<TimeSlots>> getFullyBookedSlots() {
        List<Booked> availabilities = availabilityRepository.findAll();
        Map<LocalDateTime, List<TimeSlots>> fullyBookedSlots = new HashMap<>();

        int maxEmployeesPerSlot = 2;

        for (Booked availability : availabilities) {
            if (availability.getEmployees().size() >= maxEmployeesPerSlot) {
                fullyBookedSlots
                        .computeIfAbsent(availability.getDate(), k -> new ArrayList<>())
                        .add(availability.getTimeSlots());
            }
        }

        return fullyBookedSlots;
    }
*/
    @GetMapping("/freeSlots/{date}")
    public List<TimeSlots> getFreeSlotsForDate(@PathVariable String date) throws ParseException {

        return bookedService.getFreeSlotsForDate(date);
    }

}
