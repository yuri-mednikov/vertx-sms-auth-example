# Vertx-sms-auth-example

This is an example application for my post [How I reinvented a wheel: Building SMS auth in Vertx with Java](http://www.mednikov.net/case-studies/how-i-reinvented-a-wheel-building-sms-auth-in-vertx-with-java/?preview=true&_thumbnail_id=62), published in my blog. It illustrates the approach, described in the post. Consider it as a demo, it is not a production ready app :)

## How to run this example

### Step 1. Clone this repo

Clone repository:

```
git clone https://github.com/yuri-mednikov/vertx-sms-auth-example
```

This is a Maven project, that you can import in your IDE.

### Step 2. Get an API key for SMS service

To get a full flavour, I recommend you to get an API key for [Textbelt](https://textbelt.com), because I use their API in example (if you don’t want to get a message, skip this and next steps, you anyway would get an SMS code in STDOUT).

Then go to SMSService.java and set their your API key:

```java
private final String API_KEY = "YOUR API KEY";
```

### Step 3. Enter your phone

In CredentialsDaoImpl.java set your phone number (or a phone number, where you want to receive a SMS code):

```java
this.data.setPhone("YOUR PHONE");
```

### Step 4. Run an app!

Just run a _SMSServiceVerticle.main()_ and app runs on localhost:4567

### Step 5. Login

![](http://www.mednikov.net/wp-content/uploads/2019/01/cs_vertx_smsauth_step1.png)

Access localhost:4567/login/johndoe (you can set any username in step 3) to login. You would receive an OTP SMS code on your phone. Or in any case, you would get in stdout – just check your IDE console.

### Step 6. Verify a code

![](http://www.mednikov.net/wp-content/uploads/2019/01/cs_vertx_smsauth_step2.png)

Next step is a verification. Access localhost:4567/code/:username/:code to verify a code and if everything is ok (and it is an example, so it is ok) you would get a JWT token in Authorization header.

### Step 7. Access protected resources

![](http://www.mednikov.net/wp-content/uploads/2019/01/cs_vertx_smsauth_step3.png)

Now you can use your JWT token to access a protected route.

## License

This is code is delivered for you under terms of MIT license absolutely without any warranties or guaranties. Use it on your risk :)


__Yuri Mednikov__