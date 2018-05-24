package com.cloud.etherscan.service.dto;

import com.cloud.etherscan.model.EthHolderDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by gongmei on 2018/5/23.
 */
public class ExportExcelDTO {

    private List<EthHolderDetail> list;
    private String ethName;
    private String totalCount;
    private BigDecimal top10;
    private BigDecimal top20;
    private BigDecimal top50;
    private BigDecimal top100;
    private BigDecimal top200;
    private BigDecimal top500;

    private BigDecimal top10Rate;
    private BigDecimal top20Rate;
    private BigDecimal top50Rate;
    private BigDecimal top100Rate;
    private BigDecimal top200Rate;
    private BigDecimal top500Rate;

    private boolean perDay;

    public ExportExcelDTO() {
    }

    public ExportExcelDTO(List<EthHolderDetail> list, String ethName, BigDecimal top10, BigDecimal top20, BigDecimal top50, BigDecimal top100, BigDecimal top200, BigDecimal top500) {
        this.list = list;
        this.ethName = ethName.trim();
        this.top10 = top10;
        this.top20 = top20;
        this.top50 = top50;
        this.top100 = top100;
        this.top200 = top200;
        this.top500 = top500;
    }

    public List<EthHolderDetail> getList() {
        return list;
    }

    public void setList(List<EthHolderDetail> list) {
        this.list = list;
    }

    public String getEthName() {
        return ethName;
    }

    public void setEthName(String ethName) {
        this.ethName = ethName.trim();
    }

    public BigDecimal getTop10() {
        return top10;
    }

    public void setTop10(BigDecimal top10) {
        this.top10 = top10;
    }

    public BigDecimal getTop20() {
        return top20;
    }

    public void setTop20(BigDecimal top20) {
        this.top20 = top20;
    }

    public BigDecimal getTop50() {
        return top50;
    }

    public void setTop50(BigDecimal top50) {
        this.top50 = top50;
    }

    public BigDecimal getTop100() {
        return top100;
    }

    public void setTop100(BigDecimal top100) {
        this.top100 = top100;
    }

    public BigDecimal getTop200() {
        return top200;
    }

    public boolean isPerDay() {
        return perDay;
    }

    public void setPerDay(boolean perDay) {
        this.perDay = perDay;
    }

    public void setTop200(BigDecimal top200) {
        this.top200 = top200;
    }

    public BigDecimal getTop500() {
        return top500;
    }

    public void setTop500(BigDecimal top500) {
        this.top500 = top500;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTop10Rate() {
        return top10Rate;
    }

    public void setTop10Rate(BigDecimal top10Rate) {
        this.top10Rate = top10Rate;
    }

    public BigDecimal getTop20Rate() {
        return top20Rate;
    }

    public void setTop20Rate(BigDecimal top20Rate) {
        this.top20Rate = top20Rate;
    }

    public BigDecimal getTop50Rate() {
        return top50Rate;
    }

    public void setTop50Rate(BigDecimal top50Rate) {
        this.top50Rate = top50Rate;
    }

    public BigDecimal getTop100Rate() {
        return top100Rate;
    }

    public void setTop100Rate(BigDecimal top100Rate) {
        this.top100Rate = top100Rate;
    }

    public BigDecimal getTop200Rate() {
        return top200Rate;
    }

    public void setTop200Rate(BigDecimal top200Rate) {
        this.top200Rate = top200Rate;
    }

    public BigDecimal getTop500Rate() {
        return top500Rate;
    }

    public void setTop500Rate(BigDecimal top500Rate) {
        this.top500Rate = top500Rate;
    }
}
