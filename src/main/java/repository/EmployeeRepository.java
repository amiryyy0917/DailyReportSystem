package repository;

import org.springframework.data.jpa.repository.JpaRepository;


import entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
}