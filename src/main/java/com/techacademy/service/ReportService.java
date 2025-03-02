package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;


import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;


    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;

    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Report report,@AuthenticationPrincipal UserDetail userDetail) {

    	//従業員情報
    	Employee log=userDetail.getEmployee();
    	//入力された日付



    	List<Report> s =reportRepository.findByEmployeeAndReportDate(log,report);



        //ログイン中の従業員かつ入力した日付重複チェック
    	if( findByEmployeeAndReportDate(log.getCode(), report.getReportDate())){
            return ErrorKinds.DATECHECK_ERROR;
        }



        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }


    private boolean findByEmployeeAndReportDate(String code, LocalDate reportDate) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	// 従業員削除
    @Transactional
    public ErrorKinds delete(int id, UserDetail userDetail) {


        Report report = findByCode(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {

    	/*Employee employeeReport = new Employee();
    	employeeReport.setCode(report.getCode());*/



        return reportRepository.findAll();
    }



    // 1件を検索
    public Report findByCode(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    /*従業員に紐づいた日報情報を検索
    public List<Report> getByEmployee(Employee employee){

    	return reportRepository.findByEmployee(employee);
    }*/



   //従業員更新
    @Transactional


    public ErrorKinds update(Report report,int id,@AuthenticationPrincipal UserDetail userDetail) {

    	//従業員情報
    	Employee log=userDetail.getEmployee();
    	//入力された日付



    	List<Report> s =reportRepository.findByEmployeeAndReportDate(log,report);



        //ログイン中の従業員かつ入力した日付重複チェック
    	if( findByEmployeeAndReportDate(log.getCode(), report.getReportDate())){
            return ErrorKinds.DATECHECK_ERROR;
        }





        report.setDeleteFlg(false);



        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);


        return ErrorKinds.SUCCESS;


    }



}