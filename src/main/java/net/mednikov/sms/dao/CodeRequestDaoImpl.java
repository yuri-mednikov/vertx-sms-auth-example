package net.mednikov.sms.dao;

import net.mednikov.sms.entity.CodeRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public class CodeRequestDaoImpl implements ICodeRequestDao {

    private CodeRequest data;

    public CodeRequestDaoImpl(){
        this.data = new CodeRequest();
    }


    @Override
    public void store(String username, String code, LocalDateTime dateTime) {
        this.data.setUsername(username);
        this.data.setCode(code);
        this.data.setIssuedDateTime(dateTime);

        String id = UUID.randomUUID().toString();
        this.data.setId(id);

    }

    @Override
    public void delete(String requestId) {
        System.out.println("Data deleted");
    }

    @Override
    public CodeRequest find(String username, String code) {
        return this.data;
    }
}
