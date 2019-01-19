package net.mednikov.sms.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;

public class AuthUser extends AbstractUser {

    private String name;
    private String role;

    public AuthUser(String name, String role){
        this.name = name;
        this.role = role;
    }

    @Override
    protected void doIsPermitted(String permission, Handler<AsyncResult<Boolean>> handler) {
        //if a requested role equals to user's role, we give a permission
        handler.handle(Future.succeededFuture(permission.equalsIgnoreCase(role)));
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {

    }

    @Override
    public JsonObject principal() {
        return null;
    }

}
