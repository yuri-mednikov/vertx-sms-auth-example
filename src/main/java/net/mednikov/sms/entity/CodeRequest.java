package net.mednikov.sms.entity;

import java.time.LocalDateTime;

public class CodeRequest {

    private String id;
    private String username;
    private LocalDateTime issuedDateTime;
    private String code;

    public CodeRequest(){

    }

    //Helper method to extract datetime calculations from main code
    public boolean isExpired(){
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getIssuedDateTime() {
        return issuedDateTime;
    }

    public void setIssuedDateTime(LocalDateTime issuedDateTime) {
        this.issuedDateTime = issuedDateTime;
    }
}
