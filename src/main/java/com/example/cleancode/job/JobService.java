package com.example.cleancode.job;

import com.example.cleancode.availability.Availability;
import com.example.cleancode.availability.AvailabilityRepository;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.enums.TimeSlots;
import com.example.cleancode.exceptions.JobDoesNotExistException;
import com.example.cleancode.exceptions.NoJobsForCustomerException;
import com.example.cleancode.exceptions.NoJobsForEmploeyyException;
import com.example.cleancode.exceptions.PersonDoesNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                assignedEmployee,
                customerRepository.findById(createJobDTO.getCustomerId()).get());

        jobRepository.save(job);

        updateAvailability(assignedEmployee, date, timeSlot, job);



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
}
