package com.example.cleancode.job;

import com.example.cleancode.booked.Booked;
import com.example.cleancode.booked.BookedRepository;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.Role;
import com.example.cleancode.enums.TimeSlots;
import com.example.cleancode.exceptions.JobDoesNotExistException;
import com.example.cleancode.exceptions.NoJobsForCustomerException;
import com.example.cleancode.exceptions.NoJobsForEmploeyyException;
import com.example.cleancode.exceptions.PersonDoesNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;
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
        /**
         * TODO: Se till så att en employee slumpas fram, så inte Employee med id 1 får alla jobb
         */

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


    public HashMap<Integer, Boolean> getAvailableEmployees(LocalDateTime date, int lookForAvailableThisManyHours) {
        HashMap<Long, List<TimeSlots>> availableEmployeesMap = new HashMap<>();

        List<List<Employee>> employeeListList = new ArrayList<>();
        List<Employee> empList = new ArrayList<>();
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


//            employeeListList.get(i).stream().forEach(x -> System.out.println("employee: " + x.getFirstName()));

            /**
            (
             EIGHT:     (LISTA1   - emp1, emp2, emp5, emp8)
            NINE:       (LISTA2   - emp2, emp3, emp5, emp8)
            TEN:        (LISTA3   - emp1, emp3, emp5, emp9)
            ELEVEN:     (LISTA4   - emp1, emp3, emp5, emp9)
             )
            **/

//            List<Employee> employees = employeeRepository.findUnbookedEmployees(date, timeSlotList);
        }
        employeeListList.add(List.of());
        employeeListList.add(List.of());

//        List<Employee> employeeList = employees
//                .stream()
//                .filter(x -> x.getRole().equals(Role.EMPLOYEE))
//                .toList();

        boolean eight = false;
        boolean nine = false;
        boolean ten = false;
        boolean eleven = false;
        boolean twelve = false;
        boolean thirteen = false;
        boolean fourteen = false;
        boolean fifteen = false;
        boolean sixteen = false;

        HashMap<Integer, Boolean> boolMap = new HashMap<>();
        boolMap.put(8, eight);
        boolMap.put(9, nine);
        boolMap.put(10, ten);
        boolMap.put(11, eleven);
        boolMap.put(12, twelve);
        boolMap.put(13, thirteen);
        boolMap.put(14, fourteen);
        boolMap.put(15, fifteen);
        boolMap.put(16, sixteen);


        for(int i = 0; i < employeeListList.size(); i++) {
//            System.out.println("yttre listan, nummer " + i + " " +employeeListList.get(i));

        /**
            Loopar igenom yttersta listan, som innehåller listor med tillgängliga employees ett visst klockslag och datum
        **/
            for(int j = 0; j < employeeListList.get(i).size(); j++) {
//                System.out.println("inre listan, nummer " + j + " " + employeeListList.get(i).get(j));

                /**
                 Loopar igenom inre listorna, som innehåller tillgängliga employees ett visst klockslag och datum
                 **/
                for(int k = 0; k < lookForAvailableThisManyHours; k++) {

                    /**
                     loopar lookForAvailableThisManyHours gånger, kollar så många listor framåt
                    **/

                    if(employeeListList.get(i + k).contains(employeeListList.get(i).get(j)) ) {
                        boolMap.put(i+8, true);
                    }
                }
             }
        }

//        for(int i = 0; i < employeeListList.size(); i++) {
//            for(int j = 0; j < employeeListList.get(i).size(); j++) {
//                System.out.println(employeeListList.get(i).get(j).getFirstName());
//                for(int k = 0; k < lookForAvailableThisManyHours; k++) {
//                    if(employeeListList.get(i+k).contains(employeeListList.get(i).get(j))) {
//                        boolMap.put(i, true);
//                    }
//                }
//            }
//        }

        System.out.println(boolMap);

        return boolMap;
    }





    public Long createJob(CreateJobDTO createJobDTO) {

        LocalDateTime date = LocalDateTime.parse(createJobDTO.getDate() + "T00:00:00");
        TimeSlots timeSlot = createJobDTO.getTimeSlotList().get(0);
        List<Employee> unbookedEmployees = findUnbookedEmployees(date, createJobDTO.getTimeSlotList());

        if (unbookedEmployees.isEmpty()) {
            throw new RuntimeException("No employees are available for this time slot.");
        }
        Employee assignedEmployee = unbookedEmployees.get(0);

//        Optional<Employee> assignedEmployee = employeeRepository.(createJobDTO.getEmpId());
//        if(assignedEmployee.isEmpty()) {
//            throw new PersonDoesNotExistException("That employee does not exist!");
//        }

        Job job = new Job(
                createJobDTO.getJobtype(),
                date,
                timeSlot,
                JobStatus.PENDING,
                createJobDTO.getSquareMeters(),
                createJobDTO.getPaymentOption(),
                assignedEmployee,
                customerRepository.findById(createJobDTO.getCustomerId()).get());
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

    public Optional<List<Job>> getAllJobsForEmployee(Long empId) {

        List<Job> optAllJobsForEmp = jobRepository.findAll()
                .stream()
                .filter(job -> job.getEmployee().getId() == empId)
                .collect(Collectors.toList());

        if (!optAllJobsForEmp.isEmpty()) {
            return Optional.of(optAllJobsForEmp);
        } else {
            throw new NoJobsForEmploeyyException("There is no jobs for this employee");
        }
    }

    public Optional<List<Job>> getAllJobsForCustomer(Long cusId) {

        List<Job> optAllJobsForCustomer = jobRepository.findAll()
                .stream()
                .filter(job -> job.getCustomer().getId() == cusId)
                .collect(Collectors.toList());

        if (!optAllJobsForCustomer.isEmpty()) {
            return Optional.of(optAllJobsForCustomer);
        } else {
            throw new NoJobsForCustomerException("There is no jobs attached to this this customer");
        }
    }

    @Transactional
    public Optional<Job> updateJobInfo(Long id, Job job) {

        Optional<Job> optionalJob = jobRepository.findById(id);

        // Do you need to be able to update squareMeters?
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

    public Optional<List<Job>> getJobByStatus(JobStatus status) {
        return Optional.ofNullable(jobRepository.findByJobStatus(status));
    }
}
