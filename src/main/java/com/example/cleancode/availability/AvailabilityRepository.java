package com.example.cleancode.availability;

import com.example.cleancode.enums.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    Optional<Availability> findByDateAndTimeSlots(LocalDateTime date, TimeSlots timeSlot);

    List<Availability> findByDate(LocalDateTime localDateTime);
}
