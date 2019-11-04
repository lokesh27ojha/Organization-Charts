package com.whitedeath.organizationchart.Repository;

import com.whitedeath.organizationchart.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
    List<Employee> getEmployeesByManagerId(Integer managerId);

    List<Employee> getEmployeesByManagerIdAndIdNotLike(Integer managerId, Integer id);

    @Modifying
    @Query("update Employee emp set emp.managerId =?1 where emp.managerId =?2")
    void updateEmployee(Integer newManagerId, Integer oldManagerId);

}
