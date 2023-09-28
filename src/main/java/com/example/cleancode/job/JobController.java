package com.example.cleancode.job;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/createJob")
    public Long createJob(@RequestBody CreateJobDTO createJobDTO) {
        return jobService.createJob(createJobDTO);
    }

    @DeleteMapping("/deleteJob")
    public Long deleteJob(@RequestHeader Long jobId){
        return jobService.deleteJob(jobId);
    }

    @GetMapping("/getJob")
    public Optional<Job> getJob(@RequestHeader Long jobId){
        return jobService.getJob(jobId);
    }

}
