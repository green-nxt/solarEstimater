package com.greennext.solarestimater.service;

import com.greennext.solarestimater.util.CryptoUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ShineMonitorOpenApi {
    /**
     * shinemonitor Open platform API access address.
     */
    private static String OPEN_API_URI =
            "http://api.shinemonitor.com/public/";

    public static final void main(String[] args) {
        ShineMonitorOpenApi.reg();
        ShineMonitorOpenApi.auth();
        ShineMonitorOpenApi.authPassed();
        ShineMonitorOpenApi.inquirePlantInfo();
        inquirePlantGenerationByDay();
        passwordEncoder();
    }

    /**
     * Authentication interface.
     */
    private static void auth() {
        String usr = "RBS Eng"; /* account, password, manufacturer identification. */
        String pwd = "RBS@123";
        String companyKey = "MzmgRRw3VMZSLnQQ";
        String salt = System.currentTimeMillis() + ""; /* salt. */
        String sha1Pwd =
                CryptoUtil.sha1ToLowerCase(pwd.getBytes()); /* SHA-1(pwd). */
        usr = usr.replaceAll("\\s", "%20");
        String action = "&action=auth&usr=" + usr /* Attention: Chinese need URLEncoder.encode. */
                + "&company-key=" + companyKey;
        String sign = CryptoUtil.sha1ToLowerCase((salt + sha1Pwd
                + action).getBytes()); /* SHA-1(slat + SHA-1(pwd) + action). */
        String request = OPEN_API_URI + "?sign=" +
                sign + "&salt=" + salt + action;
        System.out.println(" Auth: ==> "+request);
    }

    /**
     * register an account interface.
     */
    private static final void reg() {
        String usr = "RanaLavendrasinhji"; /* account, password, manufacturer identification. */
        String pwd = "12345678";
        String pn = "0123456789ABCD";
        String companyKey = "MzmgRRw3VMZSLnQQ";
        String salt = System.currentTimeMillis() + ""; /* salt. */
        String pwdSha1
                = CryptoUtil.sha1ToLowerCase(pwd.getBytes()); /* SHA-1(PWD).*/

        byte[] rc4 = CryptoUtil.rc4enc(companyKey.getBytes(),
                pwdSha1.getBytes(), 0, pwdSha1.getBytes().length); /* RC4(SHA-1(PN),SHA-1(PWD)). */
        String action = "&action=reg&usr=" + usr
                + "&pwd=" + CryptoUtil.sha1ToLowerCase(rc4);
        action += "&company-key=" + companyKey;
        String sign = CryptoUtil.sha1ToLowerCase((salt + pwdSha1
                + action).getBytes());
        String request = OPEN_API_URI + "?sign=" +
                sign + "&salt=" + salt + action;
        System.out.println(request);
    }

    /**
     * Business API Interface After certification is passed.
     */
    private static void authPassed() {
        String secret = "234c8e69d6603807fcacf83022d1c98453fcf136"; /*certification is passed secret and token. */
        String token =
                "8afcfb5d45ac207c73189f8bd005546f8afcb6f5e072ef4532ccc1ba2f504292";
        String salt = System.currentTimeMillis() + ""; /* salt. */
        String action = "&action=queryAccountInfo";
        String sign = CryptoUtil.sha1ToLowerCase((salt + secret
                + token + action).getBytes()); /* SHA-1(slat + secret + token + action).
         */
        String request = OPEN_API_URI + "?sign=" +
                sign + "&salt=" + salt + "&token=" + token + action;
        System.out.println("fetch ==> "+request);
    }

    private static void inquirePlantInfo() {
        String secret = "234c8e69d6603807fcacf83022d1c98453fcf136";
        String token= "8afcfb5d45ac207c73189f8bd005546f8afcb6f5e072ef4532ccc1ba2f504292";
        String pn = "Q0029242327775";
        String sn = "KSY0222HK1828";
        int devcode = 632;
        int devaddr = 1;
        String date = "2025-09-08";
        String salt = System.currentTimeMillis() + "";
        String action = "&action=queryDeviceDataOneDay&i18n=zh_EN&pn="+pn+"&devcode="
                +devcode+"&devaddr="+devaddr+"&sn="+sn+"&date="+date;

        String sign = CryptoUtil.sha1ToLowerCase((salt + secret
                + token + action).getBytes());
        String request = OPEN_API_URI + "?sign=" +
                sign + "&salt=" + salt + "&token=" + token + action;
        System.out.println("inquire PlantInfo ==> "+request);
    }

    private static void inquirePlantGenerationByDay() {
        String secret = "234c8e69d6603807fcacf83022d1c98453fcf136";
        String token= "8afcfb5d45ac207c73189f8bd005546f8afcb6f5e072ef4532ccc1ba2f504292";
        String plantId = "1228998";
        String date = "2025";
        String salt = System.currentTimeMillis() + "";
        String action = "&action=queryPlantEnergyDay&plantid="+plantId;

        String sign = CryptoUtil.sha1ToLowerCase((salt + secret
                + token + action).getBytes());
        String request = OPEN_API_URI + "?sign=" +
                sign + "&salt=" + salt + "&token=" + token + action;
        System.out.println("generation PlantInfo ==> "+request);
    }

    private static void passwordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "green_nxt@123";
        System.out.println("Encoded password: " + passwordEncoder.encode(rawPassword));
    }
}
