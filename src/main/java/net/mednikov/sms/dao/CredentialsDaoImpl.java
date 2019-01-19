package net.mednikov.sms.dao;

import net.mednikov.sms.entity.Credentials;

public class CredentialsDaoImpl implements ICredentialsDao{

    private Credentials data;

    public CredentialsDaoImpl(){
        this.data = new Credentials();
        this.data.setRole("user");
        this.data.setUsername("johndoe");

        //set your phone here
        this.data.setPhone("YOUR PHONE");
    }


    @Override
    public Credentials findCredentialsByUsername(String username) {
        if (username.equalsIgnoreCase(this.data.getUsername())){
            return this.data;
        } else {
            return null;
        }
    }
}
