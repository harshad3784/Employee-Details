package com.employee.controller;


import com.employee.model.Employee;
import com.employee.model.EmployeeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

@RestController
public class EmployeeController
{

    private static Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private static final AtomicInteger count = new AtomicInteger(0);
    private static int MAX_EXPIRE = 600 * 1000;
    private static String MAX_CALL_ALLOWED = "500";
    Map<String, EmployeeSession> sessionInfo = new HashMap();
    static Set<Employee> employees = new HashSet<>();

    static {
        // initalize a default Employee

        Employee defaultEmployee = new Employee();
        defaultEmployee.setEmployeeName("employee1");
        defaultEmployee.setFirstName("jhon");
        defaultEmployee.setLastName("wesley");
        defaultEmployee.setEmail("employee1@klm.nl");
        defaultEmployee.setPassword("welcome");
        defaultEmployee.setPhone("6434635128");
        defaultEmployee.setEmployeeStatus(1);
        defaultEmployee.setId(count.incrementAndGet());
        employees.add(defaultEmployee);
    }


    @GetMapping("/")
    public String getDefaultMesasge(){
        return "Hello, all API tests were successful";
    }
    
    
    @PostMapping("/v2/employee")
    public ResponseEntity createEmployee(@Valid @RequestBody Employee employee,@Valid  @RequestHeader("X-Api-Key" ) String key) {
    	

     String validateSession = validateAPIKey(key);
      if(validateSession.equalsIgnoreCase("SESSION KEY VALID")) {
          if (employee.getEmployeeName() != null && !isEmployeeExists(employee.getEmployeeName())) {
              employees.add(employee);
              return ResponseEntity.ok().body("Employee Successfully Created");

          } else {
              return ResponseEntity.badRequest().body("employeeName Already taken");
          }
      }else{
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validateSession);
      }
    }

    @PostMapping(value = {"/v2/employee/createWithArray", "/employee/createWithList",})
    public ResponseEntity createEmployees(@Valid @RequestBody Set<Employee> employee,  @RequestHeader("X-Api-Key") String key) {
        String validateSession = validateAPIKey(key);
        if(validateSession.equalsIgnoreCase("SESSION KEY VALID")) {
            employees.addAll(processEmployees(employee));
            return ResponseEntity.ok().body("Employees Successfully Created");
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validateSession);
        }
    }


    @GetMapping("/v2/employee/login")
    public ResponseEntity login (@RequestParam(value ="employeename" , required = true ) String employeeName , @RequestParam(value ="password" , required = true ) String password){

        if(isEmployeeExists(employeeName)  ){
            if(isPasswordMatch(employeeName,password)){
                clearExistingSession(employeeName);
                log.debug("Create Session");
                String sessionId = UUID.randomUUID().toString();
                EmployeeSession session = new EmployeeSession();
                session.setCreatedTime(new Timestamp(System.currentTimeMillis()));
                session.setSessionId(sessionId);
                session.setEmployeeName(employeeName);
                sessionInfo.put(sessionId,session);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
                headers.add("X-Expires-After",new Timestamp(session.getCreatedTime().getTime() + MAX_EXPIRE ).toString());
                headers.add("X-Rate-Limit", MAX_CALL_ALLOWED);
                return ResponseEntity.ok().headers(headers).body(sessionId);
            }else{
                return ResponseEntity.badRequest().body(" Password incorrect  " +employeeName );
            }

        }
        else{
            return ResponseEntity.badRequest().body("No employee found for  " +employeeName );
        }
    }


    @GetMapping("/v2/employee/logout")
    public ResponseEntity logout (@RequestHeader("X-Api-Key") String key){

        clearExistingSession(key);
        return ResponseEntity.accepted().body("Logged Out");
    }


    @GetMapping("/v2/employee/{employeename}")
    public ResponseEntity getemployeeByName (@PathVariable( value = "employeename" , required = true) String employeeName,@RequestHeader("X-Api-Key") String key){
        String validateSession = validateAPIKey(key);
        if(validateSession.equalsIgnoreCase("SESSION KEY VALID")) {
            if (isEmployeeExists(employeeName)) {
              return  ResponseEntity.ok(employees.stream().filter(x -> x.getEmployeeName().equalsIgnoreCase(employeeName)).findFirst().get()) ;
            } else {
                return ResponseEntity.notFound().build();
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validateSession);
        }
    }

   @PutMapping("/v2/employee/{employeename}")
    public ResponseEntity updateEmployee(@PathVariable( value = "employeename" , required = true) String employeeName , @RequestBody Employee employee,@RequestHeader("X-Api-Key") String key){
       String validateSession = validateAPIKey(key);
       if(validateSession.equalsIgnoreCase("SESSION KEY VALID")) {
           if (isEmployeeExists(employeeName)) {
               Employee oldEmployee =  employees.stream().filter(x -> x.getEmployeeName().equalsIgnoreCase(employeeName)).findFirst().get();
               employees.remove(oldEmployee);
               employees.add(employee);
               return  ResponseEntity.ok().body("Employe details updated");
           } else {
               return ResponseEntity.badRequest().body("No employee found for  " + employeeName );
           }
       }else{
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validateSession);
       }
   }


   @DeleteMapping("/v2/employee/{employeename}")
   public ResponseEntity deleteEmployee(@PathVariable( value = "employeename" , required = true) String employeeName , @RequestHeader("X-Api-Key") String key){
       String validateSession = validateAPIKey(key);
       if(validateSession.equalsIgnoreCase("SESSION KEY VALID")) {
           if (isEmployeeExists(employeeName)) {
               Employee oldEmployee =  employees.stream().filter(x -> x.getEmployeeName().equalsIgnoreCase(employeeName)).findFirst().get();
               employees.remove(oldEmployee);
               return  ResponseEntity.accepted().build();
           } else {
               return ResponseEntity.badRequest().body("No employee found for  " + employeeName );
           }
       }else{
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validateSession);
       }

   }



   private boolean isEmployeeExists(String employeeName){

       return  employees.stream().map(Employee::getEmployeeName).filter(employeeName::equalsIgnoreCase).findFirst().isPresent();
   }


   private boolean isPasswordMatch(String employeeName,String password){
       return employees.stream().filter(x -> x.getEmployeeName().equalsIgnoreCase(employeeName)).findFirst().get().getPassword().equals(password);
   }

   private String validateAPIKey(String key){
       if(sessionInfo.get(key)!=null){
           EmployeeSession session = sessionInfo.get(key);
           long diff = session.getCreatedTime().getTime() - System.currentTimeMillis() ;
          if((session.getCreatedTime().getTime() - System.currentTimeMillis()) < MAX_EXPIRE){

              return "SESSION KEY VALID";
          }else{
              return "SESSION KEY EXPIRED";
          }
       }else{
           return "INVALID_KEY";
       }


   }


   private void clearExistingSession(String employeeName){
       Optional<EmployeeSession> optionalEmployeeSession = sessionInfo.values().stream().filter(x -> x.getEmployeeName().equalsIgnoreCase(employeeName)).findFirst();
       if(optionalEmployeeSession.isPresent()){
           sessionInfo.remove(  optionalEmployeeSession.get().getSessionId());
       }
   }


   private Set<Employee> processEmployees(Set<Employee> employees){
       employees.removeIf( x -> isEmployeeExists(x.getEmployeeName()));
       employees.forEach(x -> x.setId(count.incrementAndGet()));
        return employees;
   }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public String apikeyError(MissingRequestHeaderException ex) {

        return ex.getMessage();
    }
}