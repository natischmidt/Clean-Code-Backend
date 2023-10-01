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

    @PostMapping("/createJob/")
    public Long createJob(@RequestBody CreateJobDTO createJobDTO) {
        return jobService.createJob(createJobDTO);
    }

    @DeleteMapping("/deleteJob")
    public Long deleteJob(@RequestHeader Long jobId){
        return jobService.deleteJob(jobId);
    }

    // Get a specific job
    @GetMapping("/getJob")
    public Optional<Job> getJob(@RequestHeader Long jobId){
        return jobService.getJob(jobId);
    }

    // Get all jobs
    @GetMapping("/getAllJobs")
    public Optional<List<Job>> getAllJobs(){
        return jobService.getAllJobs();
    }

    // Get all jobs for a specific employee
    @GetMapping("/getAllJobsForEmployee/{empId}")
    public Optional<List<Job>> getAllJobsForEmployee(@PathVariable Long empId){
        return jobService.getAllJobsForEmployee(empId);
    }

    // Get all jobs for a specific customer
    @GetMapping("/getAllJobsForCustomer/{cusId}")
    public Optional<List<Job>> getAllJobsForCustomer(@PathVariable Long cusId){
        return jobService.getAllJobsForCustomer(cusId);
    }

    @PutMapping("/update/{id}")
    public Optional<Job> updateJobInfo(@PathVariable Long id,
                                       @RequestBody Job job){
        return jobService.updateJobInfo(id, job);
    }




}
