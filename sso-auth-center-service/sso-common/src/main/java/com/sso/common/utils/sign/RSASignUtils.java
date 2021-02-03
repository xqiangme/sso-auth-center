package com.sso.common.utils.sign;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 签名工具类
 */
public class RSASignUtils {

	/**
	 * 加密算法RSA
	 */
	private static final String KEY_ALGORITHM = "RSA";
	/**
	 * 签名算法
	 * SHA1WithRSA | MD5withRSA
	 */
	private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

	/**
	 * 用私钥对信息生成数字签名
	 *
	 * @param data       数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static String sign(Object data, String privateKey) throws Exception {
		return sign(data.toString().getBytes(), privateKey);
	}

	/**
	 * 用私钥对信息生成数字签名
	 *
	 * @param data       需要加签数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		//私钥 key BASE64解密
		byte[] keyBytes = decryptBASE64(privateKey);
		// 转换私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 取私钥匙对象
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		// 实例化Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		// 初始化Signature
		signature.initSign(privateK);
		// 更新
		signature.update(data);
		// 生成签名
		return encryptBASE64(signature.sign());
//	    return new String(Base64.encodeBase64(signature.sign()));

	}

	/**
	 * 校验数字签名
	 *
	 * @param data      已加签的数据
	 * @param publicKey 公钥(BASE64编码)
	 * @param sign      数字签名
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(Object data, String publicKey, String sign)
			throws Exception {
		return verify(data.toString().getBytes(), publicKey, sign);
	}

	/**
	 * 校验数字签名
	 *
	 * @param data      已加签的数据
	 * @param publicKey 公钥(BASE64编码)
	 * @param sign      数字签名
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String publicKey, String sign)
			throws Exception {
		//私钥 key BASE64解密
		byte[] keyBytes = decryptBASE64(publicKey);
		// 转换公钥材料
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成公钥
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		// 实例化Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		// 初始化Signature
		signature.initVerify(publicK);
		// 更新
		signature.update(data);
		// 验证签名
		return signature.verify(decryptBASE64(sign));
	}


	/**
	 * BASE64解密
	 *
	 * @param key
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 *
	 * @param key
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

}
