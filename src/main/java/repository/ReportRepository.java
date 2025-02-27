package repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import entity.Report;

public interface ReportRepository extends JpaRepository<Report,Integer> {

	//List<Report> findById(Integer ID);

}