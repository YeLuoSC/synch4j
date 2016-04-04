var patternTree;
var patternForm;
var INPUTMODE = {
		ADD:"1",
		MODIFY:"2",
		DELETE:"3"
}
$(function(){
	init();
});

function init(){
	Hq.create({
		renderId : "mainDiv",
		xType  : "panel",
		width:$(window).width(),
		height:$(window).height()-10,
		border : false,
		layout : "split",
		closable : false,
		layoutConfig : {
			splitHorizontal : "V",
			fixedSize : false
		},
		items:[getLeftPanel(),getRightPanel()]
	});
};

//左侧树组件
function getLeftPanel(){
	return {
		xType : "panel",
		width : "20%",
		height: "100%",
		tbar  : getTreeToolbar(),
		border: false,
		items :[{
			id    : "patternTree", //唯一标识符
			xType : 'hqTree',	
			width : "25%",		
			treeType : "leftTree",
			definePath : 'synch2/freeAssign/getPatternTree.do',			
			allowBranchSelection : false,
			multipleChoice : false, //所选，清空，确定按钮
			chkboxFlag : false
		}],
		callback:{
			completed : function(){
				patternTree = Hq.getCmp("patternTree");
			}
		}
	};
	
}

//右侧布局
function getRightPanel(){
	return{
		xType : "panel",
		width : "80%",
		height: "100%",
		layout: "split",
		layoutConfig : {
			showSplitBar : false,
			splitHorizontal : "h"
		},
		border: false,
		items :[{
			xType : "grid",
			id    : "freeAssignGrid",
			tbar  : getGridToolbar(),
			height	   : "50%",
			pagination : false, 
			autoLoad   : true,
			columnsSeq : false,
			editable   : false,
			border     : true,
			definePath : "bil/setting/billType/getTableDefine.do",
			getDataPath : "bil/setting/billType/getTableData.do",
			params : {
				SUPERID : null,
				BILLTYPEID : null
			},
			callback : {
				completed : function(){
					
				}
			}
		}]
	};
};

//创建按钮区
function getTreeToolbar(){
	return [{
			type : 'button',
			text : "新增",
			disabled : false,
			icon : "add",
			id : "addPatternBtn",
			handler :function(){
				showPattern(INPUTMODE.ADD);
			}
		},  {
			type : 'button',
			text : "修改",
			icon : "style/flatblue/images/icon/edit.png",
			disabled : false,
			id   : "modifyPatternBtn",
			handler : function(){
				if(patternTree.currentNode != null){
					showPattern(INPUTMODE.MODIFY);
				}else{
					Hq.MessageBox.alert("请选择模式节点！");
				}
			} 
		},{
			type : 'button',
			text : "删除",
			icon : "style/flatblue/images/icon/delete.png",
			disabled : false,
			id   : "deletePatternBtn",
			handler : function(){
				if(patternTree.currentNode != null){
					Hq.Msg.confirm("确定删除"+patternTree.currentNode.name+"模式吗？",
					function(){
						Hq.ajax({
							url : "synch2/freeAssign/delPatternById.do",
							cache : false,
							type : "post",
							data : {
								patternId:patternTree.currentNode.id
							},
							dataType : "json",
							success : function(jsonReturn) {
								if(jsonReturn == "1"){
									patternTree.refresh();
									Hq.MessageBox.alert("删除成功！");
								}
									
							}
						});
					})
				}else{
					Hq.MessageBox.alert("请选择模式节点！");
				}
			} 
		}];
};

//创建按钮区
function getGridToolbar(){
	return [{
			type : 'button',
			text : "新增",
			disabled : false,
			icon : "add",
			id : "addTypeBtn",
			handler : function(){
			} 
		}, {
			type : 'button',
			text : "保存",
			icon : "save",
			id : "saveTypeBtn",
			handler : function(){
			} 
		}, {
			type : 'button',
			text : "删除",
			icon : "style/flatblue/images/icon/delete.png",
			disabled : false,
			id   : "deleteTypeBtn",
			handler : function(){
			} 
		},{
			type : 'button',
			text : "按类型复制",
			icon : "copy",
			disabled : false,
			id   : "billCopyBtn",
			handler : function(){
			} 
		}]
};

