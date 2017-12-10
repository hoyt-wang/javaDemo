package com.kaishengit.crm.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.controller.exception.ForbideenException;
import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.SaleChance;
import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.example.CustomerExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.service.AccountService;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.SaleChanceService;
import com.kaishengit.crm.service.TaskService;
import com.kaishengit.web.result.AjaxResult;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by hoyt on 2017/11/9.
 */

@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController{

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private TaskService taskService;

    /**
     * 客户列表
     * @param pageNo
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/my")
    public String my(@RequestParam(name = "p",required = false,defaultValue = "1") Integer pageNo,
                     HttpSession session, Model model) {
        Account account = getCurrAccount(session);
        PageInfo<Customer> pageInfo = customerService.pageForMyCustomer(pageNo,account);
        model.addAttribute("pageInfo",pageInfo);
        return "customer/my";
    }

    /**
     * 添加新客户
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String saveNewCustomer(Model model) {
        model.addAttribute("trades",customerService.findAllCustomerTrade());
        model.addAttribute("sources",customerService.findAllCustomerSource());
        return "customer/new";
    }

    @PostMapping("/new")
    public String saveNewCustomer(Customer customer, RedirectAttributes redirectAttributes) {
        try {
            customerService.saveNewCustomer(customer);
            redirectAttributes.addFlashAttribute("message","新增员工成功");
            return "redirect:/customer/my";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "customer/new";
        }
    }


    /**
     * 客户详情
     * @param id
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/my/{id:\\d+}")
    //@RequestMapping(value = "/{id\\d+}", method = RequestMethod.GET)
    public String showCustomer(@PathVariable Integer id, Model model,HttpSession session) {
        Customer customer = validateCustomer(id, session);
        model.addAttribute("taskList",taskService.findTaskByCustId(id));
        model.addAttribute("saleChanceList",saleChanceService.findSalesChanceByCustId(id));
        model.addAttribute("accountList",accountService.findAllAccount());
        model.addAttribute("customer",customer);
        return "customer/show";
    }

    /**
     * 验证客户是否属于当前登录的对象
     * @param id
     * @param session
     * @return
     */
    private Customer validateCustomer(@PathVariable Integer id, HttpSession session) {
        //根据ID查找客户
        Customer customer = customerService.findById(id);
        if(customer == null) {
            //404
            throw new NotFoundException("找不到" + id + "对应的客户");
        }
        Account account = getCurrAccount(session);
        if(!customer.getAccountId().equals(account.getId())) {
            //403
            throw new ForbideenException("没有查看客户"+ id + "的权限");
        }
        return customer;
    }

    /**
     * 编辑客户信息
     * @param id
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/{id:\\d+}/edit")
    public String editProduct(@PathVariable Integer id,
                              HttpSession session,
                              Model model) {
        Customer customer = validateCustomer(id,session);
        model.addAttribute("customer",customerService.findById(customer.getId()));
        model.addAttribute("trades",customerService.findAllCustomerTrade());
        model.addAttribute("sources",customerService.findAllCustomerSource());
        return "customer/edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String editProduct(Customer customer,RedirectAttributes redirectAttributes) {
        customerService.editCustomer(customer);
        redirectAttributes.addFlashAttribute("message","修改成功");
        return "redirect:/customer/my/"+customer.getId();
    }

    /**
     * 删除客户
     * @param id
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/{id:\\d+}/delete")
    public String delCustomer(@PathVariable Integer id,
                              HttpSession session,
                              Model model) {
        Customer customer = validateCustomer(id,session);
        customerService.delCustomerById(customer.getId());
        return "redirect:/customer/my";
    }

    /**
     * 放入公海
     * @param id
     * @param session
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/my/{id:\\d+}/public")
    public String publicCustomer(@PathVariable Integer id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Customer customer = validateCustomer(id,session);
        customerService.publicCustomer(customer);
        redirectAttributes.addFlashAttribute("message","已经将该客户放入公海");
        return "redirect:/customer/my";
    }

    /**
     * 公海客户列表
     * @return
     */
    @GetMapping("/public")
    public String publicCustomer(Model model) {
        return "customer/public";
    }

    @GetMapping("/my/{customerId:\\d+}/transfer/{toAccountId:\\d+}")
    public String transferCustomer(@PathVariable Integer customerId,
                                   @PathVariable Integer toAccountId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Customer customer = validateCustomer(customerId,session);
        customerService.transferCustomer(customer,toAccountId);
        redirectAttributes.addFlashAttribute("message","客户转交成功");
        return "redirect:/customer/my";
    }

    /**
     * 将数据导出为csv文件
     */
    @GetMapping("/my/export.csv")
    public void exportCsvData(HttpServletResponse response,
                              HttpSession session) throws IOException {
        Account account = getCurrAccount(session);
        //setContentType表示具体请求中的媒体类型信息
        response.setContentType("text/csv;charset=GBK");
        String fileName = new String("我的客户.csv".getBytes("UTF-8"),"ISO8859-1");
        response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");

        OutputStream outputStream = response.getOutputStream();
        customerService.exportCsvFileToOutputStream(outputStream,account);
    }

    /**
     * 将数据导出为xls文件
     */
    @GetMapping("/my/export.xls")
    public void exportXlsData(HttpServletResponse response,
                              HttpSession session) throws IOException {
        Account account = getCurrAccount(session);
        //setContentType表示具体请求中的媒体类型信息
        response.setContentType("application/vnd.ms-excel");
        String fileName = new String("我的客户.xls".getBytes("UTF-8"),"ISO8859-1");

        response.setHeader("Content-Disposition","attachment; filename=\""+ fileName +"\"");

        OutputStream outputStream = response.getOutputStream();

        List<Customer> customerList = customerService.findCustomerByAccountId(account);

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("我的客户");
        Row titleRow = sheet.createRow(0);
        Cell nameCell = titleRow.createCell(0);

        nameCell.setCellValue("姓名");
        titleRow.createCell(1).setCellValue("电话");
        titleRow.createCell(2).setCellValue("职位");
        titleRow.createCell(3).setCellValue("地址");

        for(int i = 0;i < customerList.size();i++) {
            Customer customer = customerList.get(i);
            Row dataRow = sheet.createRow(i+1);
            dataRow.createCell(0).setCellValue(customer.getCustName());
            dataRow.createCell(1).setCellValue(customer.getMobile());
            dataRow.createCell(2).setCellValue(customer.getJobTitle());
            dataRow.createCell(3).setCellValue(customer.getAddress());
        }
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 给客户添加待办事项
     */
    @PostMapping("/my/{customerId:\\d+}/task/new")
    public String newTaskToCustomer(Task task) {
        taskService.saveNewTask(task);
        return "redirect:/customer/my/"+task.getCustId();
    }



/*    *//**
     * 编辑客户信息
     * @return
     *//*
    @PostMapping("/{id:\\d+}/edit")
    @ResponseBody
    public AjaxResult editCustomer(Customer customer) {
        try {
            customerService.editCustomer(customer);
            return AjaxResult.success();
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return AjaxResult.error(ex.getMessage());
        }
    }*/

}
