package com.kaishengit.crm.entity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Task implements Serializable {
    private Integer id;

    private String title;

    private String finishTime;

    private String remindTime;

    /**
     * 0 未完成， 1 完成
     */
    private Byte done;

    private Integer accountId;

    private Integer custId;

    private Integer saleId;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    private Customer customer;

    private SaleChance saleChance;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SaleChance getSaleChance() {
        return saleChance;
    }

    public void setSaleChance(SaleChance saleChance) {
        this.saleChance = saleChance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public Byte getDone() {
        return done;
    }

    public void setDone(Byte done) {
        this.done = done;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", finishTime='" + finishTime + '\'' +
                ", remindTime='" + remindTime + '\'' +
                ", done=" + done +
                ", accountId=" + accountId +
                ", custId=" + custId +
                ", saleId=" + saleId +
                ", createTime=" + createTime +
                ", customer=" + customer +
                ", saleChance=" + saleChance +
                '}';
    }

    /**
     * 判断是否逾期
     * @return true 逾期 false 正常
     */
    public boolean isOverTime() {
        //String -> DateTime
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime finishTime = formatter.parseDateTime(getFinishTime());
        return finishTime.isBeforeNow();
    }
}