function showPattern(INPUTTYPE){
		var setting = {
				xType : "dialog",
				id:"patternDialog",
				isModel : true, // 是否锁屏
				isDrag : true, // 是否支持拖动
				isResize : false, // 是否支持拖动
				width : "800",
				height : "700",
				style : "white",
				content : "<div id='patternContent'></div>",
				title : function(){
					if(INPUTMODE.ADD == INPUTTYPE)
						return '新增自由模式';
					else if(INPUTMODE.MODIFY == INPUTTYPE)
						return '修改自由模式';
				}(),
				button:[{
					name:"确定",
					handler:function(){
						if(INPUTMODE.ADD == INPUTTYPE){
							
						}else if(INPUTMODE.MODIFY == INPUTTYPE){
							
						}
						var patternPOstr = Hq.getCmp("patternContent").getFormJsonData();
						Hq.ajax({
							url : "synch2/freeAssign/savePatternForm.do",
							cache : false,
							type : "post",
							data : {
								patternPO:patternPOstr
							},
							dataType : "json",
							success : function(jsonReturn) {
								
							}
						});
					}
				},{
					name:"关闭",
					handler:function(){
						Hq.getCmp("patternDialog").close();
					}
				}],
				initFn:function(){
					initPatternForm(INPUTTYPE);
				}
	}
	Hq.create(setting);
};

