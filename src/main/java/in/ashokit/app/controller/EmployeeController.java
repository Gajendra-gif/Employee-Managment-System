package in.ashokit.app.controller;

import in.ashokit.app.Exception.ResourceNotFound;
import in.ashokit.app.entity.Employee;
import in.ashokit.app.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class EmployeeController {

    EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService){

        this.employeeService = employeeService;
    }

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @GetMapping(value ="/employees" , produces = "application/json")
    public ResponseEntity<List<Employee>> returnAllEmployee(){

        log.info("Get All Employees request recieved");

        List<Employee> employees = employeeService.findall();

        log.info("Get all Employees completed; count ={}", employees.size());

       return new ResponseEntity<>(employees , HttpStatus.OK);
    }

    @GetMapping(value = "/employees/{id}" , produces = {"application/json", "application/xml"})
    public ResponseEntity<Employee> returnEmployee(@PathVariable("id") Integer id){
        Employee employee = employeeService.findByid(id);
        if(employee == null){
            throw new ResourceNotFound("Employee not found with this Id "+id);
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employee , HttpStatus.OK);
    }

    @PostMapping(value = "/save" , produces = "application/json")
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee1){
        try{
            Employee saveEmp = employeeService.empSave(employee1);
            return  new ResponseEntity<>(saveEmp , HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/update" , produces = "application/json")
    public ResponseEntity<Employee> updateEmp(@Valid @RequestBody Employee employee){
        Employee empDb = employeeService.findByid(employee.getEmpno());
        if(empDb != null){
            Employee updateEmp = employeeService.empSave(empDb);
            return ResponseEntity.ok(updateEmp);
        }
        else{
            throw new ResourceNotFound("Employee not found with this Id "+ employee.getEmpno());
            //return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/update/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Employee> updateBySal( @PathVariable Integer id , @RequestBody Map<String , Object> map){
        Employee emp1 = employeeService.findByid(id);
        if(emp1 != null){
            Number sal = (Number) map.get("sal");
            Double salary = sal.doubleValue();
            Employee updateEmp1 = employeeService.updateBySal(id , salary);
            return ResponseEntity.ok(updateEmp1);
        }
        else{
            throw new ResourceNotFound("Employee not found with this Id "+id);
            //  ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
    public ResponseEntity<Employee> deleteById(@PathVariable Integer id){
        Employee employee = employeeService.findByid(id);
        if(employee != null){
            employeeService.delete(id);
            return ResponseEntity.noContent().build();
        }
        else {
            throw new ResourceNotFound("Employee not found with this Id "+id);
            //return ResponseEntity.badRequest().build();
        }
    }

 /*   @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)

    public Map<String , String> handleException(MethodArgumentNotValidException ex){

        Map<String , String> map = new HashMap<>();
        List<FieldError> list = ex.getBindingResult().getFieldErrors();

        list.forEach(error -> {
            map.put(error.getField() , error.getDefaultMessage());
        });

        return map;
    }*/
}

