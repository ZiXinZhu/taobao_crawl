package com.zzx.executor.entity;



public class ReOrderEntity {

    private Integer id;

    private String trade_date;

    private String trade_time;

    private String money;

    private String trade_type;

    private String remark;

    private String identity;

    private String bank;

    private Boolean report;

    private String bank_account;

    @Override
    public String toString() {
        return "ReOrderEntity{" +
                "id=" + id +
                ", trade_date='" + trade_date + '\'' +
                ", trade_time='" + trade_time + '\'' +
                ", money='" + money + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", remark='" + remark + '\'' +
                ", identity='" + identity + '\'' +
                ", bank='" + bank + '\'' +
                ", report=" + report +
                ", bank_account='" + bank_account + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(String trade_date) {
        this.trade_date = trade_date;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Boolean getReport() {
        return report;
    }

    public void setReport(Boolean report) {
        this.report = report;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }
}
