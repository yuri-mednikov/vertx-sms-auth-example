package net.mednikov.sms.service;

public interface ISmsService {

    void sendMessage(String phone, String code);
}
