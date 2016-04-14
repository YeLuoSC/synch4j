package com.synch4j.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.synch4j.po.SynchPO;

public class SynchToolUtil {
	
	private static Logger logger = Logger.getLogger(SynchToolUtil.class);
	
	public static String GUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}
	
	public static String getValueFromProperties(String key){
		Properties prop = new Properties();
		try {
			InputStream in = SynchToolUtil.class.getResourceAsStream("/synch4j.properties");
			prop.load(in);
			return prop.getProperty(key);
		} catch (FileNotFoundException e) {
			logger.error("synch4j的配置文件未找到！synch4j.properties请检查!");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			logger.error("synch4j的配置文件读取出错！synch4j.properties请检查!");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 把Map转换成Bean
	 * @param dataList
	 * @return
	 * key     PHYSDBNAME,
		       TABLENAME,
		       BIGDATA,
		       SYNCHORDER,
		       SYNCHCONDITION,
		       SYNCHEDHANDLER,
		       SYNCHRECOGCOL,
		       TABLETYPE,
		       PKCOL,
		       NETTYPE,
		       MAXROW,
		       REMARK
	 */
	public static List<SynchPO> convertSynchPOList(List<Map<String, Object>> dataList) {
		List<SynchPO> resultList = new ArrayList<SynchPO>();
		if (dataList == null) return resultList;
		Map<String, Object> dataMap = null;
		SynchPO synchPO = null;
		for (int i = 0; i < dataList.size(); i++) {
			dataMap = dataList.get(i);
			synchPO = new SynchPO();
			synchPO.setPhysDBName(((String)dataMap.get("PHYSDBNAME")).trim());
			synchPO.setTableName((String)dataMap.get("TABLENAME"));
			synchPO.setIsBigDataCol((String)dataMap.get("BIGDATA"));
			if(dataMap.get("FILTERCOL") != null){
				synchPO.setFilterCol(dataMap.get("FILTERCOL").toString());
			}
			synchPO.setSynchOrder(dataMap.get("SYNCHORDER") == null?0: Integer.valueOf(String.valueOf(dataMap.get("SYNCHORDER"))));
			if(dataMap.get("SYNCHCONDITION") != null && !"".equals(((String)dataMap.get("SYNCHCONDITION")).trim())){
				synchPO.setSynchCondition((String)dataMap.get("SYNCHCONDITION"));
			}
			if(dataMap.get("SYNCHCONDITION") == null){
				synchPO.setSynchCondition(" WHERE 1=1 ");
			}
			//代码走到此处，objBean中的条件一定不为null了；
			synchPO.setSynchRecogCol(dataMap.get("SYNCHRECOGCOL") == null?"":(String)dataMap.get("SYNCHRECOGCOL"));
			synchPO.setTableType(dataMap.get("TABLETYPE") == null?"":(String)dataMap.get("TABLETYPE"));
			synchPO.setRemark(dataMap.get("REMARK") == null?"":(String)dataMap.get("REMARK"));
			synchPO.setIsSynch(dataMap.get("ISSYNCH") == null?"":(String)dataMap.get("ISSYNCH"));
			synchPO.setIsAlwaysExport(dataMap.get("ISALWAYSEXPORT") == null?"":(String)dataMap.get("ISALWAYSEXPORT"));
			if ((String)dataMap.get("PKCOL") != null) {
				for (String str : ((String)dataMap.get("PKCOL")).trim().split(",")) {
					if(synchPO.getPkList() != null){
						synchPO.getPkList().add(str);
					}else{
						List<String> pkList = new ArrayList<String>();
						pkList.add(str);
						synchPO.setPkList(pkList);
					}
				}
			}
			resultList.add(synchPO);
		}
		return resultList;
	}
	
	public static String getOracleValue(String dataType, String value) {
		String colValue = "";
		if (dataType.equals("DATE")) {
			colValue = " to_date('" + value
					+ "', 'yyyy-mm-dd hh24:mi:ss') ";
		} else if (dataType.startsWith("TIMESTAMP")) {
			colValue = " to_timestamp('" + value
					+ "', 'yyyy-mm-dd hh24:mi:ss.ff9')";
		} else {
			if(value.equalsIgnoreCase("EMPTY_CLOB()") || value.equalsIgnoreCase("EMPTY_BLOB()")){
				//这里只有导入时候会进入！因为在导出时，我将clob和blob类型的列名也一起导出了，对应的值写死为empty_clob()了，
				//所以在导入时候，当判断是这个值得时候，不能加单引号！插入的是个函数
				colValue = value;
			}else{
				colValue = value.replaceAll("'", "''");
				colValue = "'" + colValue + "'";
			}
			
		}
		
		return colValue;
	}
	
	public static byte[] removeNull(byte[] rSource, int alength) {
		try {
			byte[] aResult = new byte[alength];
			System.arraycopy(rSource, 0, aResult, 0, aResult.length);
			return aResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用来拼接成 物理表名.txt形式的字符串，会保存日志中
	 * @param name
	 * @return
	 */
	public static String getFileName(String name) {
		return name.concat(SynchConstants.FILE_TYPE);
	}
	
	/**
	 * 用来拼接成 物理表名-次数.txt形式的字符串，如果同一张表有两个同步对象会出现a-1.txt,a-2.txt两个文件名
	 * @param name
	 * @return
	 */
	public static String getCountFileName(String name,Map<String,Integer> countPhysdbNameMap) {
		int count;
		if(countPhysdbNameMap.get(name) == null){
			countPhysdbNameMap.put(name, 1);
			count = 1;
		}else{
			count = countPhysdbNameMap.get(name);
			count++;
			countPhysdbNameMap.put(name, count);
		}
		return name.concat("-" + count + "" + SynchConstants.FILE_TYPE);
	}
	
	/**
	 * 将两个字节数组，拼接成一个首尾相接
	 * @param a1
	 * @param a2
	 * @param a2Len
	 * @return
	 * @throws Exception
	 */
	public static byte[] join(byte[] a1, byte[] a2, int a2Len)
			throws Exception {
		if (a1 == null) {
			return removeNull(a2, a2Len);
		}
		byte[] result = new byte[a1.length + a2Len];
		System.arraycopy(a1, 0, result, 0, a1.length);
		System.arraycopy(a2, 0, result, a1.length, a2Len);
		return result;
	}
	
	/**
	 * 将字符串按照分隔符切割，最后返回数组
	 * @param str
	 * @param splitStr
	 * @return
	 */
	public static String[] split(String str, String splitStr) {
		List<String> dataList = new ArrayList<String>();
		int indx = str.indexOf(splitStr);
		while (indx >= 0) {
			dataList.add(str.substring(0, indx));
			str = str.substring(indx + splitStr.length());
			indx = str.indexOf(splitStr);
		}
		dataList.add(str);
		
		String [] datas = new String [dataList.size()];
		for (int i = 0; i < dataList.size(); i++) {
			datas[i] = dataList.get(i);
		}
		return datas;
	}
	
	/**
	 * 保存Bean转换成Map
	 * @param dataList
	 * @return
	 */
	public static Map<String, Object> getSynchPOMap(SynchPO synchPO) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("physDBName", synchPO.getPhysDBName());
		resultMap.put("synchOrder", synchPO.getSynchOrder());
		resultMap.put("synchCondition", synchPO.getSynchCondition());
		resultMap.put("pkCol", ArrayListJoin(synchPO.getPkList(), ","));
		resultMap.put("synchRecogCol", synchPO.getSynchRecogCol());
		resultMap.put("maxRow", synchPO.getMaxRow());
		resultMap.put("tableType", synchPO.getTableType());
		resultMap.put("remark", synchPO.getRemark());
		resultMap.put("filterCol", synchPO.getFilterCol());
		return resultMap;
	}
	
	public static String ArrayListJoin(List<String> list, String separator) {
		String resultStr = "";
		if (list == null) return resultStr; 
		for (String string : list) {
			resultStr = resultStr.concat(string).concat(separator);
		}
		return resultStr.substring(0, resultStr.length() - 1);
	}
	
	public static void deleteFilesInDirectory(String dirPath) throws IOException {
		File dirFile = new File(dirPath);
		if (dirFile.exists() && dirFile.isDirectory()) {
			File[] childFiles = dirFile.listFiles();
			for (File childFile : childFiles) {
				FileUtils.forceDelete(childFile);
			}
		}
	}
	
}
