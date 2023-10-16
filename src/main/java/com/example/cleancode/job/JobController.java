package com.example.cleancode.job;

import com.example.cleancode.enums.JobStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/jobs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    // Get a specific job
    @GetMapping("/getJob")
    public GetJobForUpdate getJob(@RequestHeader Long jobId){
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
    @GetMapping("/getAllJobsForEmployeeWithStatus/{empId}")
    public List<GetJobDTO> getAllJobsForEmployeeWithStatus(@PathVariable Long empId,
                                                           @RequestParam(name = "status", required = false) List<JobStatus> status) {
        return jobService.getAllJobsForEmployeeWithStatus(empId, status);
    }

    // Get all jobs for a specific customer
    @GetMapping("/getAllJobsForCustomer/{cusId}")
    public List<GetJobDTO> getAllJobsForCustomer(@PathVariable String cusId) {
        return jobService.getAllJobsForCustomer(UUID.fromString(cusId));
    }

    @GetMapping("/getAllJobsForCustomerWithStatus/{cusId}")
    public List<GetJobDTO> getAllJobsForCustomerWithStatus(@PathVariable UUID cusId,
                                                           @RequestParam(name = "status", required = false) List<JobStatus> status) {
        return jobService.getAllJobsForCustomerWithStatus(cusId, status);
    }

    @PutMapping("/updateJob")
    public GetJobDTO updateJobInfo(@RequestBody UpdateJobDTO jobDTO,
                                   @RequestParam (name ="message", required = false) String message){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! SSSSSSSSSSSSSSSSSSSSSSSSSSSSS @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        if (message != null && !message.isEmpty()) {
                jobDTO.setMessage(message);
        }
        return jobService.updateJobInfo(jobDTO);
    }

    @GetMapping("/getByStatus")
    public List<Job> getJobByStatus(@RequestParam List<String> statuses){
        List<JobStatus> jobStatuses = statuses.stream().map(status ->
                JobStatus.valueOf(status.toUpperCase())).collect(Collectors.toList());
        return jobService.getJobsByStatus(jobStatuses);
    }

    @PostMapping("/getAvailableEmployees")
    public List<Boolean> getAvailableEmployees(@RequestBody GetAvailableEmployeeDTO getAvailableEmployeeDTO) {
        return jobService.getAvailableEmployees(
                //LocalDateTime.parse(getAvailableEmployeeDTO.getDate().substring(0,10) + "T00:00:00"),
//                Date.from(Instant.from(LocalDateTime.parse(getAvailableEmployeeDTO.getDate()))),
                getAvailableEmployeeDTO.getDate(),
                getAvailableEmployeeDTO.getLookForAvailableThisManyHours());
    }
}
