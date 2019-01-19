package net.mednikov.sms;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.JWTAuthHandler;
import net.mednikov.sms.auth.AuthProvider;
import net.mednikov.sms.dao.CodeRequestDaoImpl;
import net.mednikov.sms.dao.CredentialsDaoImpl;
import net.mednikov.sms.dao.ICodeRequestDao;
import net.mednikov.sms.dao.ICredentialsDao;
import net.mednikov.sms.entity.CodeRequest;
import net.mednikov.sms.entity.Credentials;
import net.mednikov.sms.service.ISmsService;
import net.mednikov.sms.service.SmsService;
import net.mednikov.sms.util.CodeGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

public class SmsServiceVerticle extends AbstractVerticle{

    private ICodeRequestDao codeRequestDao;
    private ICredentialsDao credentialsDao;

    private ISmsService smsService;

    private ECKey key;
    private ECKey secret;

    public SmsServiceVerticle() throws Exception{
        init();
    }

    @Override
    public void start() throws Exception {
        super.start();

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        this.smsService = new SmsService(vertx);

        AuthProvider authProvider = new AuthProvider(key, secret);

        /*
        Steps are described in the article
        * Access it here: http://www.mednikov.net/case-studies/how-i-reinvented-a-wheel-building-sms-auth-in-vertx-with-java/
        */

        router.get("/login/:username").handler(c->{
            String username = c.pathParam("username");
            Credentials creds = credentialsDao.findCredentialsByUsername(username); //step 2
            if (creds==null){
                //step 3.
                c.response().setStatusCode(404).end("Not found");
            }
            //step 4
            String phone = creds.getPhone();
            //CodeGenerator is a wrapper to extract dependency
            String code = CodeGenerator.generate();

            //step 5
            //send SMS with code to user
            smsService.sendMessage(phone, code);

            //store
            codeRequestDao.store(username, code, LocalDateTime.now());
            c.response().setStatusCode(201).end();
        });

        router.get("/code/:username/:code").handler(c->{
            String code = c.pathParam("code");
            String username = c.pathParam("username");
            //step 7
            CodeRequest codeRequest = codeRequestDao.find(username, code);
            if (codeRequest == null){
                c.response().setStatusCode(403).end("Unauthorized");
            }
            if (codeRequest.isExpired()){
                //step 8
                c.response().setStatusCode(403).end("Unauthorized");
            }
            //Step 9
            codeRequestDao.delete(codeRequest.getId());
            Credentials creds = credentialsDao.findCredentialsByUsername(username);
            //setting JWT claims
            JsonObject claims = new JsonObject();
            claims.put("role", creds.getRole());
            claims.put("sub", creds.getUsername());
            //...other claims can be specified
            //Step 10
            String token = authProvider.generateToken(claims, new JWTOptions());
            c.response().setStatusCode(201).putHeader("Authorization", token).end("Access granted");
        });

        router.route("/private/*").handler(JWTAuthHandler.create(authProvider));

        /*An example of protected route*/
        router.get("/private/hello").handler(c->c.response().setStatusCode(200).end("This is a protected route. If you read this, you are cool"));

        server.requestHandler(router).listen(4567);

    }

    private void init() throws Exception{
        
        //init dependencies here
        this.credentialsDao = new CredentialsDaoImpl();
        this.codeRequestDao = new CodeRequestDaoImpl();

        /*
        * Generate keys
        * Key generation depends on your implementation
        * Here I use ECKeys and JOSE JWT lib
        * Check their docs for more details https://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-ec-signature
        */
        secret = new ECKeyGenerator(Curve.P_256).keyID(UUID.randomUUID().toString()).generate();
        key = secret.toPublicJWK();
    }

    public static void main(String[] args) throws Exception{
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new SmsServiceVerticle());
    }
}
