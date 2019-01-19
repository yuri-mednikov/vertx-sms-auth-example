package net.mednikov.sms.service;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;

public class SmsService implements ISmsService {

    /*
    * You need to get an API key for textbelt in order to try.
    * Get it here https://textbelt.com/
    * */
    private final String API_KEY = "YOUR API KEY";
    private HttpClient client;

    public SmsService(Vertx vertx){
        this.client = vertx.createHttpClient();
        //this.apiKey = System.getenv("SMS_API_KEY");
    }

    @Override
    public void sendMessage(String phone, String code) {
        JsonObject body = new JsonObject();
        body.put("key", API_KEY);
        body.put("message", "Your authorization code: "+code);
        body.put("phone",phone);

        HttpClientRequest request = client.post("https://textbelt.com/text", r->{
            System.out.println("Request to Textbelt API sent. Code: "+r.statusCode());
        });

        request.putHeader("content-length", "1000");
        request.putHeader("content-type", "application/json");
        request.write(body.toString());
        request.end();
    }
}
