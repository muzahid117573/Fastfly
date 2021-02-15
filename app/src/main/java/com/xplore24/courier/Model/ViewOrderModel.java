package com.xplore24.courier.Model;

public class ViewOrderModel {
    private String paymentType,status,_id,phone,address,money,deliveryCharge;

    public ViewOrderModel( String status, String _id, String phone, String address, String money, String deliveryCharge) {
        this.status = status;
        this._id = _id;
        this.phone = phone;
        this.address = address;
        this.money = money;
        this.deliveryCharge = deliveryCharge;
    }

    public ViewOrderModel() {
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }
}
