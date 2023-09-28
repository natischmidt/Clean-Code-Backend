package com.example.cleancode.job;

import com.example.cleancode.availability.Availability;
import com.example.cleancode.availability.AvailabilityRepository;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.enums.TimeSlots;
import com.example.cleancode.exceptions.JobDoesNotExistException;
import com.example.cleancode.exceptions.PersonDoesNotExistException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final AvailabilityRepository availabilityRepository;

    public JobService(JobRepository jobRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository, AvailabilityRepository availabilityRepository) {

        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public void updateAvailability(Employee selectedEmployee, LocalDateTime date, TimeSlots timeSlot, Job job) {

        Optional<Availability> optionalAvailability = availabilityRepository.findByDateAndTimeSlots(date, timeSlot);
        Availability availability;

        if (optionalAvailability.isPresent()) {
            availability = optionalAvailability.get();
        } else {
            availability = new Availability();
            availability.setDate(date);
            availability.setTimeSlots(timeSlot);
        }

        availability.addEmployee(selectedEmployee);

        availability.getJobs().add(job);
        job.getAvailabilities().add(availability);

        availabilityRepository.save(availability);
    }

    public List<Employee> findUnbookedEmployees(LocalDateTime date, TimeSlots timeSlot){
        return employeeRepository.findUnbookedEmployees(date, timeSlot);
    }


    public Long createJob(CreateJobDTO createJobDTO) {

        LocalDateTime date = LocalDateTime.parse(createJobDTO.getDate() + "T00:00:00");

        TimeSlots timeSlot = createJobDTO.getTimeSlot();

        List<Employee> unbookedEmployees = findUnbookedEmployees(date, timeSlot);



        if (unbookedEmployees.isEmpty()){
            throw new RuntimeException("No employees are available for this time slot.");
        }

        Employee assignedEmployee = unbookedEmployees.get(0);

        Job job = new Job(
                createJobDTO.getJobtype(),
                date,
                timeSlot,
                createJobDTO.getJobStatus(),
                createJobDTO.getSquareMeters(),
                createJobDTO.getPaymentOption(),

                //Temporary field for employee
                // TODO: Before an employee is assigned for the job, we must check if the employee
                // is free this time and time in the calender.
                assignedEmployee,

                customerRepository.findById(createJobDTO.getCustomerId()).get());

        jobRepository.save(job);

        updateAvailability(assignedEmployee, date, timeSlot, job);



        return job.getJobId();
    }

    public Long deleteJob(Long id){
        Optional<Job> optJob = jobRepository.findById(id);

        if(optJob.isPresent()){
            jobRepository.deleteById(id);
            return id;
        } else {
            throw new PersonDoesNotExistException("There is no job with that id in database.");
        }
    }

    public Optional<Job> getJob(Long id) {

        Optional<Job> optJob = jobRepository.findById(id);

        if(optJob.isPresent()){
            jobRepository.findById(id);
            return optJob;
        } else {
            throw new JobDoesNotExistException("There is no job with that id in database.");
        }

    }
}
