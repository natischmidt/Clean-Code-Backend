package com.example.cleancode.availability;

import com.example.cleancode.enums.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    Optional<Availability> findByDateAndTimeSlot(LocalDateTime date, TimeSlots timeSlot);
}
