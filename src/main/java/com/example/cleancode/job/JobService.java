package com.example.cleancode.job;

import com.example.cleancode.booked.Booked;
import com.example.cleancode.booked.BookedRepository;
import com.example.cleancode.customer.Customer;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.Role;
import com.example.cleancode.enums.TimeSlots;
import com.example.cleancode.exceptions.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.cleancode.enums.Role.EMPLOYEE;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final BookedRepository bookedRepository;

    public JobService(JobRepository jobRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository, BookedRepository bookedRepository) {
        this.jobRepository = jobRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.bookedRepository = bookedRepository;
    }

    public void updateAvailability(Employee selectedEmployee, LocalDateTime date, List<TimeSlots> timeSlot, Job job) {
     /** Denna metod anropas från createJob. Den lägger till rader i BookedRepository med de uppbokade tiderna, så
      * att vi inte kan boka fler städningar än vi har städare. */

        for (int i = 0; i < timeSlot.size(); i++) {

            Optional<Booked> optionalAvailability = bookedRepository.findByDateAndTimeSlots(date, timeSlot.get(i));
            Booked booked;

            if (optionalAvailability.isPresent()) {
                booked = optionalAvailability.get();
            } else {
                booked = new Booked();
                booked.setDate(date);
                booked.setTimeSlots(timeSlot.get(i));
            }

            booked.addEmployee(selectedEmployee);

            booked.getJobs().add(job);
            job.getAvailabilities().add(booked);

            bookedRepository.save(booked);
        }
    }

    public List<Employee> findUnbookedEmployees(LocalDateTime date, List<TimeSlots> timeSlot) {

        List<Employee> employees = employeeRepository.findUnbookedEmployees(date, timeSlot.get(0));

        return employees.stream().filter(x -> x.getRole().equals(Role.EMPLOYEE)).toList();
    }


    public List<Boolean> getAvailableEmployees(LocalDateTime date, int lookForAvailableThisManyHours) {

        List<List<Employee>> employeeListList = new ArrayList<>();
        List<Employee> empList;
        List<TimeSlots> timeSlotList = List.of(
                TimeSlots.EIGHT,
                TimeSlots.NINE,
                TimeSlots.TEN,
                TimeSlots.ELEVEN,
                TimeSlots.TWELVE,
                TimeSlots.THIRTEEN,
                TimeSlots.FOURTEEN,
                TimeSlots.FIFTEEN,
                TimeSlots.SIXTEEN
                );

        for(int i = 0; i < timeSlotList.size(); i++) {

            empList = employeeRepository.findUnbookedEmployees(date, timeSlotList.get(i));

            employeeListList.add(empList
                    .stream()
                    .filter(x -> x.getRole().equals(EMPLOYEE))
                    .collect(Collectors.toList()));
        }

        employeeListList.add(List.of());
        employeeListList.add(List.of());

        List<Boolean> boolList = new ArrayList<>(List.of(false, false, false, false, false, false, false, false, false));

        for(int i = 0; i < employeeListList.size(); i++) {
        /**
            Loopar igenom yttersta listan, som innehåller listor med tillgängliga employees ett visst klockslag och datum
        **/
            for(int j = 0; j < employeeListList.get(i).size(); j++) {
                /**
                 Loopar igenom inre listorna, som innehåller tillgängliga employees ett visst klockslag och datum
                 **/
                for(int k = 0; k < lookForAvailableThisManyHours; k++) {

                    /**
                     loopar lookForAvailableThisManyHours gånger, kollar så många listor framåt
                    **/
                    if(employeeListList.get(i + k).contains(employeeListList.get(i).get(j)) ) {
                        boolList.set(i, true);
                    }
                }
             }
        }
        if(lookForAvailableThisManyHours == 2) {
            boolList.set(8, false);
        }
        if(lookForAvailableThisManyHours == 3) {
            boolList.set(8, false);
            boolList.set(7, false);
        }
        return boolList;
    }

    public Long createJob(CreateJobDTO createJobDTO) {

        LocalDateTime date = LocalDateTime.parse(createJobDTO.getDate() + "T00:00:00");
        TimeSlots timeSlot = createJobDTO.getTimeSlotList().get(0);
        List<Employee> unbookedEmployees = findUnbookedEmployees(date, createJobDTO.getTimeSlotList());
        Optional<Customer> customerOptional = customerRepository.findById(createJobDTO.getCustomerId());

        if(customerOptional.isEmpty()) {
            throw new CustomerDoesNotExistException(createJobDTO.getCustomerId());
        }

        if (unbookedEmployees.isEmpty()) {
            throw new RuntimeException("No employees are available for this time slot.");
        }

        /** Assign random employee from the unbookedEmployees list */
        int randomAvailableEmployee = (int) Math.floor(Math.random() * unbookedEmployees.size());
        Employee assignedEmployee = unbookedEmployees.get(randomAvailableEmployee);

        Job job = new Job(
                createJobDTO.getJobtype(),
                date,
                timeSlot,
                JobStatus.PENDING,
                createJobDTO.getSquareMeters(),
                createJobDTO.getPaymentOption(),
                assignedEmployee,
                customerOptional.get());
        jobRepository.save(job);

        updateAvailability(assignedEmployee, date, createJobDTO.getTimeSlotList(), job);

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
            //jobRepository.findById(id);
            return optJob;
        } else {
            throw new JobDoesNotExistException("There is no job with that id in database.");
        }

    }


