package com.example.cleancode.booked;

import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.enums.Role;
import com.example.cleancode.enums.TimeSlots;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BookedService {

    private final BookedRepository bookedRepository;
    private final EmployeeRepository employeeRepository;

    public BookedService(BookedRepository bookedRepository, EmployeeRepository employeeRepository) {
        this.bookedRepository = bookedRepository;
        this.employeeRepository = employeeRepository;
    }


    public List<TimeSlots> getFreeSlotsForDate(String date) {
        // Parse the string to a LocalDate object
        LocalDate targetDate = LocalDate.parse(date);

        // Initialize a list to hold free slots
        List<TimeSlots> freeSlots = new ArrayList<>(Arrays.asList(TimeSlots.values()));

        // Fetch all Booked records for the specific date
        List<Booked> availabilitiesForDate = bookedRepository.findByDate(targetDate.atStartOfDay());

        //int maxEmployees = 2;
        int maxEmployees = employeeRepository.findByRole(Role.EMPLOYEE).size();
        System.out.println(maxEmployees);
        // Loop through each availability and remove time slots from the freeSlots list if they are fully booked
        for (Booked booked : availabilitiesForDate) {
            if (booked.getEmployees().size() >= maxEmployees) {
                freeSlots.remove(booked.getTimeSlots());
            }
        }
        return freeSlots;
    }
}
