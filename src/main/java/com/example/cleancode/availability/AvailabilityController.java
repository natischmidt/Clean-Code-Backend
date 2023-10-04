package com.example.cleancode.availability;

import com.example.cleancode.enums.TimeSlots;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/ava")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AvailabilityController {

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityController(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    //Svart magii nerråt
    @GetMapping("/getfull")
    public Map<LocalDateTime, List<TimeSlots>> getFullyBookedSlots() {
        List<Availability> availabilities = availabilityRepository.findAll();
        Map<LocalDateTime, List<TimeSlots>> fullyBookedSlots = new HashMap<>();

        int maxEmployeesPerSlot = 2;

        for (Availability availability : availabilities) {
            if (availability.getEmployees().size() >= maxEmployeesPerSlot) {
                fullyBookedSlots
                        .computeIfAbsent(availability.getDate(), k -> new ArrayList<>())
                        .add(availability.getTimeSlots());
            }
        }

        return fullyBookedSlots;
    }

    @GetMapping("/freeSlots/{date}")
    public List<TimeSlots> getFreeSlotsForDate(@PathVariable String date) throws ParseException {
        // Parse the string to a LocalDate object
        LocalDate targetDate = LocalDate.parse(date);

        // Initialize a list to hold free slots
        List<TimeSlots> freeSlots = new ArrayList<>(Arrays.asList(TimeSlots.values()));

        // Fetch all Availability records for the specific date
        List<Availability> availabilitiesForDate = availabilityRepository.findByDate(targetDate.atStartOfDay());

        int maxEmployees = 2;
        // Loop through each availability and remove time slots from the freeSlots list if they are fully booked
        for (Availability availability : availabilitiesForDate) {
            if (availability.getEmployees().size() >= maxEmployees) {
                freeSlots.remove(availability.getTimeSlots());
            }
        }

        return freeSlots;
    }

}
