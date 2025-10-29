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
        queryDataCollector();
        queryDeviceEnergyMonthPerDay();
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
        String secret = "86846c6694a2f33cf8501cfc855fcc9a3b47dfba";
        String token= "fd4a3d3438802dfadf6d2f94f83a738a9e262cc87e84a56ea31c91f8cf04d0eb";
        String pn = "I30000230543823968";
        String sn = "KSY0625HT18768";
        int devcode = 632;
        int devaddr = 1;
        String date = "2025-10-08";
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
        String secret = "86846c6694a2f33cf8501cfc855fcc9a3b47dfba";
        String token= "fd4a3d3438802dfadf6d2f94f83a738a9e262cc87e84a56ea31c91f8cf04d0eb";
        String plantId = "1443652";
        String date = "2025-10-10";
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

//     Query data collector
    private static void  queryDataCollector(){
        String secret = "86846c6694a2f33cf8501cfc855fcc9a3b47dfba";
        String token= "fd4a3d3438802dfadf6d2f94f83a738a9e262cc87e84a56ea31c91f8cf04d0eb";
        String plantId = "1443652";
        String date = "2025-10-10";
        String salt = System.currentTimeMillis() + "";
        String action = "&action=queryCollectors";
        String sign = CryptoUtil.sha1ToLowerCase((salt + secret
                + token + action).getBytes());
        String request = OPEN_API_URI + "?sign=" +
                sign + "&salt=" + salt + "&token=" + token + action;
        System.out.println("generation PlantInfo ==> "+request);
    }

    /**
     * Query the power output of inverter or other power
     * generation equipment in a certain day
     *
     * Parameter Description Required
     * action queryDeviceEnergyDay yes
     * pn Pn of data collector yes
     * devcode Equipment protocol code yes
     * devaddr Device address (485 bus) yes
     * sn Equipment serial number yes
     * **/
    private static void queryDeviceGenerationByDay() {
        String secret = "86846c6694a2f33cf8501cfc855fcc9a3b47dfba";
        String token= "fd4a3d3438802dfadf6d2f94f83a738a9e262cc87e84a56ea31c91f8cf04d0eb";
        String pn = "I30000230543823968";
        String sn = "KSY0625HT18768";
        int devcode = 632;
        int devaddr = 1;
        String date = "2025-10-08";
        String salt = System.currentTimeMillis() + "";
        String action = "&action=queryDeviceEnergyDay&i18n=zh_EN&pn="+pn+"&devcode="
                +devcode+"&devaddr="+devaddr+"&sn="+sn+"&date="+date;
        String sign = CryptoUtil.sha1ToLowerCase((salt + secret
                + token + action).getBytes());
        String request = OPEN_API_URI + "?sign=" +
                sign + "&salt=" + salt + "&token=" + token + action;
        System.out.println("inquire PlantInfo ==> "+request);
    }

    /**
     * Query the daily power output of an inverter for a specific month.
     * This data is used to generate a graph.
     * Based on API doc section 6.4.
     */
    private static void queryDeviceEnergyMonthPerDay() {
        // Use the same secret and token from your successful login
        String secret = "86846c6694a2f33cf8501cfc855fcc9a3b47dfba";
        String token = "fd4a3d3438802dfadf6d2f94f83a738a9e262cc87e84a56ea31c91f8cf04d0eb";

        // --- Device Identification Parameters ---
        String pn = "I30000230543823968"; // Data collector PN [cite: 1146]
        String sn = "KSY0625HT18768";      // Equipment serial number [cite: 1146]
        int devcode = 632;                 // Equipment protocol code [cite: 1146]
        int devaddr = 1;                   // Device address (485 bus) [cite: 1146]

        // --- Date Parameter (Year and Month) ---
        String date = "2025-10"; // The month to query in "yyyy-mm" format [cite: 1146]

        String salt = System.currentTimeMillis() + "";

        // Construct the action string for the API call
        String action = "&action=queryDeviceEnergyMonthPerDay&pn=" + pn + "&devcode="
                + devcode + "&devaddr=" + devaddr + "&sn=" + sn + "&date=" + date;

        // Calculate the signature
        String sign = CryptoUtil.sha1ToLowerCase((salt + secret
                + token + action).getBytes());

        // Build the final request URL
        String request = OPEN_API_URI + "?sign=" +
                sign + "&salt=" + salt + "&token=" + token + action;

        System.out.println("Graph Data URL ==> " + request);
    }
}
