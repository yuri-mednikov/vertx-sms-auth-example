package net.mednikov.sms.util;

public class CodeGenerator {

    public static String generate(){

        StringBuilder sb = new StringBuilder();

        for (int i=0; i<4; i++){
            double random = Math.random()*10;
            int number = (int) random;
            sb.append(number);
        }

        String result = new String(sb);

        /*debug message, if you don't want to use SMS api but want to try a code*/
        System.out.println("Generated code: "+result);
        return result;
    }
}
