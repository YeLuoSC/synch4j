package com.synch4j.execute.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.execute.service.ISynch2TestService;

@Service
@Transactional(readOnly=true)
public class Synch2TestServiceImpl implements ISynch2TestService{

	/*@Resource
	private ISynch2TestMapper synch2TestMapper;
	@Resource
	private ISettingGridService settingGridService;
	
	@Override
	public Table setTableDefine() {
		CommonGrid grid = new CommonGrid();
		grid.setTableID("bgt_t_timeplan");
    	grid.setTableDBName("bgt_t_timeplan");
    	grid.setTableName("bgt_t_timeplan");

		List<Column> columns = new ArrayList<Column>();
		Column docId = new Column();
		docId.setColumnDBName("DOCID");
		docId.setAlias("公文ID");
		docId.setColumnName("DOCID");
		docId.setReadOnly(true);
		columns.add(docId);

		Column fileGuid = new Column();
		fileGuid.setColumnDBName("FILEGUID");
		fileGuid.setAlias("文件ID");
		fileGuid.setColumnName("FILEGUID");
		fileGuid.setReadOnly(true);
		grid.setColumnList(columns);
		columns.add(fileGuid);

		Column province = new Column();
		province.setColumnDBName("PROVINCECODE");
		province.setAlias("导入省份码");
		province.setColumnName("PROVINCECODE");
		province.setReadOnly(true);
		columns.add(province);
		
		Column year = new Column();
		year.setColumnDBName("YEAR");
		year.setAlias("导入年度");
		year.setColumnName("YEAR");
		year.setReadOnly(true);
		columns.add(year);
		
		Column status = new Column();
		status.setColumnDBName("STATUS");
		status.setAlias("当前状态");
		status.setColumnName("STATUS");
		status.setReadOnly(true);
		columns.add(status);
		
		Column remark = new Column();
		remark.setColumnDBName("REMARK");
		remark.setAlias("执行时异常备注");
		remark.setColumnName("REMARK");
		remark.setReadOnly(true);
		remark.setShowType(ShowType.SHOW_TYPE_TEXT_AREA);
		columns.add(remark);
		
		Column direction = new Column();
		direction.setColumnDBName("DIRECTION");
		direction.setAlias("方向,A下发,R上报,X规范");
		direction.setColumnName("DIRECTION");
		direction.setReadOnly(true);
		columns.add(direction);
		
		Column appid = new Column();
		appid.setColumnDBName("APPID");
		appid.setAlias("APPID");
		appid.setColumnName("APPID");
		appid.setReadOnly(true);
		columns.add(appid);
		
		Column runtime = new Column();
		runtime.setColumnDBName("RUNTIME");
		runtime.setAlias("运行时间");
		runtime.setColumnName("RUNTIME");
		runtime.setReadOnly(true);
		columns.add(runtime);
		
		grid.setColumnList(columns);
		return grid;
	}

	@Override
	public Object getData(CommonGrid commonGrid) throws Exception {
		List resultList = synch2TestMapper.getData();
        settingGridService.transferGridData(resultList, commonGrid.getPageInfo());
        return commonGrid.getPageInfo();
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void reset(List<String> list) {
		synch2TestMapper.reset(list);
	}*/

}
