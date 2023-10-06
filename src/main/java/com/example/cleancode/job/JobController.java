package com.example.cleancode.job;

import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.TimeSlots;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/jobs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    public List<GetJobDTO> getAllJobs(){
        return jobService.getAllJobs();
    }

    // Get all jobs for a specific employee
    @GetMapping("/getAllJobsForEmployee/{empId}")
    public List<GetJobDTO> getAllJobsForEmployee(@PathVariable Long empId){
        return jobService.getAllJobsForEmployee(empId);
    }

    // Get all jobs for a specific customer
    @GetMapping("/getAllJobsForCustomer/{cusId}")
    public List<GetJobDTO> getAllJobsForCustomer(@PathVariable Long cusId){
        return jobService.getAllJobsForCustomer(cusId);
    }

    @PutMapping("/update/{id}")
    public GetJobDTO updateJobInfo(@PathVariable Long id,
                                   @RequestBody GetJobDTO jobDTO){
        return jobService.updateJobInfo(id, jobDTO);
    }

    @GetMapping("/getByStatus")
    public List<Job> getJobByStatus(@RequestParam List<String> statuses){
        List<JobStatus> jobStatuses = statuses.stream().map(status ->
                JobStatus.valueOf(status.toUpperCase())).collect(Collectors.toList());
        return jobService.getJobsByStatus(jobStatuses);

    }

    @GetMapping("/getAvailableEmployees")
    public HashMap<Integer, Boolean> getAvailableEmployees(@RequestBody GetAvailableEmployeeDTO
                                                                       getAvailableEmployeeDTO) {

        return jobService.getAvailableEmployees(
                LocalDateTime.parse(getAvailableEmployeeDTO.getDate() + "T00:00:00"),
                getAvailableEmployeeDTO.getLookForAvailableThisManyHours());

    }




}
