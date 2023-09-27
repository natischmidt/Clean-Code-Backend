package com.example.cleancode.job;

import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.EmployeeRepository;
import org.springframework.stereotype.Service;

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
                createJobDTO.getDate(),
                createJobDTO.getJobStatus(),
                createJobDTO.getSquareMeters(),
                createJobDTO.getPaymentOption(),
                //Temporary fields
                employeeRepository.findById(1L).get(),
                customerRepository.findById(1L).get());


        jobRepository.save(job);

        return job.getJobId();

    }

}
