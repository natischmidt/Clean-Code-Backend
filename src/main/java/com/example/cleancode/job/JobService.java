package com.example.cleancode.job;

import com.example.cleancode.employees.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;

    public JobService(JobRepository jobRepository, EmployeeRepository employeeRepository) {

        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
    }


    public Long createJob(CreateJobDTO createJobDTO) {
        Job job = new Job(
                createJobDTO.getJobtype(),
                createJobDTO.getDate(),
                createJobDTO.getJobStatus(),
                createJobDTO.getSquareMeters(),
                createJobDTO.getPaymentOption(),
                employeeRepository.findById(1L).get());

        jobRepository.save(job);

        return job.getJobId();

    }

}
