package net.mednikov.sms.dao;

import net.mednikov.sms.entity.CodeRequest;

import java.time.LocalDateTime;

public interface ICodeRequestDao {

    void store(String username, String code, LocalDateTime dateTime);

    void delete(String requestId);

    CodeRequest find(String username, String code);
}
