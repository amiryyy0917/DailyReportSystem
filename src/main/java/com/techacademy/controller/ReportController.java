package com.techacademy.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;

    }

    // 日報一覧画面
    @GetMapping
    public String list(Model model,@AuthenticationPrincipal UserDetail userDetail) {





     // 空のReport型のreportListを作成
        List<Report> reportList = new ArrayList<Report>();

        // ログイン中の従業員情報を取得
        Employee e = userDetail.getEmployee();

        // 権限が一般の場合(if)
        if (e.getRole() == Employee.Role.GENERAL) {

        	// rというReport型のListを作成し、reportServiceのfindAllメソッドを指定
        	List<Report> r = reportService.findAll();
        	// rをReport reportでループ(拡張for文)
            for(Report report:r) {

            	// reportから取得したemployeeのcodeを取得
                String code = report.getEmployee().getCode();
                // codeとログイン中の従業員情報のcodeが一致した場合(if)
                if(code.equals (e.getCode())) {
                	// reportListへaddを利用してreportを追加
                	 reportList.add(report);
                     model.addAttribute("reportList",report);
                     model.addAttribute("listSize",reportList.size());
                }

            }
         // 権限が一般以外の場合(else)
        }else {
        	// reportListへreportServiceのfindAllメソッドを指定
        	model.addAttribute("reportList", reportService.findAll());
        	model.addAttribute("listSize", reportService.findAll().size());
        }



        return "reports/list";
    }

    // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable("id")int id, Model model) {

        model.addAttribute("report", reportService.findByCode(id));
        return "reports/detail";
    }

    //日報更新画面
    @GetMapping(value="/{id}/update")
    public String edit(@PathVariable("id")int id,Model model,Report report){

    	model.addAttribute("report", reportService.findByCode(id));

    	return "reports/update";
    }




    //日報更新処理
    @PostMapping(value = "/{id}/update")
    public String update(@Validated Report report, BindingResult res, Model model,@PathVariable("id")int id,@AuthenticationPrincipal UserDetail userDetail) {



        //入力チェック
        if (res.hasErrors()) {
            return edit(id,model,report);
        }

        // 論理削除を行った従業員番号を指定すると例外となるためtry~catchで対応
        // (findByIdでは削除フラグがTRUEのデータが取得出来ないため)
        try {
            ErrorKinds result = reportService.update(report,id,userDetail);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return edit(id,model,report);
            }

        } catch (DataIntegrityViolationException e) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
            return edit(id,model,report);
        }

        return "redirect:/report";
    }







    //日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report,Model model,@AuthenticationPrincipal UserDetail userDetail) {



    	Report reportName = new Report();
        reportName.setEmployee(userDetail.getEmployee());

        model.addAttribute("employeeName",reportName.getEmployee().getName());


        return "reports/new";
    }

    // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res, Model model,@AuthenticationPrincipal UserDetail userDetail) {


         //入力チェック
        if (res.hasErrors()) {
            return create(report,model,userDetail);
        }

        // 論理削除を行った従業員番号を指定すると例外となるためtry~catchで対応
        // (findByIdでは削除フラグがTRUEのデータが取得出来ないため)
        try {
            ErrorKinds result = reportService.save(report,userDetail);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return create(report,model,userDetail);
            }

        } catch (DataIntegrityViolationException e) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
            return create(report,model,userDetail);
        }

        return "redirect:/reports";
    }

    // 従業員削除処理
    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable("id")int id, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        ErrorKinds result = reportService.delete(id, userDetail);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("employee", reportService.findByCode(id));
            return detail(id, model);
        }

        return "redirect:/reports";
    }


}



