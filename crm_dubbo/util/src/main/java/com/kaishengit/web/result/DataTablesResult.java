package com.kaishengit.web.result;


import java.util.List;

/**
 * dataTable结果
 * Created by hoyt on 2017/11/8.
 */
public class DataTablesResult<T> {

    private Integer draw;
    private Integer recordsTotal;
    private Integer recordsFiltered;
    private List<T> data;

    public DataTablesResult() {}

    public DataTablesResult(Integer draw,Integer recordsTotal,List<T> data) {
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsTotal;
        this.data = data;
    }

    public DataTablesResult(Integer draw, Integer recordsTotal, Integer recordsFiltered, List<T> data) {
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Integer recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
