package com.xplore24.courier.Model;

public class OrderStatus {
    private String placed,cancel,inshipment,complete;

    public OrderStatus(String placed, String cancel, String inshipment, String complete) {
        this.placed = placed;
        this.cancel = cancel;
        this.inshipment = inshipment;
        this.complete = complete;
    }

    public OrderStatus() {
    }

    public String getPlaced() {
        return placed;
    }

    public void setPlaced(String placed) {
        this.placed = placed;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getInshipment() {
        return inshipment;
    }

    public void setInshipment(String inshipment) {
        this.inshipment = inshipment;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }
}
