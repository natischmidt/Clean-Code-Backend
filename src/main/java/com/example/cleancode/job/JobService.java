package com.example.cleancode.job;

import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.exceptions.JobDoesNotExistException;
import com.example.cleancode.exceptions.PersonDoesNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    public JobService(JobRepository jobRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository) {

        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    public Long createJob(CreateJobDTO createJobDTO) {
        Job job = new Job(
                createJobDTO.getJobtype(),
                createJobDTO.getDateAndTime(),
                createJobDTO.getJobStatus(),
                createJobDTO.getSquareMeters(),
                createJobDTO.getPaymentOption(),

                //Temporary field for employee
                // TODO: Before an employee is assigned for the job, we must check if the employee
                // is free this time and time in the calender.
                employeeRepository.findById(1L).get(),

                customerRepository.findById(createJobDTO.getCustomerId()).get());

        jobRepository.save(job);
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

    @Transactional
    public Optional<Job> updateJobInfo(Long id, Job job) {

        Optional<Job> optionalJob = jobRepository.findById(id);

        // Behöver man ha med squareMeters?
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
