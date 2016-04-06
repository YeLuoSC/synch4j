package com.synch4j.exp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.synch4j.Synch2Context;
import com.synch4j.datapicker.DataPickerManager;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.DataPickerException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.exception.NotSupportAppIdException;
import com.synch4j.exp.dao.ExportMapper;
import com.synch4j.log.service.ISynchLogService;
import com.synch4j.po.SynchMainLogPO;
import com.synch4j.po.SynchPO;
import com.synch4j.resolver.SynchResolverManager;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.synchenum.LogType;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;
import com.synch4j.zip.CZipOutputStream;
import com.synch4j.zip.ZipEntry;

/**
 * @author XieGuanNan
 * @date 2015-8-12-上午11:23:13
 * 策略算法的分离，该类是导出算法策略的一个模板，提供一套默认实现，
 * 根据算法的不同，通过继承该类，获得默认实现，并在此基础上，进行扩展
 * 如果算法变动很大，也可直接实现IBaseExportStrategy接口
 *
 * @date 2015-8-25-上午11:23:13
 * 重构了这个类，加入了一个final方法(endExport方法)，这个final方法中，我会固定将同步对象的部分信息写入文件中，并打包，
 * 因为这样，在导入方可以直接通过这个文件读取到所有需要同步的表、主键等信息，而从正常逻辑来说，导入方也应该
 * 从导出方的名单中获取需要同步的表，而不应该是和以前一样去查导入方的表，浪费时间不说，还非常容易出现漏导的表！
 * 所以以后这个抽象类只能是打包成Zip的模板。假如以后出现了别的形式，那么应该直接重新实现接口！
 * 
 * @date 2015-8-26-上午11:23:13
 * 又对该类进行了重构，将prepareExport及endExport全部改为了final方法，endExport改为只对流进行关闭，而将原来生成
 * synchInfo.txt的方法放到了prepareExport中。造成原因是导出时，该文件最后写入的压缩包，导致了在导入的时候，该文件解析被迫最后一个
 * 解析，但我的算法中必须要最先解析才可以，否则无法处理导入的同步对象，CZipInputStream有reset方法，该方法没有实现，而是直接抛出IO异常，
 * 这样的话，只能在导出时做手脚，将该文件作为第一个导出文件，综合来看，这样做也是可行的。子类只需要重写startExport方法即可，而获取同步对象
 * 在开始导出前就已经由模板类实现完成了！
 * 
 * @date 2016-1-21-下午17:32:13
 * 将原先子类中的共用代码提到了模板类中，startExport()方法，将exportMapper和synchLogService注入到了这里，现在子类继承
 * 这个模板类，只需要实现3个方法即可完成；另外原先导出时，没有切换分区，现在加上了
 * 
 * @date 2016-3-11
 * 之前修改过了，导出不需要切分区，改回去了，导出时有用户信息，不需要切换分区
 * 修改了zos,fos流的关闭形式，之前如果导出出现异常，流不会被关闭
 */
public abstract class AbsExportZipStrategy implements IBaseExportStrategy{
	
	Logger logger = Logger.getLogger(AbsExportZipStrategy.class);
	
	protected Synch2Context context;
	
	protected String exportFileName;
	
	protected long startTime;
	
	protected String zipPath;

	protected CZipOutputStream zos = null;
	
	protected FileOutputStream fos = null;
	
	protected List<SynchPO> synchList = null;

	protected ExportMode mode;
	
	@Resource
	protected ExportMapper exportMapper;
	
	@Resource
	protected ISynchLogService synchLogService;
	
	/**
	 * 用于记录当前物理表被导出了几次，用于将次数拼成压缩包busidata/表名-次数.txt
	 */
	protected Map<String,Integer> countPhysdbNameMap = null;
	
	@Override
	public void export(Synch2Context context)
			throws CallbackException, NotSupportAppIdException,
			ErrorConfigureException, DataPickerException, IOException {
		try{
			this.context = context;
			
			setNewFileName();
			
			//子类要最先设置自己的模式标记位
			mode = setExportMode();
			
			//导出前，一些处理工作；
			prepareExport();
			
			//开始导出
			startExport();
			
			//导出完成后的一些处理；
			endExport();
		}finally{
			try{
				zos.close();
				fos.close();
			}catch(Exception e){
				//不用打了吧，没什么意义
				//e.printStackTrace();
				//logger.error("关闭时出错，可能已经被关闭了",e);
			}
		}
	}
	
