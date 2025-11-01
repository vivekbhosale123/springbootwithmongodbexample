package com.vdb.controller;

import com.vdb.exception.RecordNotFoundException;
import com.vdb.model.Employee;
import com.vdb.service.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @PostMapping("/signup")
    public ResponseEntity<Employee> signUp(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.signUp(employee));
    }

    @PostMapping("/savebulkdata")
    public ResponseEntity<List<Employee>> saveBulkData(@RequestBody List<Employee> employeeList) {
        return ResponseEntity.ok(employeeService.saveAll(employeeList));
    }

    @GetMapping("signin/{empEmailId}/{empPassword}")
    public ResponseEntity<Boolean> signIn(@PathVariable String empEmailId, @PathVariable String empPassword) {
        return ResponseEntity.ok(employeeService.signIn(empEmailId, empPassword));
    }

    @GetMapping("/findbyid/{empId}")
    public ResponseEntity<Optional<Employee>> findById(@PathVariable int empId) {
        return ResponseEntity.ok(employeeService.findById(empId));
    }

    @GetMapping("/findbyname/{empName}")
    public ResponseEntity<List<Employee>> findByName(@PathVariable String empName) {
        return ResponseEntity.ok(employeeService.findByEmpName(empName));
    }

    @GetMapping("/findbycontactnumber/{empContactNumber}")
    public ResponseEntity<Employee> findByContactNumber(@PathVariable long empContactNumber) {
        return ResponseEntity.ok(employeeService.findAll().stream().filter(emp -> emp.getEmpContactNumber() == empContactNumber).toList().get(0));
    }

    @GetMapping("/findbyemailid/{empEmailId}")
    public ResponseEntity<Employee> findByEmailId(@PathVariable String empEmailId) {
        return ResponseEntity.ok(employeeService.findAll().stream().filter(emp -> emp.getEmpEmailId().equals(empEmailId)).toList().get(0));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Employee>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/findbyanyinput/{input}")
    public ResponseEntity<List<Employee>> findByAnyInput(@PathVariable String input) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");

        return ResponseEntity.ok(employeeService.findAll().stream().filter(emp -> emp.getEmpName().equals(input)
                || String.valueOf(emp.getEmpId()).equals(input)
                || String.valueOf(emp.getEmpContactNumber()).equals(input)
                || emp.getEmpEmailId().equals(input)
                || simpleDateFormat.format(emp.getEmpDob()).equals(input)).toList());
    }

    @GetMapping("/sortbydob")
    public ResponseEntity<List<Employee>> sortByDob() {
        return ResponseEntity.ok(employeeService.findAll().stream().sorted(Comparator.comparing(Employee::getEmpDob)).toList());
    }


    @GetMapping("/sortbyname")
    public ResponseEntity<List<Employee>> sortByName() {
        return ResponseEntity.ok(employeeService.findAll().stream().sorted(Comparator.comparing(Employee::getEmpName)).toList());
    }


    @GetMapping("/sortbysalary")
    public ResponseEntity<List<Employee>> sortBySalary() {
        return ResponseEntity.ok(employeeService.findAll().stream().sorted(Comparator.comparing(Employee::getEmpSalary)).toList());
    }

    @GetMapping("/checkloaneligibility/{empId}")
    public ResponseEntity<String> checkLoanEligibility(@PathVariable int empId) {
        String msg = "";

        Employee employee1 = employeeService.findById(empId).orElseThrow(() -> new RecordNotFoundException("Employee Id Not Found"));

        if (employee1.getEmpSalary() >= 5000.000) {
            msg = "Eligible for loan";
        } else {
            msg = "not Eligible for loan";
        }

        return ResponseEntity.ok(msg);
    }


    @PutMapping("/update/{empId}")
    public ResponseEntity<Employee> update(@PathVariable int empId, @RequestBody Employee employee) {
        Employee employee1 = employeeService.findById(empId).orElseThrow(() -> new RecordNotFoundException("Employee Id Not Found"));

        employee1.setEmpName(employee.getEmpName());
        employee1.setEmpAddress(employee.getEmpAddress());
        employee1.setEmpSalary(employee.getEmpSalary());
        employee1.setEmpContactNumber(employee.getEmpContactNumber());
        employee1.setEmpDob(employee.getEmpDob());
        employee1.setEmpEmailId(employee.getEmpEmailId());
        employee1.setEmpPassword(employee.getEmpPassword());

        return ResponseEntity.ok(employeeService.update(employee1));
    }

    @PatchMapping("/changeaddress/{empId}/{empAddress}")
    public ResponseEntity<Employee> changeAddress(@PathVariable int empId, @PathVariable String empAddress) {
        Employee employee1 = employeeService.findById(empId).orElseThrow(() -> new RecordNotFoundException("Employee Id Not Found"));

        employee1.setEmpAddress(empAddress);

        return ResponseEntity.ok(employeeService.update(employee1));
    }

    @DeleteMapping("/deletebyid/{empId}")
    public ResponseEntity<String> deleteById(@PathVariable int empId) {
        employeeService.deleteById(empId);

        return ResponseEntity.ok("employee deleted successfully");
    }

    @DeleteMapping("/deleteall/{empId}")
    public ResponseEntity<String> deleteAll() {
        employeeService.deleteAll();

        return ResponseEntity.ok("all employee deleted successfully");
    }

}
