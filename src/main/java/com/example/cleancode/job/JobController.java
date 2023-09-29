package com.example.cleancode.job;

import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/getAllJobs")
    public Optional<List<Job>> getAllJobs(){
        return jobService.getAllJobs();
    }

    @PutMapping("/update/{id}")
    public Optional<Job> updateJobInfo(@PathVariable Long id,
                                       @RequestBody Job job){
        return jobService.updateJobInfo(id, job);
    }




}