	public abstract ExportMode setExportMode();

	/**
	 * 导出前的处理
	 * 这里做的事情,最主要的是synchList的值，这个方法里将需要导出表数据的同步对象SynchPO放到synchList中去
	 * 最后将表的一些同步信息写到synchInfo.txt中
	 * @throws CallbackException 
	 */
	public final void prepareExport() throws NotSupportAppIdException, ErrorConfigureException,IOException, CallbackException {
		
		//删除视图源码表和开发模式中的导出SQL
		exportMapper.truncateTempData();
		
		zipPath = SynchToolUtil.getValueFromProperties(SynchConstants.EXPORT_DIR_KEY);
		checkFileDir();
		//拼出导出包的绝对路径
		zipPath += File.separator + exportFileName;
		try {
			fos = new FileOutputStream(zipPath);
			zos = new CZipOutputStream(fos, "GBK");
			
			synchList =  SynchResolverManager.getExportSynchList(mode, context);;
			
			//导出前调用，实现类的接口，如果子类算法不需要回调，可以重写该方法,空实现；
			prepareExportCallback(synchList);
			
			//synchList信息第一个写入synchInfo.txt文件中，导入时，根据此文件进行解析，获取同步对象
			//会写入：物理表名，主键，同步顺序以及TABLEID(仅限业务表)
			if(synchList != null){
				StringBuffer synchInfo = new StringBuffer("EXPORT_MODE:" + mode + "\r\n");
				for(SynchPO synchPO : synchList){
					List<String> pkList = synchPO.getPkList();
					if(pkList.size() == 0 || (pkList.size() == 1 && pkList.get(0).equalsIgnoreCase("STATUS"))){
						//synchPO构造函数中，有new pklist的语句，所以只能判断是否为空
						logger.error("该"+synchPO.getPhysDBName()+"表未设置主键或仅设置了1个错误的主键-STATUS！分别检查物理表和SYNCH_T_SETTING中是否配置了正确的主键！注：无论STATUS是否为物理主键，均不能设置该值！");
						throw new ErrorConfigureException("该"+synchPO.getPhysDBName()+"表未设置主键或仅设置了1个错误的主键-STATUS！分别检查物理表和SYNCH_T_SETTING中是否配置了正确的主键！注：无论STATUS是否为物理主键，均不能设置该值！");
					}
					String pkStr = "";
					for(String pk : pkList){
						if(pk.equalsIgnoreCase("STATUS")){
							//不允许STATUS为主键，如果检测到，自动跳过！导出时有这个无所谓，关键在导入时。
							continue;
						}
						pkStr += pk + "#";
					}
					pkStr = pkStr.substring(0,pkStr.length() - 1);
					String tableId = "";
					if(synchPO.getTableId() != null){
						tableId = synchPO.getTableId();
					}
					synchInfo.append(synchPO.getPhysDBName() + "," + pkStr + "," + synchPO.getSynchOrder() + "," + tableId + "\r\n");
				}
				ZipEntry headEntry = new ZipEntry(SynchToolUtil.getFileName(SynchConstants.SYNCH_INFO_TXT));
				zos.putNextEntry(headEntry);
				zos.write(synchInfo.toString().getBytes("GBK"));
				zos.flush();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 导出前，调用开发人员实现的ExportPostProcess接口,由各个子类去实现
	 * @throws CallbackException 
	 */
	public abstract void prepareExportCallback(List<SynchPO> list) throws CallbackException;
	
	/**
	 * 开始导出,默认实现
	 * @throws CallbackException
	 */
	public void startExport() throws DataPickerException, ErrorConfigureException, CallbackException, IOException{
			
			//用于存放当前导出的表格为第几次导出，因为同一张表可能导出多次
			countPhysdbNameMap = new HashMap<String,Integer>();
			// 记录日志
			saveExportMainLog();
			if(logger.isInfoEnabled()){
				logger.info(startTime + ":当前策略为“" + mode +"”,数据开始导出！");
				logger.info("开始导出数据");
			}
			Map<String, String> tableValues = null;
			List<Map<String, String>> tableValueList = new ArrayList<Map<String,String>>();
			StringBuffer fileHeadInfo = new StringBuffer("EXPORT_MODE:" + mode +"\r\n");
			// 记录导出的详细信息
			Map<String, String> exportInfo = new HashMap<String, String>();
			for (int i = 0; i < synchList.size(); i++){
				SynchPO synchPO = synchList.get(i);
				tableValues = DataPickerManager.getData(mode,context,synchPO, zos);
				if (tableValues != null) {
					String outRow = tableValues.get("outRow");
					String lastDate = tableValues.get("lastDate");
					tableValues.put("physDbName", synchPO.getPhysDBName().trim());
					tableValues.put("tableType", synchPO.getTableType());
					exportInfo.put(synchPO.getPhysDBName(), synchPO
							.getPhysDBName()
							+ "," + lastDate + "," + outRow + "\r\n");
					// 暂时先顺序记录
					fileHeadInfo.append(exportInfo
							.get(synchPO.getPhysDBName()));
					tableValueList.add(tableValues);
				}
			}
			ZipEntry headEntry = new ZipEntry(SynchConstants.SYNCH_ZIP_EXPLAIN + "/"
					+ SynchToolUtil.getFileName(SynchConstants.SYNCH_ZIP_EXPLAIN));
			zos.putNextEntry(headEntry);
			zos.write(fileHeadInfo.toString().getBytes("GBK"));
			
			for (int i = 0; i < tableValueList.size(); i++) {
				tableValues = tableValueList.get(i);
				String outInfo = tableValues.get("outInfo");
				String physDbName = tableValues.get("physDbName");
				String tableType = tableValues.get("tableType");
				zos.putNextEntry(new ZipEntry(SynchConstants.SYNCH_ZIP_BUSIDATA
						+ "/"
						+ SynchToolUtil.getCountFileName(physDbName,countPhysdbNameMap)));

				zos.write(outInfo.getBytes("GBK"));
				
				// 如果是附件表 则需要同步附件
				//2016.1.21未来如果需要同步附件，也不需要使用这种方式，可以考虑使用XCH直接把FILEGUID传给XCH，由其负责
				if (tableType.equals(SynchConstants.TABLE_TYPE_ATTACH)) {
					logger.info("attach==null?"+tableValues.get("attach")+",attach:"+tableValues.get("attach"));
					if(tableValues.get("attach") != null && (!StringUtils.isEmpty(tableValues.get("attach")))){
						String[] attachs = tableValues.get("attach").split(",");
						if(logger.isInfoEnabled()){
							logger.info("当前附件表表名："+physDbName+",attachs:"+tableValues.get("attach"));
						}
						if(attachs != null && attachs.length > 0){
							exportMapper.insertFileId(attachs);
						}
					}
				}
			}
			//更新导出使用时间等信息到主日志
			synchLogService.updateMainLog(context.getLogId(), exportMapper.getDbDate(),String
					.valueOf((System.currentTimeMillis() - startTime) / 1000));
	}
	
	/**
	 * 保存导出主日志信息，如果有特殊处理，重写该方法
	 */
	public void saveExportMainLog(){
		String userName = "";//SecureUtil.getCurrentUser().getName();
		String exportStartTime = exportMapper.getDbDate();
		SynchMainLogPO mainLog = new SynchMainLogPO();
		mainLog.setLogId(context.getLogId());
		mainLog.setUserId(userName);
		mainLog.setFileName(exportFileName);
		mainLog.setDistrictId("");
		mainLog.setStartDate(exportStartTime);
		mainLog.setDirection("O");
		
		synchLogService.saveLog(mainLog,LogType.MAINLOG);
	}
	
	/**
	 * 导出文件名字的方法及路径，如果有特殊需求，请在子类中重写该方法
	 * @param sourceProvince
	 * @param date
	 */
	public void setNewFileName() {
		startTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String date = sdf.format(new Date());
		exportFileName =  "[" + date + "].zip";
	}
	
	/**
	 * 导出完成后的回调,调用开发人员实现的ExportPostProcess接口,由各个子类去实现
	 * @param zipPath TODO
	 */
	public abstract void endExportCallback(String zipPath) throws CallbackException;
	
	/**
	 * 导出后的处理
	 * @throws IOException 
	 * @throws CallbackException 
	 */
	public final void endExport() throws IOException, CallbackException{
		try{
			zos.flush();
			zos.close();
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new IOException();
		}
		//导出完成后，回调；
		endExportCallback(zipPath);
		logger.info("文件名为("+exportFileName+")数据导出成功！");
	}
	
	/**
	 * 检查是否存在文件目录
	 */
	private void checkFileDir(){
		File file = new File(zipPath);
		if(!file.exists()){
			file.mkdir();
		}
	}
	
}
