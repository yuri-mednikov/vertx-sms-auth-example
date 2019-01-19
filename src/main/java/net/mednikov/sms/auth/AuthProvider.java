package net.mednikov.sms.auth;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.jwt.JWTOptions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AuthProvider implements JWTAuth {

    private ECKey key;
    private ECKey secret;

    public AuthProvider(ECKey key, ECKey secret){
        this.key = key;
        this.secret = secret;
    }

    @Override
    public String generateToken(JsonObject data, JWTOptions jwtOptions) {
        try{
            JWSSigner signer = new ECDSASigner(secret);
            //get payload
            String subject = data.getString("sub");
            String role = data.getString("role");
            //set expiration time -> token is valid for 30 days
            LocalDateTime expired = LocalDateTime.now().plusDays(30);
            /*convert "expired" to Date, because JWTClaimsSet accepts Date as an argument*/
            Date date = Date.from(expired.atZone(ZoneId.systemDefault()).toInstant());
            //create JWT claims
            JWTClaimsSet claims = new JWTClaimsSet.Builder().subject(subject).expirationTime(date).claim("role", role).build();

            //create token and sign
            SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .keyID(secret.getKeyID()).build(),
                    claims);
            signedJWT.sign(signer);

            //return create token
            return signedJWT.serialize();
        } catch (Exception ex){
            //Log error to STDOUT
            System.out.println("Unable to generate token: "+ex.getLocalizedMessage());
            //return null result
            return null;
        }
    }

    @Override
    public void authenticate(JsonObject data, Handler<AsyncResult<User>> handler) {

        //get JWT token
        String token = data.getString("jwt");
        //check if token is valid or not
        try{
            //verify token
            JWSVerifier verifier = new ECDSAVerifier(key);
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!signedJWT.verify(verifier)){
                //we cannot verify this token -> throw Exception
                throw new Exception();
            }
            //get user name from token
            String name = signedJWT.getJWTClaimsSet().getSubject();
            //get user role from token
            String role = signedJWT.getJWTClaimsSet().getStringClaim("role");
            //create AuthUser object with name and specified permission
            AuthUser user = new AuthUser(name, role);
            //User was authentificated successfully
            handler.handle(Future.succeededFuture(user));
        } catch (Exception ex){
            //Log error to STDOUT
            System.out.println(ex.getLocalizedMessage());
            //user is not authorized
            handler.handle(Future.failedFuture(ex));
        }

    }
}
