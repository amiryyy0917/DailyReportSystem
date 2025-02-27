package service;

import java.util.Optional;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import entity.Employee;
import repository.EmployeeRepository;

@Service
public class UserDetailService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    public UserDetailService(EmployeeRepository repository) {
        this.employeeRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(username);

        if (employee.isEmpty()) {
            throw new UsernameNotFoundException("Exception:Username Not Found");
        }
        return new UserDetail(employee.get());
    }
}