function initPatternForm(INPUTTYPE){
	var patternPO;
	setting = {
			xType : "form",
			renderId : "patternContent",
			saveDataPath : "synch2/freeAssign/savePatternForm.do",
			localConfigration : true,
			autoLoad:false,
			border : false,
			autoHeight:true,
			columnSize:2,//每行几个
			labelWidth:150,
			formItemList:[{
				"columnID" : "patternId",
				"columnName" : "patternId",
				"columnDBName" : "patternId",
				"dataType" : "S", 
				"showType" : "textfield",
				"scale" : 0,
				"regExpression" : null,
				"regInfo" : null,
				"readOnly" : true,
				"alias" : "模式ID",
				"visible" : false
			},{
				"columnID" : "patternName",
				"columnName" : "patternName",
				"columnDBName" : "patternName",
				"dataType" : "S", 
				"showType" : "textfield",
				"scale" : 0,
				"regExpression" : null,
				"regInfo" : null,
				"readOnly" : false,
				"alias" : "模式名称",
				"visible" : true,
				"nullable" : false,
				"rowSpan" : 2,
				"colSpan" : 2
			},{
				"columnID" : "isAlwaysAssign",
				"columnName" : "isAlwaysAssign",
				"columnDBName" : "isAlwaysAssign",
				"dataType" : "S",
				"showType" : "radio",
				"dataList":[{
					"name" : "是",
					"value" : "1"
				},{
					"name" : "否",
					"value" : "0"
				}],
				"readOnly" : false,
				"alias" : "是否采取增量形式下发？",
				"visible" : true,
				"nullable" : false,
				"rowSpan" : 2,
				"colSpan" : 1
			},{
				"columnID" : "templateData",
				"columnName" : "templateData",
				"columnDBName" : "templateData",
				"dataType" : "S",
				"showType" : "radio",
				"dataList":[{
					"name" : "是",
					"value" : "1"
				},{
					"name" : "否",
					"value" : "0"
				}],
				"readOnly" : false,
				"alias" : "是否下发模板数据？",
				"visible" : true,
				"nullable" : false,
				"rowSpan" : 2,
				"colSpan" : 1
			}, {
				"columnID" : "busiData",
				"columnName" : "busiData",
				"columnDBName" : "busiData",
				"dataType" : "S",
				"showType" : "radio",
				"dataList":[{
					"name" : "是",
					"value" : "1"
				},{
					"name" : "否",
					"value" : "0"
				}],
				"readOnly" : false,
				"alias" : "是否下发业务数据？（任务类型中定义的下发设置）",
				"visible" : true,
				"nullable" : false,
				"rowSpan" : 2,
				"colSpan" : 1
			},  {
				"columnID" : "systemData",
				"columnName" : "systemData",
				"columnDBName" : "systemData",
				"dataType" : "S",
				"showType" : "radio",
				"dataList":[{
					"name" : "是",
					"value" : "1"
				},{
					"name" : "否",
					"value" : "0"
				}],
				"readOnly" : false,
				"alias" : "是否下发系统表数据？(model,factor,公式表,任务相关表等)",
				"visible" : true,
				"nullable" : false,
				"rowSpan" : 2,
				"colSpan" : 1
			},{
				"columnID" : "settingData",
				"columnName" : "settingData",
				"columnDBName" : "settingData",
				"dataType" : "S",
				"showType" : "radio",
				"dataList":[{
					"name" : "是",
					"value" : "1"
				},{
					"name" : "否",
					"value" : "0"
				}],
				"readOnly" : false,
				"alias" : "是否下发设置类表数据？（synch_t_setting表中定义的）",
				"visible" : true,
				"nullable" : false,
				"rowSpan" : 2,
				"colSpan" : 1
			},{
				"columnID" : "importPartitionMode",
				"columnName" : "importPartitionMode",
				"columnDBName" : "importPartitionMode",
				"dataType" : "S",
				"showType" : "radio",
				"dataList":[{
					"name" : "application.properties",
					"value" : "1"
				},{
					"name" : "下发时所选目标的分区信息",
					"value" : "0"
				}],
				"readOnly" : false,
				"alias" : "导入时分区信息取自？",
				"visible" : true,
				"nullable" : false,
				"rowSpan" : 2,
				"colSpan" : 1
			},{
				"columnID" : "sourceProvince",
				"columnName" : "sourceProvince",
				"columnDBName" : "sourceProvince",
				"dataType" : "S", 
				"showType" : "textfield",
				"scale" : 0,
				"regExpression" : null,
				"regInfo" : null,
				"readOnly" : false,
				"alias" : "导出省份编码分区信息",
				"visible" : true,
				"nullable" : false,
				"rowSpan" : 2,
				"colSpan" : 1
			},{
				"columnID" : "sourceYear",
				"columnName" : "sourceYear",
				"columnDBName" : "sourceYear",
				"dataType" : "S", 
				"showType" : "textfield",
				"scale" : 0,
				"regExpression" : null,
				"regInfo" : null,
				"readOnly" : false,
				"alias" : "导出年份分区信息",
				"visible" : true,
				"nullable" : false,
				"rowSpan" : 2,
				"colSpan" : 1
			}],
			callback : {
				completed:function(){
					if(INPUTTYPE==INPUTMODE.MODIFY){
						patternForm = Hq.getCmp("patternContent");
						if(patternTree.currentNode != null){
							var patternId = patternTree.currentNode.id;
							Hq.ajax({
								url : "synch2/freeAssign/getPatternById.do",
								cache : false,
								type : "post",
								data : {
									patternId:patternId
								},
								dataType : "json",
								success : function(jsonReturn) {
									patternForm.setValueByColumnName("PATTERNNAME",jsonReturn.patternName);
									patternForm.setValueByColumnName("ISALWAYSASSIGN",jsonReturn.isAlwaysAssign);
									patternForm.setValueByColumnName("TEMPLATEDATA",jsonReturn.templateData);
									patternForm.setValueByColumnName("BUSIDATA",jsonReturn.busiData);
									patternForm.setValueByColumnName("SYSTEMDATA",jsonReturn.systemData);
									patternForm.setValueByColumnName("SETTINGDATA",jsonReturn.settingData);
									patternForm.setValueByColumnName("IMPORTPARTITIONMODE",jsonReturn.importPartitionMode);
									patternForm.setValueByColumnName("SOURCEPROVINCE",jsonReturn.sourceProvince);
									patternForm.setValueByColumnName("SOURCEYEAR",jsonReturn.sourceYear);
								}
							});
						}
					}
				}
			}
		};
	Hq.create(setting);
}