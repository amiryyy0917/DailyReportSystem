package com.techacademy.service;

import java.time.LocalDateTime;


import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

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
    public ErrorKinds save(Report report) {



        // 従業員番号重複チェック
        if (findByCode(report.getId()) != null) {
            return ErrorKinds.DUPLICATE_ERROR;
        }

        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }



    // 従業員削除
    @Transactional
    public ErrorKinds delete(int ID, UserDetail userDetail) {


        Report report = findByCode(ID);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

    // 従業員一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 1件を検索
    public Report findByCode(Integer ID) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(ID);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }




   //従業員更新
    @Transactional


    public ErrorKinds update(Report report,int ID) {




        report.setDeleteFlg(false);



        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);


        return ErrorKinds.SUCCESS;


    }



}