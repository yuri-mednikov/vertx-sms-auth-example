package net.mednikov.sms.dao;

import net.mednikov.sms.entity.Credentials;

public interface ICredentialsDao {

    Credentials findCredentialsByUsername (String username);
}
