package com.kaishengit.crm.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.controller.exception.ForbideenException;
import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.SaleChance;
import com.kaishengit.crm.entity.SaleChanceRecord;
import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.SaleChanceService;
import com.kaishengit.crm.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * Created by hoyt on 2017/11/13.
 */

@Controller
@RequestMapping("/record")
public class RecordController extends BaseController{

    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TaskService taskService;

    /**
     * 我的记录
     * @return
     */
    @GetMapping("/my")
    public String myRecord(@RequestParam(name = "p",required = false,defaultValue = "1") Integer pageNo,
                           HttpSession session,Model model) {
        Account account = getCurrAccount(session);
        PageInfo<SaleChance> pageInfo = saleChanceService.pageForSaleChance(pageNo,account);
        model.addAttribute("pageInfo",pageInfo);
        return "record/my";
    }


    /**
     * 新增工作记录
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/new")
    public String saveNewRecord(Model model,HttpSession session) {
        Account account = getCurrAccount(session);
        model.addAttribute("customerList",customerService.findCustomerByAccountId(account));
        model.addAttribute("progressList",saleChanceService.findAllProgress());
        return "record/new";
    }

    @PostMapping("/new")
    public String saveNewRecord(SaleChance saleChance, RedirectAttributes redirectAttributes) {
        saleChanceService.saveNewRecord(saleChance);
        redirectAttributes.addFlashAttribute("message","新增成功");
        return "redirect:/record/my";
    }

    /**
     * 工作记录详情
     * @param id
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/my/{id:\\d+}")
    public String showRecord(@PathVariable Integer id,Model model,HttpSession session) {
        SaleChance saleChance = validateSaleChance(id,session);
        Account account = getCurrAccount(session);
        model.addAttribute("taskList",taskService.findTaskBySaleId(id));
        model.addAttribute("recordList",saleChanceService.findSalesChanceRecodeListBySalesId(id));
        model.addAttribute("saleChance",saleChance);
        model.addAttribute("progressList",saleChanceService.findAllProgress());
        return "record/show";
    }

    private SaleChance validateSaleChance(@PathVariable Integer id, HttpSession session) {
        Account account =getCurrAccount(session);
        SaleChance saleChance = saleChanceService.findSalesChanceWithCustomerById(id);
        if(saleChance == null) {
            //404
            throw new NotFoundException();
        }
        if(!saleChance.getAccountId().equals(account.getId())) {
            //403
            throw new ForbideenException();
        }
        return saleChance;
    }

    /**
     * 更新进度
     * @param id
     * @param progress
     * @return
     */
    @GetMapping("/my/{id:\\d+}/changeProgress")
    public String changeProgress(@PathVariable Integer id, String progress) {
        System.out.println(progress);
        saleChanceService.changeProgress(id,progress);
        return "redirect:/record/my/" + id;
    }

    /**
     * 改变工作记录跟进状态
     * @return
     */
    @PostMapping("/my/{id:\\d+}/progress/update")
    public String updateSaleChanceState(@PathVariable Integer id,String progress,HttpSession session) {
        validateSaleChance(id, session);
        saleChanceService.updateSalesChanceState(id,progress);
        return "redirect:/record/my/"+id;
    }

    /**
     * 添加新的跟进记录
     * @param record
     * @return
     */
    @PostMapping("/my/new/record")
    public String saveNewSaleChanceRecode(SaleChanceRecord record, HttpSession session) {
        validateSaleChance(record.getSaleId(), session);
        saleChanceService.saveNewSalesChanceRecode(record);
        return "redirect:/record/my/"+record.getSaleId();
    }


    /**
     * 删除工作记录
     * @param id
     * @param redirectAttributes
     * @param session
     * @return
     */
    @GetMapping("/my/{id:\\d+}/delete")
    public String deleteSalesChance(@PathVariable Integer id,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {
        validateSaleChance(id, session);
        saleChanceService.deleteSalesChanceById(id);
        return "redirect:/record/my";
    }

    /**
     * 添加待办事项
     * @param task
     * @return
     */
    @PostMapping("/my/{saleId:\\d+}/task/new")
    public String newTaskToSaleChance(Task task) {
        taskService.saveNewTask(task);
        return "redirect:/record/my/" + task.getSaleId();
    }

    /**
     * 公海记录
     * @return
     */
    @GetMapping("/public")
    public  String publicRecord() {
        return "record/public";
    }


}
