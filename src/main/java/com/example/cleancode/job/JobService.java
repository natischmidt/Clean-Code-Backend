package com.example.cleancode.job;

import com.example.cleancode.booked.Booked;
import com.example.cleancode.booked.BookedRepository;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.Role;
import com.example.cleancode.enums.TimeSlots;
import com.example.cleancode.exceptions.JobDoesNotExistException;
import com.example.cleancode.exceptions.NoJobsForCustomerException;
import com.example.cleancode.exceptions.NoJobsForEmploeyyException;
import com.example.cleancode.exceptions.PersonDoesNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final BookedRepository bookedRepository;

    public JobService(JobRepository jobRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository, BookedRepository bookedRepository) {

        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.bookedRepository = bookedRepository;
    }

    public void updateAvailability(Employee selectedEmployee, LocalDateTime date, List<TimeSlots> timeSlot, Job job) {

        for (int i = 0; i < timeSlot.size(); i++) {

            Optional<Booked> optionalAvailability = bookedRepository.findByDateAndTimeSlots(date, timeSlot.get(i));
            Booked booked;

            if (optionalAvailability.isPresent()) {
                booked = optionalAvailability.get();
            } else {
                booked = new Booked();
                booked.setDate(date);
                booked.setTimeSlots(timeSlot.get(i));
            }

            booked.addEmployee(selectedEmployee);

            booked.getJobs().add(job);
            job.getAvailabilities().add(booked);

            bookedRepository.save(booked);
        }
    }

    public List<Employee> findUnbookedEmployees(LocalDateTime date, TimeSlots timeSlot) {

        List<Employee> employees = employeeRepository.findUnbookedEmployees(date, timeSlot);

        return employees.stream().filter(x -> x.getRole().equals(Role.EMPLOYEE)).toList();
    }


    public Long createJob(CreateJobDTO createJobDTO) {

        LocalDateTime date = LocalDateTime.parse(createJobDTO.getDate() + "T00:00:00");
        TimeSlots timeSlot = createJobDTO.getTimeSlotList().get(0);
        List<Employee> unbookedEmployees = findUnbookedEmployees(date, timeSlot);

        if (unbookedEmployees.isEmpty()) {
            throw new RuntimeException("No employees are available for this time slot.");
        }

        Employee assignedEmployee = unbookedEmployees.get(0);

        Job job = new Job(
                createJobDTO.getJobtype(),
                date,
                timeSlot,
                JobStatus.PENDING,
                createJobDTO.getSquareMeters(),
                createJobDTO.getPaymentOption(),
                assignedEmployee,
                customerRepository.findById(createJobDTO.getCustomerId()).get());

        jobRepository.save(job);

        updateAvailability(assignedEmployee, date, createJobDTO.getTimeSlotList(), job);

        return job.getJobId();
    }

    public Long deleteJob(Long id) {
        Optional<Job> optJob = jobRepository.findById(id);

        if (optJob.isPresent()) {
            jobRepository.deleteById(id);
            return id;
        } else {
            throw new PersonDoesNotExistException("There is no job with that id in database.");
        }
    }

    public Optional<Job> getJob(Long id) {

        Optional<Job> optJob = jobRepository.findById(id);

        if (optJob.isPresent()) {
            jobRepository.findById(id);
            return optJob;
        } else {
            throw new JobDoesNotExistException("There is no job with that id in database.");
        }

    }

    public Optional<List<Job>> getAllJobs() {

        List<Job> optAllJobs = jobRepository.findAll();

        if (!optAllJobs.isEmpty()) {
            return Optional.of(optAllJobs);
        } else {
            throw new JobDoesNotExistException("There is no jobs in the database");
        }
    }

    public Optional<List<Job>> getAllJobsForEmployee(Long empId) {

        List<Job> optAllJobsForEmp = jobRepository.findAll()
                .stream()
                .filter(job -> job.getEmployee().getId() == empId)
                .collect(Collectors.toList());

        if (!optAllJobsForEmp.isEmpty()) {
            return Optional.of(optAllJobsForEmp);
        } else {
            throw new NoJobsForEmploeyyException("There is no jobs for this employee");
        }
    }

    public Optional<List<Job>> getAllJobsForCustomer(Long cusId) {

        List<Job> optAllJobsForCustomer = jobRepository.findAll()
                .stream()
                .filter(job -> job.getCustomer().getId() == cusId)
                .collect(Collectors.toList());

        if (!optAllJobsForCustomer.isEmpty()) {
            return Optional.of(optAllJobsForCustomer);
        } else {
            throw new NoJobsForCustomerException("There is no jobs attached to this this customer");
        }
    }

    @Transactional
    public Optional<Job> updateJobInfo(Long id, Job job) {

        Optional<Job> optionalJob = jobRepository.findById(id);

        // Do you need to be able to update squareMeters?
        if (optionalJob.isPresent()) {
            Job jobUpdate = optionalJob.get();

            if (job.getJobtype() != null) {
                jobUpdate.setJobtype(job.getJobtype());
            }
            if (job.getDate() != null) {
                jobUpdate.setDate(job.getDate());
            }
            if (job.getJobStatus() != null) {
                jobUpdate.setJobStatus(job.getJobStatus());
            }
            if (job.getPaymentOption() != null) {
                jobUpdate.setPaymentOption(job.getPaymentOption());
            }
            jobRepository.save(jobUpdate);
            return Optional.of(jobUpdate);
        } else {
            throw new JobDoesNotExistException("Job does not exist");
        }
    }

    public Optional<List<Job>> getJobByStatus(JobStatus status) {
        return Optional.ofNullable(jobRepository.findByJobStatus(status));
    }
}
