package com.synch4j.secret;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.synch4j.secret.logic.DESUtil;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.util.SynchConstants;

/**
 * @author XieGuanNan
 * @date 2015-8-20-下午4:22:29
 * 加解密管理器，统一管理加密器，具体用那种算法等
 */
public class SecretManager {
	
	private static Logger logger = Logger.getLogger(SecretManager.class);
	/**
	 * 通过该方法，可以针对不同的导出模式，做出不同的加密；暂时先不写了，真正有这个需求的时候再改这里
	 * @param mode
	 * @param srcData
	 * @param srcKey
	 * @return
	 */
	public static String encode(ExportMode mode,byte[] srcData, byte[] srcKey){
		byte[] encryptedBytes;
		try {
			encryptedBytes = DESUtil.encrypt(srcData, srcKey); // DES加密
			BASE64Encoder encoder = new BASE64Encoder();
			String encode = encoder.encode(encryptedBytes); // BASE64加密
			return encode;
		} catch (Exception e) {
			logger.error("加密时出错",e);
			e.printStackTrace();
		}
		return null;
//		switch(mode){不同模式不同加密算法，导入的时候会稍有麻烦，导出模式已经记录在清单上了，导入时可以根据这个名称判断解密
//		case STANDARD_INCREMENT:
//			
//		}
	}
	
	/**
	 * 按照GBK字符集进行取值，最后解密
	 * @param dataBytes
	 * @return
	 */
	public static byte[] decode(byte[] dataBytes){
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] msgDataByte;
		try {
			msgDataByte = decoder.decodeBuffer(new String(dataBytes,
					"GBK"));// BASE64解密
			byte[] data = DESUtil
						.decrypt(msgDataByte, SynchConstants.SECURE_KEY.getBytes());// DES解密
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
}
