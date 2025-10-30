package com.example.homecleaning.models;

public class Booking {
    private int id;
    private int userId;
    private int serviceId;
    private String serviceTitle; // joined for convenience
    private String address;
    private String date;
    private String time;
    private String status; // e.g., Pending, Confirmed


    public Booking() {}


    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getServiceTitle() { return serviceTitle; }
    public void setServiceTitle(String serviceTitle) { this.serviceTitle = serviceTitle; }
}
