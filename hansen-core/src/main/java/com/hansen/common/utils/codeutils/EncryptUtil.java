package com.hansen.common.utils.codeutils;

import com.hansen.common.utils.toolutils.ToolUtil;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Date;

public class EncryptUtil {


    public static void main(String[] args) throws Exception {


        byte[] msg = "test!中文".getBytes("UTF8"); // 待加解密的消息

        // 用证书的公钥加密
        CertificateFactory cff = CertificateFactory.getInstance("X.509");
        FileInputStream fis1 = new FileInputStream("E:\\test\\Key.cer");
        Certificate cf = cff.generateCertificate(fis1);
        PublicKey pk1 = cf.getPublicKey(); // 得到证书文件携带的公钥
        Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // 定义算法：RSA
        c1.init(Cipher.ENCRYPT_MODE, pk1);
        byte[] msg1 = c1.doFinal(msg); // 加密后的数据
        System.out.println(EncryptUtil.convertByteToString("aaa".getBytes()));
        System.out.println(msg1);

        // 用证书的私钥解密 - 该私钥存在生成该证书的密钥库中
        FileInputStream fis2 = new FileInputStream("E:\\test\\KeyStore");
        KeyStore ks = KeyStore.getInstance("JKS"); // 加载证书库
        char[] kspwd = "123456789".toCharArray(); // 证书库密码
        char[] keypwd = "abcdefghijkl".toCharArray(); // 证书密码
        ks.load(fis2, kspwd); // 加载证书
        PrivateKey pk2 = (PrivateKey) ks.getKey("Key", keypwd); // 获取证书私钥
        fis2.close();
        Cipher c2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        c2.init(Cipher.DECRYPT_MODE, pk2);
        byte[] msg2 = c2.doFinal(msg1); // 解密后的数据

        System.out.println(new String(msg2, "UTF8")); // 将解密数据转为字符串
    }


    public static Double getBigDecimalDouble4(Double cost) {
        BigDecimal b = new BigDecimal(cost);
        return b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static Boolean isIpPassCheck(String ip) {
        if (ToolUtil.isEmpty(ip)) {
            return false;
        }
        if ("".contains(ip)) {
            return true;
        }
        return false;
    }

    public static String convertByteToString(byte[] bs) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bs) {
            stringBuffer.append(b).append(",");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    public static byte[] convertStringToByte(String s) {
        byte[] bs = null;
        if (ToolUtil.isNotEmpty(s)) {
            String[] array = s.split(",");
            bs = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                bs[i] = Byte.parseByte(array[i]);
            }
        }
        return bs;
    }

    public static Long getCurrentDateTime() {
        System.out.println(new Date().getHours());
        System.out.println(new Date().getMinutes());
        System.out.println(new Date().getSeconds());
        return Long.valueOf(new Date().getHours());
    }
}