//    public Optional<List<Job>> getAllJobs() {
//
//        List<Job> optAllJobs = jobRepository.findAll();
//
//        if (!optAllJobs.isEmpty()) {
//            return Optional.of(optAllJobs);
//        } else {
//            throw new JobDoesNotExistException("There is no jobs in the database");
//        }
//    }

    public List<GetJobDTO> getAllJobs() {
        List<Job> allJobs = jobRepository.findAll();
        if (!allJobs.isEmpty()) {
            return convertToDTOList(allJobs);
        } else {
            throw new JobDoesNotExistException("There are no jobs in the database");
        }
    }

    private List<GetJobDTO> convertToDTOList(List<Job> jobs) {
        return jobs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GetJobDTO convertToDTO(Job job) {
        return new GetJobDTO(
                job.getJobId(),
                job.getJobtype(),
                job.getDate(),
                job.getTimeSlot(),
                job.getJobStatus(),
                job.getSquareMeters(),
                job.getPaymentOption(),
                job.getMessage(),
                job.getCustomer()
        );
    }

    public List<GetJobDTO> getAllJobsForEmployee(Long empId) {
        List<Job> jobsForEmployee = jobRepository.findAll()
                .stream()
                .filter(job -> job.getEmployee().getId() == empId)
                .collect(Collectors.toList());

        if (!jobsForEmployee.isEmpty()) {
            return convertToDTOList(jobsForEmployee);
        } else {
            throw new NoJobsForEmploeyyException("There are no jobs for this employee");
        }
    }

//    public Optional<List<Job>> getAllJobsForEmployee(Long empId) {
//
//        List<Job> optAllJobsForEmp = jobRepository.findAll()
//                .stream()
//                .filter(job -> job.getEmployee().getId() == empId)
//                .collect(Collectors.toList());
//
//        if (!optAllJobsForEmp.isEmpty()) {
//            return Optional.of(optAllJobsForEmp);
//        } else {
//            throw new NoJobsForEmploeyyException("There is no jobs for this employee");
//        }
//    }

//    public Optional<List<Job>> getAllJobsForCustomer(Long cusId) {
//
//        List<Job> optAllJobsForCustomer = jobRepository.findAll()
//                .stream()
//                .filter(job -> job.getCustomer().getId() == cusId)
//                .collect(Collectors.toList());
//
//        if (!optAllJobsForCustomer.isEmpty()) {
//            return Optional.of(optAllJobsForCustomer);
//        } else {
//            throw new NoJobsForCustomerException("There is no jobs attached to this this customer");
//        }
//    }

    public List<GetJobDTO> getAllJobsForCustomer(UUID cusId) {
        List<Job> jobsForCustomer = jobRepository.findAll()
                .stream()
                .filter(job -> job.getCustomer().getId() == cusId)
                .collect(Collectors.toList());
        if (!jobsForCustomer.isEmpty()) {
            return convertToDTOList(jobsForCustomer);
        } else {
            throw new NoJobsForCustomerException("There are no jobs attached to this customer");
        }
    }

    @Transactional
    public GetJobDTO updateJobInfo(GetJobDTO jobDTO) {
        Optional<Job> optionalJob = jobRepository.findById(jobDTO.getJobId());

        if (optionalJob.isPresent()) {
            Job jobToUpdate = optionalJob.get();

            // Update properties from the dto
            if (jobDTO.getJobtype() != null) {
                jobToUpdate.setJobtype(jobDTO.getJobtype());
            }
            if (jobDTO.getDate() != null) {
                jobToUpdate.setDate(jobDTO.getDate());
            }
            if (jobDTO.getJobStatus() != null) {
                jobToUpdate.setJobStatus(jobDTO.getJobStatus());
            }
            if (jobDTO.getPaymentOption() != null) {
                jobToUpdate.setPaymentOption(jobDTO.getPaymentOption());
            }
            jobRepository.save(jobToUpdate);

            // return  updated jobb as DTO
            return convertToDTO(jobToUpdate);
        } else {
            throw new JobDoesNotExistException("Job does not exist");
        }
    }


//    @Transactional
//    public Optional<Job> updateJobInfo(Long id, Job job) {
//
//        Optional<Job> optionalJob = jobRepository.findById(id);
//
//        // Do you need to be able to update squareMeters?
//        if (optionalJob.isPresent()) {
//            Job jobUpdate = optionalJob.get();
//
//            if (job.getJobtype() != null) {
//                jobUpdate.setJobtype(job.getJobtype());
//            }
//            if (job.getDate() != null) {
//                jobUpdate.setDate(job.getDate());
//            }
//            if (job.getJobStatus() != null) {
//                jobUpdate.setJobStatus(job.getJobStatus());
//            }
//            if (job.getPaymentOption() != null) {
//                jobUpdate.setPaymentOption(job.getPaymentOption());
//            }
//            jobRepository.save(jobUpdate);
//            return Optional.of(jobUpdate);
//        } else {
//            throw new JobDoesNotExistException("Job does not exist");
//        }
//    }


    public List<Job> getJobsByStatus(List<JobStatus> statuses) {
        return jobRepository.findByJobStatusIn(statuses);
    }
}
