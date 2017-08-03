package hansen.utils;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class Key {

	public static String RSA_ALGORITHM ="RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";  
	public static String STORE_PASSWORD ="123456789";
	public static String CERT_PASSWORD ="abcdefghijkl";  
	public static String DES_ALGORITHM ="DES";
	public static String ENCRIPT_STRING ="ABCDEFG"+ ToolUtil.getCurrentDateTime();
	public static String DEFAULT_ENCODING ="UTF-8";
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//		SecretKey secretKey = Key.getSecretKey(DES_ALGORITHM);
//		Cipher cipher = Key.getCipher(DES_ALGORITHM);
//		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//		byte[] encriptBtye = cipher.doFinal(ENCRIPT_STRING.getBytes());
//
//
//		SecretKeySpec secretKeySpec =Key.getSecretKeySpec(secretKey.getEncoded(), DES_ALGORITHM);
//		cipher.init(Cipher.DECRYPT_MODE, secretKey);
//		byte[] decriptBtye = cipher.doFinal(encriptBtye);
//		System.out.println(new String(decriptBtye));


		try {
			long time=System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				System.out.println(ToolUtil.convertByteToString(getEncode()));
			}
			System.out.println("=============  "+(System.currentTimeMillis()-time));
			getDecode(ToolUtil.convertByteToString(getEncode()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		 try {
			/*SecretKey secretKey = Key.getSecretKey(DES_ALGORITHM);
			byte[] e=Key.getEncodeResult(secretKey);
			System.out.println(ToolUtil.convertByteToString(e));
			byte[] r =Key.getDecodeResult(secretKey,ToolUtil.convertStringToByte(ToolUtil.convertByteToString(e)));
			System.out.println(new String(r));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}





	public static byte[] getEncodeResult(SecretKey secretKey) throws  Exception {
		Cipher cipher = Key.getCipher(DES_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encriptBtye = cipher.doFinal(ENCRIPT_STRING.getBytes());
		return encriptBtye;
	}

	public static byte[] getDecodeResult(SecretKey secretKey,byte[] encodeByte) throws  Exception {
		SecretKeySpec secretKeySpec = Key.getSecretKeySpec(secretKey.getEncoded(),DES_ALGORITHM);
		Cipher cipher = Key.getCipher(DES_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] encriptBtye = cipher.doFinal(encodeByte);
		return encriptBtye;
	}

	/*private static void encode() throws  Exception {
		KeyGenerator generator = KeyGenerator.getInstance("DES");
		SecretKey key = generator.generateKey();
		saveFile("key.data", key.getEncoded());
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		String text = "Hello World";
		byte[] encrypted = cipher.doFinal(text.getBytes());
		saveFile("encrypted.bin", encrypted);
	}*/

	public static SecretKey getSecretKey(String algorithm) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(DES_ALGORITHM);
			SecretKey key = generator.generateKey();
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SecretKeySpec getSecretKeySpec(byte[] key,String algorithm) {
		SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
		return keySpec;
	}

	public static Cipher getCipher(String algorithm) {
		try {
			Cipher	cipher = Cipher.getInstance(algorithm);
			return  cipher;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}

/*
	private static void decode() throws  Exception {
//		byte[] keyData = getData("key.data");
		SecretKeySpec keySpec = new SecretKeySpec("keyData".getBytes(), "DES");
		SecretKey secretKey = new SecretKeySpec("AA".getBytes(), "RSA");
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
//		byte[] data = getData("encrypted.bin");
		byte[] result = cipher.doFinal("data".getBytes());
	}*/


	/*private static void saveFile(String string, byte[] encoded) {

	}
*/



	public  static byte[] getEncode() throws  Exception {


		// 用证书的公钥加密
	    CertificateFactory cff = CertificateFactory.getInstance("X.509");
	    FileInputStream fis1 = new FileInputStream("E:\\test\\Key.cer");
	    BufferedInputStream bi = new BufferedInputStream(fis1);
//	    FileInputStream fis1 = new FileInputStream("C:\\project\\wallet-call\\WEB-INF\\classes\\Key.cer");

	    Certificate cf =   cff.generateCertificate(fis1);
	    PublicKey pk1 = cf.getPublicKey(); // 得到证书文件携带的公钥
	    Cipher c1 = Cipher.getInstance(RSA_ALGORITHM); // 定义算法：RSA
	    c1.init(Cipher.ENCRYPT_MODE, pk1);
	    byte[] encode = c1.doFinal(ENCRIPT_STRING.getBytes(DEFAULT_ENCODING)); // 加密后的数据
	    return  encode;
	}

	public static byte[] getDecode(String encdoe) throws  Exception {
		System.out.println(encdoe);
		 // 用证书的私钥解密 - 该私钥存在生成该证书的密钥库中
	    FileInputStream fis2 = new FileInputStream("E:\\test\\KeyStore");
//	    FileInputStream fis2 = new FileInputStream("C:\\project\\wallet-call\\WEB-INF\\classes\\KeyStore");
	    KeyStore ks = KeyStore.getInstance("JKS"); // 加载证书库
	    char[] kspwd = STORE_PASSWORD.toCharArray(); // 证书库密码
	    char[] keypwd = CERT_PASSWORD.toCharArray(); // 证书密码
	    ks.load(fis2, kspwd); // 加载证书
	    PrivateKey pk2 = (PrivateKey) ks.getKey("Key", keypwd); // 获取证书私钥
	    fis2.close();
	    Cipher c2 = Cipher.getInstance(RSA_ALGORITHM);
	    c2.init(Cipher.DECRYPT_MODE, pk2);
	    byte[] decode = c2.doFinal(ToolUtil.convertStringToByte(encdoe)); // 解密后的数据
	    System.out.println(new String(decode, DEFAULT_ENCODING)); // 将解密数据转为字符串  
	    return  decode;
	}
	
	public static Boolean check(String encdoe){
		String s;
		try {
			s = new String(getDecode(encdoe));
			if (s.equals(ENCRIPT_STRING)) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}
