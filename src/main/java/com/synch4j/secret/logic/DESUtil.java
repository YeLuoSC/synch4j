package com.synch4j.secret.logic;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESUtil {
	private final static String DES = "DES"; 
	
	public static String C_Base64Code(byte[] srcBytes) 
	{
		  
		char[] Base64Code = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T', 
				    'U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n', 
				    'o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7', 
				    '8','9','+','/','='}; 
		byte empty = (byte)0; 

		StringBuffer outmessage= new StringBuffer("");
		int messageLen = srcBytes.length; //将字符分成3个字节一组，如果不足，则以0补齐		
		
		int tolLength=messageLen;
		int use= messageLen % 3;
		if(use>0)
		{
			tolLength = tolLength+(3-use);
		}
		byte[] byteMessage =new byte[messageLen+(3-messageLen % 3)];
		System.arraycopy(srcBytes, 0, byteMessage, 0, messageLen);

		int page = messageLen / 3; 
		if (use> 0) 
		{ 				  
			for (int i = 0; i < 3 - use; i++)
				byteMessage[messageLen+i]=empty;
//				  byteMessage.Add(empty); 
				page++; 
		}	//将3个字节的每组字符转换成4个字节一组的。3个一组，一组一组变成4个字节一组 　　//方法是：转换成ASCII码，按顺序排列24 位数据，再把这24位数据分成4组，即每组6位。再在每组的的最高位前补两个0凑足一个字节。 　　
//			outmessage = new System.Text.StringBuilder(page * 4); 　　
		for (int i = 0; i < page; i++) 
		{ 	//取一组3个字节的组 　　

			byte[] instr = new byte[3];
			instr[0] = (byte)byteMessage[i * 3]; 
			instr[1] = (byte)byteMessage[i * 3 + 1]; 
			instr[2] = (byte)byteMessage[i * 3 + 2]; //六个位为一组，补0变成4个字节 　　
			int[] outstr = new int[4];	//第一个输出字节：取第一输入字节的前6位，并且在高位补0，使其变成8位（一个字节） 　　
			outstr[0] = instr[0] >> 2; 	//第二个输出字节：取第一输入字节的后2位和第二个输入字节的前4位（共6位），并且在高位补0，使其变成8位（一个字节） 　　
			outstr[1] = ((instr[0] & 0x03) << 4) ^ (instr[1] >> 4); 	//第三个输出字节：取第二输入字节的后4位和第三个输入字节的前2位（共6位），并且在高位补0，使其变成8位（一个字节）
			if(instr[1]!=empty)
			  outstr[2] = ((instr[1] & 0x0f) << 2) ^ (instr[2] >> 6); 
			else outstr[2] = 64;	//第四个输出字节：取第三输入字节的后6位，并且在高位补0，使其变成8位（一个字节） 　　
				
			if (instr[2]!=empty)
			  outstr[3] = (instr[2] & 0x3f); 
			else 
			  outstr[3] = 64; 
			  outmessage.append(Base64Code[outstr[0]]); 
			  outmessage.append(Base64Code[outstr[1]]);
			  outmessage.append(Base64Code[outstr[2]]);
			  outmessage.append(Base64Code[outstr[3]]); 
		} 
		return outmessage.toString(); 
	}
	
	public static String J_encode(byte[] a) 
	{
		char[] codec_table = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
				 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
				 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
				'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
				'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
				'7', '8', '9', '+', '/' };
		
	     int totalBits = a.length * 8;
	     int nn = totalBits % 6;
	     int curPos = 0;// process bits
	     StringBuffer toReturn = new StringBuffer();
	      while (curPos < totalBits)
	      {
	    	  int bytePos = curPos / 8;
	    	  switch (curPos % 8) 
	    	  {
	    	  	case 0:
	    	  		toReturn.append(codec_table[(a[bytePos] & 0xfc) >> 2]);
	                      break;
	                  case 2:
	      
	                      toReturn.append(codec_table[(a[bytePos] & 0x3f)]);
	                      break;
	                  case 4:
	                      if (bytePos == a.length - 1) {
	                          toReturn
	                                  .append(codec_table[((a[bytePos] & 0x0f) << 2) & 0x3f]);
	                      } else {
	                          int pos = (((a[bytePos] & 0x0f) << 2) | ((a[bytePos + 1] & 0xc0) >> 6)) & 0x3f;
	                          toReturn.append(codec_table[pos]);
	                      }
	                      break;
	                  case 6:
	                      if (bytePos == a.length - 1) {
	                          toReturn
	                                  .append(codec_table[((a[bytePos] & 0x03) << 4) & 0x3f]);
	                      } else {
	                          int pos = (((a[bytePos] & 0x03) << 4) | ((a[bytePos + 1] & 0xf0) >> 4)) & 0x3f;
	                          toReturn.append(codec_table[pos]);
	                      }
	                      break;
	                  default:
	                      //never hanppen
	                      break;
	                  }
	                  curPos+=6;
	              }
	              if(nn==2)
	              {
	                  toReturn.append("==");
	              }
	              else if(nn==4)
	              {
	                  toReturn.append("=");
	              }
	              return toReturn.toString();
	      
	}



	

	
	/** 
	* 加密 
	* @param src 数据源 
	* @param key 密钥，长度必须是8的倍数 
	* @return 返回加密后的数据 
	* @throws Exception 
	*/ 
	
	public static byte[] encrypt(byte[] srcData, byte[] srcKey) throws Exception
	{ 
		try
		{
			//DES算法要求有一个可信任的随机数源 
			SecureRandom sr = new SecureRandom(); 
		// 从原始密匙数据创建DESKeySpec对象 
			byte[] key = expandZeroByte(srcKey);
			DESKeySpec dks = new DESKeySpec(key); 
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成 
		// 一个SecretKey对象 
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES); 
			SecretKey securekey = keyFactory.generateSecret(dks); 
		// Cipher对象实际完成加密操作 
			Cipher cipher = Cipher.getInstance(DES); 
		// 用密匙初始化Cipher对象 
			cipher.init(Cipher.ENCRYPT_MODE, securekey, sr); 
		// 现在，获取数据并加密 
		// 正式执行加密操作 
//			return cipher.doFinal(src);
			
			byte[] data = expandZeroByte(srcData);
			return cipher.doFinal(data);
			
		}
		catch(Exception e)
		{
			throw new Exception("");
		}	
	} 
	
	/** 
	* 解密 
	* @param src 数据源 
	* @param key 密钥，长度必须是8的倍数 
	* @return 返回解密后的原始数据 
	* @throws Exception 
	*/ 
	public static byte[] decrypt(byte[] srcData, byte[] srcKey) throws Exception{
		try
		{
			// DES算法要求有一个可信任的随机数源 
			SecureRandom sr = new SecureRandom(); 
		// 从原始密匙数据创建一个DESKeySpec对象 
			
			byte[] key = expandZeroByte(srcKey);
			DESKeySpec dks = new DESKeySpec(key); 
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成 
		// 一个SecretKey对象 
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES); 
			SecretKey securekey = keyFactory.generateSecret(dks); 
		// Cipher对象实际完成解密操作 
			Cipher cipher = Cipher.getInstance(DES); 
		// 用密匙初始化Cipher对象 
			cipher.init(Cipher.DECRYPT_MODE, securekey, sr); 
		// 现在，获取数据并解密 
		// 正式执行解密操作 
			
			byte[] dataBytes = cipher.doFinal(srcData);
			return reduceZeroByte(dataBytes);			
		}
		catch(Exception e)
		{
			throw new Exception("");
		}
	} 	
	
	public static String byte2hex(byte[] b) throws Exception{ // 一个字节的数，
		try
		{
			// 转成16进制字符串 
			String hs = ""; 
			String stmp = ""; 
			for (int n = 0; n < b.length; n++) 
			{ 
			// 整数转成十六进制表示 
				stmp = (java.lang.Integer.toHexString(b[n] & 0XFF)); 
				if (stmp.length() == 1) 
					hs = hs + "0" + stmp; 
				else 
					hs = hs + stmp; 
			} 
			return hs.toUpperCase(); // 转成大写 			
		}
		catch(Exception e)
		{
			throw new Exception("");
		}
	}
	
	public static byte[] hex2byte(byte[] b) throws Exception{ 
		try
		{
			if ((b.length % 2) != 0) 
				throw new IllegalArgumentException("长度不是偶数"); 
				byte[] b2 = new byte[b.length / 2]; 
				for (int n = 0; n < b.length; n += 2) { 
				String item = new String(b, n, 2); 
				// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节 
				b2[n / 2] = (byte) Integer.parseInt(item, 16); 
				} 

				return b2; 			
		}
		catch(Exception e)
		{
			throw new Exception("");
		}
	} 	
	
	
	// 末尾扩充0
	public static byte[] expandZeroByte(byte[] srcBytes)
	{
		int leftLength = srcBytes.length%8;
		byte[] retBytes = new byte[srcBytes.length+(8-leftLength)];
		
		System.arraycopy(srcBytes, 0, retBytes, 0, srcBytes.length);

		return retBytes;
	}
	
	public static byte[] reduceZeroByte(byte[] srcBytes)
	{
		int index=0;
		for(index=srcBytes.length-1; index>=0; index--)
		{
			if(srcBytes[index]!=0)
			{				
				break;
			}
		}
		byte[] retBytes = new byte[index+1];
		System.arraycopy(srcBytes, 0, retBytes, 0, index+1);
		return retBytes;
	}	
	
}
