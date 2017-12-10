package com.kaishengit.crm.controller;

import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.SaleChanceService;
import com.kaishengit.web.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by hoyt on 2017/11/18.
 */

@Controller
@RequestMapping("/charts")
public class ChartCotroller {

    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    public String chartList() {
        return "charts/list";
    }

    @GetMapping("/list/bar.json")
    @ResponseBody
    public AjaxResult loadBarData() {
        List<Map<String,Object>> progressCountMap = saleChanceService.findProgressCount();
        return AjaxResult.successWithData(progressCountMap);
    }

    @GetMapping("/list/newCustomer/count")
    @ResponseBody
    public AjaxResult newCustomerCount() {
        return AjaxResult.successWithData(customerService.countByCreateTime());
    }

}
