package com.greennext.solarestimater.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.security.Key;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ShineMonitorOpenApis
{
    /** shinemonitor Open platform API Access Address. */
    private static String OPEN_API_URI = "http://api.shinemonitor.com/public/";

    static class AuthResponseDTO
    {
        public String token;
        public String secret;
    }

    static class GlobalParams {
        public String source;
        public String app_id;
        public String app_version;
        public String app_client;
    }

    public static final void main(String[] args)
    {
        //global params
        GlobalParams globalParams = new GlobalParams();
        globalParams.source = "0";
        globalParams.app_id = "com.demo.test";// your application id
        globalParams.app_version = "3.6.2.1";// your application version
        globalParams.app_client = "android"; //android or ios or web

        //your auth info
        String usr = "vplant"; /* Account, password, and manufacturer identification assigned by the platform. */
        String pwd = "vplant";
        String companyKey = "MzmgRRw3VMZSLnQQ";

        //first: login
        ShineMonitorOpenApis.auth(usr,pwd,companyKey, globalParams);

        //second: you will get json and parse it to AuthResponseDTO
        //{"err":0,"desc":"ERR_NONE","dat":{"secret":"f9dc4a46b4668e9363d4d0d15e0757e8ae3a7e9a","expire":604800,"token":"5e641f1b785c237c8df905ca4a3ce53fa3d05200fc457b032aa9b187deeae3cc","role":5,"usr":"vplant","uid":2}}
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.token = ""; //dat.token;
        authResponseDTO.secret = ""; //dat.secret;

        //third: use your api
        String pn = "I30000231153570848";
        String sn = "KSY0624HK14604";
        int devcode = 632;
        int devaddr = 1;
        String date = "2025-08-21";
        ShineMonitorOpenApis.queryDeviceDataOneDay(pn,devcode,devaddr,sn,date,authResponseDTO, globalParams);
    }

    /** Authentication interface. */
    private static void auth(String usr, String pwd, String companyKey, GlobalParams globalParams)
    {
            String _app_id_ = globalParams.app_id;
            String _app_version_ = globalParams.app_version;
            String _app_client_ = globalParams.app_client;
            String source = globalParams.source;
            //
            String salt = System.currentTimeMillis() + ""; /* salt value. */
            String sha1Pwd = ShineMonitorOpenApis.sha1ToLowerCase(pwd.getBytes()); /* SHA-1(pwd). */
            String action = "&action=auth&usr=" + URLEncoder.encode(usr) /* Note: URLEncoder.encode encoding is required for Chinese */ + "&company-key="
                    + companyKey  + "&source=" + source
                    + "&_app_id_=" + _app_id_ + "&_app_version_=" + _app_version_
                    +"&_app_client_=" + _app_client_;
             /* SHA-1(slat + SHA-1(pwd) + action). */
            String sign = ShineMonitorOpenApis.sha1ToLowerCase((salt + sha1Pwd + action).getBytes());
            String request = ShineMonitorOpenApis.OPEN_API_URI + "?sign=" + sign + "&salt=" + salt + action;
            System.out.println(request);
    }

    private static void queryDeviceDataOneDay(String pn, int devcode, int devaddr, String sn, String date,
                                                    AuthResponseDTO authResponseDTO, GlobalParams globalParams)
    {
            // Secrets and tokens after authentication
            String secret = authResponseDTO.secret; /* Secrets and tokens after authentication. */
            String token = authResponseDTO.token;
            // API Initialization params
            String _app_id_ = globalParams.app_id;
            String _app_version_ = globalParams.app_version;
            String _app_client_ = globalParams.app_client;
            String source = globalParams.source;
            //
            String salt = System.currentTimeMillis() + ""; /* salt value. */

            String action = "&action=queryDeviceDataOneDay&i18n=zh_EN&pn="+pn+"&devcode="
                    +devcode+"&devaddr="+devaddr+"&sn="+sn+"&date="+date+""
                    + "&source=" + source + "&_app_id_="
                    + _app_id_ + "&_app_version_=" + _app_version_ + "&_app_client_=" + _app_client_;
            String sign = ShineMonitorOpenApis.sha1ToLowerCase((salt + secret + token + action).getBytes()); /* SHA-1(slat +                   secret + token + action). */
            String request = ShineMonitorOpenApis.OPEN_API_URI + "?sign=" + sign + "&salt=" + salt + "&token=" + token + action;
            System.out.println(request);
        }

    /** Convert byte stream to hexadecimal string (compact format, no spaces). */
    private static final String byte2HexStr(byte by[], int ofst, int len)
    {
        if (len < 1)
            return "";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        for (int i = ofst; i < ofst + len; i++)
            ps.printf("%02X", by[i]);
        return bos.toString();
    }

    /** SHA-1 digest algorithm. */
    private static final String sha1ToLowerCase(byte[] by)
    {
        try
        {
            byte dat[] = MessageDigest.getInstance("SHA-1").digest(by);
            return ShineMonitorOpenApis.byte2HexStr(dat, 0, dat.length).toLowerCase();
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /** RC4 encryption. */
    public static final byte[] rc4enc(byte key[], byte[] org, int ofst, int len)
    {
        try
        {
            Key k = new SecretKeySpec(key, "RC4");
            Cipher cip = Cipher.getInstance("RC4");
            cip.init(Cipher.ENCRYPT_MODE, k);
            return cip.doFinal(org, ofst, len);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}