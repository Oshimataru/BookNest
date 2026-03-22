package com.booknest.booknest_backend.dto;

public class PaymentRequest {
    private Double amount;
    private Long bookId;
    private String address;

    public Double getAmount() { return amount; }
    public Long getBookId() { return bookId; }
    public String getAddress() { return address; }

    public void setAmount(Double amount) { this.amount = amount; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public void setAddress(String address) { this.address = address; }
}