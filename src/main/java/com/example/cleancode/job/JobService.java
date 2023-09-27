package com.example.cleancode.job;

import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }


    public Long createJob(CreateJobDTO createJobDTO) {
        Job job = new Job(
                createJobDTO.getJobtype(),
                createJobDTO.getDate(),
                createJobDTO.getJobStatus(),
                createJobDTO.getSquareMeters(),
                createJobDTO.getPaymentOption());

        jobRepository.save(job);

        return job.getJobId();

    }

}
