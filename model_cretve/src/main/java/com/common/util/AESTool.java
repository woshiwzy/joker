package com.common.util;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class AESTool {
	
	
//    public static void main(String[] args) throws Exception {
//    	
//        String cKey = "1234567890abcDEF";
//        String cSrc = "wzyfly@sina.com";
//        String enString = AESTool.Encrypt(cSrc, cKey);
//        String DeString = AESTool.Decrypt(enString, cKey);
//        
//        
//    }


    private static char[] pwd = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f'};//it must be 16 bit

    public static String decrypt(String input) throws Exception {
        String key = new String(pwd);
        return Decrypt(input, key);
    }

    public static String encrpt(String input) {
        String key = new String(pwd);
        return Encrypt(input, key);

    }

    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            if (sKey == null) {
                return null;
            }
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
    
    
    public static String Encrypt(String sSrc, String sKey) {
    	try{
    	if (sKey == null) {
        	LogUtil.e(Constant.TAG, "skey is null");
            return null;
        }
        if (sKey.length() != 16) {
        	LogUtil.e(Constant.TAG, "skey is not 16 length");
            return null;
        }
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return byte2hex(encrypted).toLowerCase();
    	}catch (Exception e) {
    		e.printStackTrace();
    		LogUtil.e(Constant.TAG, "AES加密错误");
		}
        return null;
    }
    private static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
}
