package com.cloud.etherscan.model;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-05-22 10:04
 */
public class EthHolderDetail {

    private String token;

    private String address;

    private String quantity;

    private String percentage;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "EthHolderDetail{" +
                "token='" + token + '\'' +
                ", address='" + address + '\'' +
                ", quantity='" + quantity + '\'' +
                ", percentage='" + percentage + '\'' +
                '}';
    }
}
