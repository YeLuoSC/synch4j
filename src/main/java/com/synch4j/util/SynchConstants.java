package com.synch4j.util;

public class SynchConstants {
	/**
	 * 回调接口所在包
	 */
	public static final String CALLBACK_PACKAGE = "com.synch4j.callback";
	
	/**
	 * 回调接口所在包
	 */
	public static final String CHANGE_DBNAME_SUFFIX = "_S";
	
	/**
	 * 配置文件中的导出文件夹Key
	 */
	public static final String EXPORT_DIR_KEY = "EXPORT.ZIPDIRECTORY";
	
	/**
	 * 默认导出最大行数
	 */
	public static final String EXPORT_DEFAULT_MAXROW_KEY = "EXPORT.DEFAULT.MAXROW";
	
	/**
	 * 系统表，基本无论何种模式都需要导出的表格
	 */
	public static final String SYSTEM_TABLE = "SYSTEM.TABLE";
	
			
	/**
	 * CommonExport的数据库解析器，获取SynchPO的解析器
	 */
	public static final String EXPORT_COMMON_DEFAULT_DB_RESOLVER = "EXPORT.COMMON.DB.RESOLVER";
	
	/**
	 * BAS数据库解析器的key,value为beanId,默认配置~如果有自己特殊的，自己扩展出一个新的key，在相关组件中，获取自己的key
	 */
	public static final String EXPORT_COMMON_DEFAULT_PROPERTIES_RESOLVER = "EXPORT.COMMON.PROPRETIES.RESOLVER";
	
	/**
	 * 默认的数据提取器配置Key，默认增量
	 */
	public static final String EXPORT_DEFAULT_DB_DATAPICKER_KEY = "EXPORT.DEFAULT.DATAPICKER";
	
	/**
	 * 默认的SQL生成器配置Key，默认增量
	 */
	public static final String EXPORT_DEFAULT_SQLGENERATOR_INC_KEY = "EXPORT.DEFAULT.DATAPICKER.SQLGENERATOR.INC";
	
	/**
	 * 通用SQL生成器的忽略列配置Key
	 */
	public static final String EXPORT_COMMON_SQLGEN_IGNORE_COL_KEY = "EXPORT.COMMON_SQLGENERATOR.IGNORE.COL";
	
	/**
	 * 通用SQL生成器的非增量表配置Key
	 */
	public static final String EXPORT_COMMON_SQLGEN_NOINC_TABLE_KEY = "EXPORT.COMMON_SQLGENERATOR.NOINC.TABLE";
	
	/**
	 * 单线程导入模式的解析器配置Key
	 */
	public static final String IMPORT_SINGLE_RESOLVER_KEY = "IMPORT.SINGLE.RESOLVER";

	/**
	 * 单线程导入模式的解析器配置Key
	 */
	public static final String IMPORT_SINGLE_DATAIMPORTER_KEY = "IMPORT.SINGLE.DATAIMPORTER";
	
	/**
	 * 单线程导入模式的SQL生成器配置Key
	 */
	public static final String IMPORT_SINGLE_SQLGENERATOR_KEY = "IMPORT.SINGLE.DATAIMPORTER.SQLGENERATOR";
	
	/**
	 * 固定行列表和浮动表的导出条件，必须导出模板数据
	 */
	public static final String A1ORA2_CONDITION = " ISTEMPLATE='1' ";
	
	/**
	 * 开发模式的配置文件key
	 */
	public static final String DEVELOPE_MODE_KEY = "SYNCH2.DEVELOPE.MODE";
	
	/**
	 * 导出文件时，同步信息的保存文件名
	 */
	public static final String SYNCH_INFO_TXT = "SYNCH_INFO";
	
	public static final String PROVINCE_CONDITION = " AND PROVINCE=GLOBAL_MULTYEAR_CZ.SECU_F_GLOBAL_PARM('DIVID') ";
	
	public static final String YEAR_CONDITION = " AND YEAR=GLOBAL_MULTYEAR_CZ.SECU_F_GLOBAL_PARM('YEAR') ";
	
	/**
	 * 远程脚本表
	 */
	public static final String TABLE_REMOTEPROCEDURE_NAME = "P#SYNCH_T_REMOTEPROCEDURE";
	
	public static final String MODEL_TABLE = "P#DICT_T_MODEL";
	
	public static final String FACTOR_TABLE = "P#DICT_T_FACTOR";
	
	public static final String VIEWCODE_TABLE = "P#SYNCH_T_VIEWCODE";
	/**
	 * 普通录入表
	 */
	public static final String TABLE_TYPE_INPUT = "1";
	
	/**
	 * 附件表
	 */
	public static final String TABLE_TYPE_ATTACH = "2";
	
	/**
	 * 附件ID
	 */
	public static final String ATTACHMENT_COL_ATTACHID = "FILEID";
	
	/**
	 * 附件名称
	 */
	public static final String ATTACHMENT_COL_FILENAME = "FILENAME";
	
	/**
	 * 说明文件
	 */
	public static final String SYNCH_ZIP_EXPLAIN = "EXPLAINFILE";
	
	/**
	 * 业务数据
	 */
	public static final String SYNCH_ZIP_BUSIDATA = "BUSIDATAFILE";
	
	/**
	 * 大数据文件
	 */
	public static final String SYNCH_ZIP_BIGDATA = "BIGDATAFILE";
	
	/**
	 * 附件文件
	 */
	public static final String SYNCH_ZIP_ATTACHMENT = "ATTACHMENTFILE";
	
	/**
	 * 日期格式
	 */
	public static final String SYSTIMESTAMP_FORMAT = "'yyyy-mm-dd hh24:mi:ss.ff9'";
	
	/**
	 * 导出文件类型
	 */
	public static final String FILE_TYPE = ".TXT";
	
	public static final String BGT_APPID = "bgt";
	
	public static final String SPF_APPID = "spf";
	
	public static final String BAS_APPID = "bas";
	
	public static final String DATA_SPLIT = "TjhQ";
	
	public static final String SECURE_KEY = "CZ";
	
	public static final String XCH_STANDARD_DOCID = "规范模式下发-";
	
	public static final String XCH_SPF_DOCID = "SPF模式下发-";
	
	/**
	 * 上下级同步动作类型： A为下发，R为上报 , X为不走OA手动下发
	 */
	public static final String ASSIGN = "A";
	
	public static final String REPORT = "R";

	public static final String XCHASSIGN = "X";
	
	public static final String CURRENT_APPID_KEY = "SYNCH2.APPID";
	
	public static final String CHECKDEF_TABLE = "P#BGT_T_CHECKDEF";
	
	public static final String CHECKSORT_TABLE = "P#BGT_T_CHECKSORT";
	
	public static final String CHECKDEF_BUSINESS_TABLE = "P#BGT_T_BUSINESSCHECKDEF";
	
	public static final String FORMULADEF_TABLE = "P#FORMULA_T_FORMULADEF";
	
	public static final String REF_RELA_TABLE = "P#DICT_T_SETREFRELA";
	
	public static final String SINDETAIL_TABLE = "P#DICT_T_SINRECDETAIL";
	
}
