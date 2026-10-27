package in.ashokit.app.service;

import in.ashokit.app.entity.Employee;
import in.ashokit.app.repository.EmployeeRepsitory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    EmployeeRepsitory emprepo;

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

   public  EmployeeService(EmployeeRepsitory emprepo){
       this.emprepo = emprepo;
   }

   @CachePut(value = "employee" , key = "#employee.empno")
   public Employee empSave(Employee employee){

       log.info("Saving Employee ; id ={}" , employee.getEmpno());

       Employee savedEmployee = emprepo.save(employee);

       log.info("Employee saved Successfully: id={}", savedEmployee.getEmpno());

       return  savedEmployee;
   }

   @Cacheable(value = "employee" , key = "#id", unless = "#result == null")
   public Employee findByid(Integer id){

       log.info("Fetching Employee ; id={}", id);

       Employee employee =   emprepo.findById(id).orElse(null);
       if (employee == null)
           log.warn("Employee not found; id={}" , id);

       return employee;
   }


   public List<Employee> findall(){
       log.info("Fetching all Employess : ");
       return (List<Employee>) emprepo.findAll();
   }

   @CacheEvict(value = "employee", allEntries = true)
   public void delete(Integer id){
       log.info("Deleting Employee ; id={}", id);

       emprepo.deleteById(id);

       log.info("Employee Deleted successfully ; id={}", id);

   }

   public Employee updateBySal(Integer empno , Double sal){
       emprepo.updateSal(empno , sal);
       return emprepo.findById(empno).orElse(null);
   }
}
