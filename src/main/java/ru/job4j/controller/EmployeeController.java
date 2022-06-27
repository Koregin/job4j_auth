package ru.job4j.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.Employee;
import ru.job4j.domain.Person;
import ru.job4j.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final RestTemplate restTemplate;
    private final EmployeeRepository employeeRepository;

    private static final String API = "http://localhost:8080/person/";
    private static final String API_ID = "http://localhost:8080/person/{id}";

    public EmployeeController(RestTemplate restTemplate, EmployeeRepository employeeRepository) {
        this.restTemplate = restTemplate;
        this.employeeRepository = employeeRepository;
    }

    /*
    Get all employees with their accounts (class Person)
     */
    @GetMapping("/")
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employeeRepository.findAll().forEach(employees::add);
        List<Person> accounts = restTemplate.exchange(
                API, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Person>>() {
                }).getBody();
        for (Employee employee : employees) {
            employee.setAccounts(accounts.stream()
                    .filter(person -> person.getEmployeeId() == employee.getId())
                    .collect(Collectors.toList()));
        }

        return employees;
    }

    /*
    Create new account for employee
     */
    @PostMapping("/{empId}/person")
    public ResponseEntity<Person> createPersonForEmployee(@PathVariable int empId, @RequestBody Person person) {
        var employee = employeeRepository.findById(empId);
        person.setEmployeeId(employee.get().getId());
        Person result = restTemplate.postForObject(API, person, Person.class);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /*
    Update account for employee
     */
    @PutMapping("/{empId}/person")
    public ResponseEntity<Void> updatePersonOnEmployee(@PathVariable int empId, @RequestBody Person person) {
        System.out.println("Обновление сотруднка");
        var employee = employeeRepository.findById(empId);
        ResponseEntity<Void> response = ResponseEntity.noContent().build();
        if (employee.get().getId() == person.getEmployeeId()) {
            restTemplate.put(API, person);
            response = ResponseEntity.ok().build();
        }
        return response;
    }

    /*
    Delete account for employee
     */
    @DeleteMapping("/person/{id}")
    public ResponseEntity<Void> deletePersonOnEmployee(@PathVariable("id") int id) {
        System.out.println("Удаление сотрудника");
        restTemplate.delete(API_ID, id);
        return ResponseEntity.ok().build();
    }
}
