<!DOCTYPE html> 
<html>
  <head>
    <title>数据下发设置</title>
	<link rel="stylesheet" href="static/app/synch/css/styles/jqx.base.css" type="text/css" /> 
	<link rel="stylesheet" href="static/app/synch/css/styles/jqx.ui-smoothness.css" type="text/css" />  
	<link rel="stylesheet" href="static/app/synch/css/zTreeStyle/zTreeStyle.css" type="text/css" />  
  	<script type="text/javascript" src="static/app/synch/js/jqxGrid/jqx-all.js"></script>   
  	<script type="text/javascript" src="static/json/json2.js"></script>   
  	<script type="text/javascript" src="static/jquery-plugin/ztree/js/jquery.ztree.all-3.5.min.js"></script> 
  	<script type="text/javascript" src="static/app/synch/js/jquery-plugin/blockUI/jquery.blockUI.js"></script>
  	<script type="text/javascript"> 
  	var roleType ='';//= '<%=roleType%>'; 
  	var suitTree;
  	var rightPanelWidth = ((($(window).width()-30)/5)*4);
  	var arrTable = new Array();
  	
  	$(function(){
  		 $('#mainSplitter').jqxSplitter({ width: $(window).width()-30, height: $(window).height()-30, panels: [{ size: ($(window).width()-30)/5 }] });
  		$.ajax({
            contentType: "application/json",   
            url: "synch/assignConfig/getAppSelect.do",  
            dataType: "json", 
            success: function(data){ 
            	$.each(data, function(index, item) {
					$("#appSelect").append("<option value='"+item.APPID+"'>"+item.APPNAME+"</option>");
				});
            }
        });
  		$("#appSelect").on("change",function(){
  			if($("#appSelect").val() != undefined){
  				var setting = {
 						 async : {
 							 enable : true,
 							 otherParam:{appId:$("#appSelect").val()},
 							 url :  "synch/assignConfig/getSuitTree.do"
 						 },
 						 data : {
 							 key : {
 							 	name : "SUITNAME"
 							 },
 						 	simpleData : {
 							 	enable : true,
 							 	idKey : "SUITID",
 							 	pIdKey : "SUPERID",
 								rootPId:"0"
 							 }
 						 },
 						 callback:{
 							 onClick:function(e, treeId,treeNode){
 								 getRightPanel(treeNode.SUITID);
 							 }
 						 }
 							
 					 };
 				suitTree = $.fn.zTree.init($("#suitTree"),setting,null);
  			}else{
  				alert("请选择业务系统！");
  			}
  		});
  		$('#assignWindow').jqxWindow({
        	showCollapseButton: true,
  			height: 530,
  			width: 600,   
  			title:'请选择下发的地区',
  			isModal:true,
  			autoOpen:false, 
  			position: {x:($(window).width()/2)-150, y: $(window).height()/10},     
  			initContent: function () { 
  				var setting = {
  						 async : {
  							 enable : true,
  							 otherParam:{},
  							 url :  "synch/execute/getProvinceTree.do"
  						 },
  						 data : {
  							 key : {
  							 	name : "NAME"
  							 },
  						 	simpleData : {
  							 	enable : true,
  							 	idKey : "GUID",
								pIdKey : "SUPERGUID"
  							 }
  						 },
  						 check: {
  							 enable: true
  						 }
  							
  					 };
  					 var assignTree = $.fn.zTree.init($("#assignTree"),setting,null);
      		}     
   	    });
  	})
  	
  	function getRightPanel(suitId){
  		$("#tableInfo").css("display","");
  		$("#jqxgrid").css("display","none");
  		var source =
        {
        	datatype: "json",
        	type:'post', 
            datafields: [
                { name: 'ISCHECK',type: 'bool'},
                { name: 'DBTABLENAME',type: 'string' },
                { name: 'NAME', type: 'string' }, 
                { name: 'DEALNAME',type:'string'},
                { name: 'TABLEID',type:'string'}
            ], 
            //root:'result', 
            //pagenum: 0,                 
            //pagesize: 50,
            data:{suitId:suitId}, 
            /*pager: function (pagenum, pagesize, oldpagenum) { 
            	alert(pagenum);              
            }, */
            url: "synch/assignConfig/getTableInfo.do?tokenid=<%=tokenID %>"
        }; 
  		 var dataAdapter = new $.jqx.dataAdapter(source, {
             downloadComplete: function (data, status, xhr) { },
             loadComplete: function (data) {
            	 orignalData = data;
             },
             loadError: function (xhr, status, error) {
             	alert(error);
             }
         });
  		 
  		 $("#tableInfo").jqxGrid({
         	width:rightPanelWidth,  
         	height:$(window).height(),  
            source: dataAdapter,                 
            showtoolbar: true,
            editable: true, 
            columnsresize: true,
            //localization: getLocalization(),  
            columns: [ 
               {text: '导入选择',  datafield: 'ISCHECK',columntype:'checkbox',cellsalign: 'center',width: 100}, 
               {text: '物理表名',  datafield: 'DBTABLENAME', align: 'center', cellsalign: 'left', width: 200,editable:false}, 
               {text: '中文名称',  datafield: 'NAME', align: 'center', cellsalign: 'left', width: 200,editable:false},
               {text: '表处理类型',  datafield: 'DEALNAME', align: 'center', cellsalign: 'left', width: 200,editable:false},
               {text: '表ID',  datafield: 'TABLEID', align: 'center', cellsalign: 'left', width: 200,editable:false}
             ],
             rendertoolbar: function (toolbar) {
            	 toolbar.empty();
            	 var container = $("<div style='margin: 5px;'></div>");
                 var nextBtn = $("<input id='nextBtn' value='下一步' type='button' style='height: 23px; float: left; width: 100px;' />");
                 toolbar.append(container);
                 container.append(nextBtn);
                 nextBtn.on('click',nextBtnClickHandler)
             }

  		 });
  	}
  	
  	function nextBtnClickHandler(e){
  		var data = $('#tableInfo').jqxGrid('getrows');
  		$.each(data,function(index,item){
  			if(item.ISCHECK==true){
  				arrTable.push(item);
  			}
  		});
  		$("#tableInfo").css("display","none");
  		$("#jqxgrid").css("display","");
  		getjqxgrid(arrTable);
  		
  	}
  	
  	function getjqxgrid(arrTable){
  		var orignalData = null;
  		var editable = true;
  		if(roleType=='ROLE_EXEC'){
  			editable = false;
  			
  			document.getElementById("synchconfigMainBody").style.display = "none";
  			return;
  		}
  		
  		$.jqx.theme = 'ui-smoothness';              
  		
        var yesornolist = [{label:'是',value:1},{label:'否',value:0}];     
        var yesornoSource =   {                 
        		datatype: "array",                 
        		datafields: [ { name: 'label', type: 'string' },{ name: 'value', type: 'string' } ],                 
        		localdata: yesornolist           
        };           
        var yesornoAdapter = new $.jqx.dataAdapter(yesornoSource, {autoBind: true});
  		
        var tabletypelist = [{label:'系统表',value:0},{label:'普通录入表',value:1},{label:'附件表',value:2}]; 
        var tabletypeSource =   {                 
        		datatype: "array",                 
        		datafields: [ { name: 'label', type: 'string' },{ name: 'value', type: 'string' } ],                 
        		localdata: tabletypelist           
        };           
        var tableTypeAdapter = new $.jqx.dataAdapter(tabletypeSource, {autoBind: true}); 
        var physDBName = $('#physDBName').val();
    	var tableName = $('#tableName').val();
  		var url = "synch/assignConfig/getAssignSynchConfigList.do?&tokenid=<%=tokenID %>"; 
  		var tableIdList = "";
		var json = Hq.JSON.stringify(arrTable);
        var source =
        {
        	datatype: "json",
        	type:'post', 
            datafields: [
                { name: 'physDBName',type: 'string' },
                { name: 'tableName', type: 'string' }, 
                { name: 'isBigDataCol',type:'string'},
                { name: 'isBigData',value:'isBigDataCol',values: {source: yesornoAdapter.records, value: 'value', name: 'label' }},
                { name: 'synchOrder',type: 'string' }, 
                { name: 'synchCondition',type: 'string' },
                { name: 'synchedHandler',type: 'string' }, 
                { name: 'synchRecogCol',type: 'string' },
                { name: 'tableType',type: 'string' }, 
                {name: 'tableTypeShow',value:'tableType',values: {source: tableTypeAdapter.records, value: 'value', name: 'label' }},
                { name: 'isSynch', type: 'string' },
                { name: 'maxRow', type: 'string' },
                { name: 'pkList', type: 'string' },
                { name: 'remark', type: 'string' },
                { name: 'readOnly',type:'bool'}
            ], 
            root:'result', 
            pagenum: 0,                 
            pagesize: 50,
            data:{physDBName:physDBName,tableName:tableName,json:json}, 
            pager: function (pagenum, pagesize, oldpagenum) { 
            	alert(pagenum);              
            }, 
            url: url
        }; 
        var dataAdapter = new $.jqx.dataAdapter(source, {
            downloadComplete: function (data, status, xhr) { },
            loadComplete: function (data) {
            	orignalData = data;
            },
            loadError: function (xhr, status, error) {
            	alert(error);
            }
        });
        var cellsrenderer = function (row, columnfield, value, defaulthtml, columnproperties, rowdata) {
            if (value < 20) {
                return '<span style="margin: 4px; float: ' + columnproperties.cellsalign + '; color: #ff0000;">' + value + '</span>';
            }
            else {
                return '<span style="margin: 4px; float: ' + columnproperties.cellsalign + '; color: #008000;">' + value + '</span>';
            }
        }
        
        var getLocalization = function () {               
        	var localizationobj = {};               
        	localizationobj.pagergotopagestring = "跳转到:";
        	localizationobj.pagershowrowsstring = "每页显示:";
        	return localizationobj;            
        }
        
        var height = $(document).height();
        $("#jqxgrid").jqxGrid({
        	width:rightPanelWidth,  
        	height:$(window).height(),  
            source: dataAdapter,                 
            pageable: true,    
            sortable: true,
            editable: editable,
            showtoolbar: true,
            columnsresize: true,
            localization: getLocalization(),  
            pagesizeoptions:[50,100,200],  
            pagesize: 50,  
            rendertoolbar: function (toolbar) {
             toolbar.empty();
           	 var container = $("<div style='margin: 5px;'></div>");
           	 var physTip = $("<span>物理表名：</span>");
             var physDBName = $("<input type='text' id='physDBName'/>");
           	 var tableTip = $("<span>中文表名</span>");
             var tableName = $("<input type='text' id='tableName'/>");
             var searchBtn = $("<input type='button' id='queryBtn' value='查询'/>");
             var saveBtn = $("<input type='button' id='saveBtn' value='保存'/>");
             var keyValiBtn = $("<input type='button' id='keyValidate' value='主键校验'/>");
             var assignBtn = $("<input type='button' id='assignBtn' value='下发'/>");
             var structCheck = $("<input type='checkbox' id='structCheck'/>")
                toolbar.append(container);
                container.append(physTip);
                container.append(physDBName);
                container.append(tableTip);
                container.append(tableName);
                container.append(searchBtn);
                container.append(saveBtn);
                container.append(keyValiBtn);
                container.append(assignBtn);
                container.append(structCheck);
                container.append("不导出表结构");
                
                saveBtn.on("click",function(){
                	 //保存同步数据
        	    	var originaldata = dataAdapter.originaldata;
                 	var data = $('#jqxgrid').jqxGrid('getrows'); 
                 	var page = $('#jqxgrid').jqxGrid('getpaginginformation').pagenum;  
                 	var pagesize = $('#jqxgrid').jqxGrid('getpaginginformation').pagesize;
                 	var pagescount = $('#jqxgrid').jqxGrid('getpaginginformation').pagescount;
                 	var map = {};
                 	//for(var i= page*pagesize;i<(page+1)*pagesize;i++){ //current page 
                 	for(var i=0;i<data.length;i++){ // all	
                 		if(data[i].readOnly==true){
                 			map[originaldata[i].physDBName]='I';
                 			continue;
                 		}
                 		if(originaldata[i].isSynch=='0'&&data[i].isSynch=='1'){
                 			map[originaldata[i].physDBName]='I';
                 		}
                 		if(originaldata[i].isSynch=='1'&&data[i].isSynch=='1'){
                 			map[originaldata[i].physDBName]='M';
                 		}
                 		if(originaldata[i].isSynch=='1'&&data[i].isSynch=='0'){
                 			map[originaldata[i].physDBName]='D';
                 		}
                 		if(data[i].isSynch=='1'&&(data[i].synchRecogCol==null||data[i].synchRecogCol=='')){
                 			alert("选中的表同步标识列不能为空");
                 			return false;
                 		}
                 		if(data[i].isSynch=='1'&&(data[i].pkList==''||data[i].pkList==null)){
                 			alert("选中的表主键不能为空");  
                 			return false;
                 		}
                 	} 
                 	//判断新增 删除 修改
                 	$.ajax({
                          contentType: "application/json",   
                          url: "synch/assignConfig/saveSynchObjects.do",
                          data: {datas:Hq.JSON.stringify(data),map:Hq.JSON.stringify(map)},    
                          dataType: "json", 
                          success: function(data){ 
         					if(data=='success'){
         						$('#jqxgrid').jqxGrid({source:dataAdapter});
         					}
                          }
                      });//保存ajax逻辑结束
                });//保存按钮结束
                
                keyValiBtn.on("click",function(){
                	var datas = $('#jqxgrid').jqxGrid('getrows'); 
                	var pkerrors = new Array();
                	for(var i=0;i<datas.length;i++){ 
                		if(datas[i].isSynch==1&&(datas[i].pkList.length==0)){
                			pkerrors.push(datas[i].physDBName);
                		}
                	}
                	if(pkerrors.length>0){  
                		alert(pkerrors.join('\n')+'主键未设置');     
                	}else{
                		alert("主键都已设置! 无异常");  
                	}
        	     });//主键验证按钮结束
        	     
        	     searchBtn.on('click',function(){
    		        var physDBName = $('#physDBName').val();
    		        var tableName = $('#tableName').val();
    		        if($('#physDBName').jqxInput('placeHolder')==$('#physDBName').val()){
    		        	physDBName='';
    		        }
    		        if($('#tableName').jqxInput('placeHolder')==$('#tableName').val()){
    		        	tableName='';  
    		        }
    		        var json = Hq.JSON.stringify(arrTable);
    		        dataAdapter._source.data={ 
    	        		physDBName:physDBName,  
    	        		tableName:tableName,
    	        		json:json
    		        } 
     		        $("#jqxgrid").jqxGrid({
    		            source: dataAdapter 
    		        });
    	   		   });//查询按钮结束
    	   		   
        	     assignBtn.on('click',function(){
        	    	 //保存同步数据
        	    	var originaldata = dataAdapter.originaldata;
                 	var data = $('#jqxgrid').jqxGrid('getrows'); 
                 	var page = $('#jqxgrid').jqxGrid('getpaginginformation').pagenum;  
                 	var pagesize = $('#jqxgrid').jqxGrid('getpaginginformation').pagesize;
                 	var pagescount = $('#jqxgrid').jqxGrid('getpaginginformation').pagescount;
                 	var map = {};
                 	//for(var i= page*pagesize;i<(page+1)*pagesize;i++){ //current page 
                 	for(var i=0;i<data.length;i++){ // all	
                 		if(data[i].readOnly==true){
                 			map[originaldata[i].physDBName]='I';
                 			continue;
                 		}
                 		if(originaldata[i].isSynch=='0'&&data[i].isSynch=='1'){
                 			map[originaldata[i].physDBName]='I';
                 		}
                 		if(originaldata[i].isSynch=='1'&&data[i].isSynch=='1'){
                 			map[originaldata[i].physDBName]='M';
                 		}
                 		if(originaldata[i].isSynch=='1'&&data[i].isSynch=='0'){
                 			map[originaldata[i].physDBName]='D';
                 		}
                 		if(data[i].isSynch=='1'&&(data[i].synchRecogCol==null||data[i].synchRecogCol=='')){
                 			alert("选中的表同步标识列不能为空");
                 			return false;
                 		}
                 		if(data[i].isSynch=='1'&&(data[i].pkList==''||data[i].pkList==null)){
                 			alert("选中的表主键不能为空");  
                 			return false;
                 		}
                 	} 
                 	
                 	//判断新增 删除 修改
                 	$.ajax({
                          contentType: "application/json",   
                          url: "synch/assignConfig/saveSynchObjects.do",
                          data: {datas:Hq.JSON.stringify(data),map:Hq.JSON.stringify(map)},    
                          dataType: "json", 
                          success: function(data){ 
         					if(data=='success'){
         						$('#jqxgrid').jqxGrid({source:dataAdapter});
         	        	    	$('#assignWindow').jqxWindow('open');
         					}
                          }
                      });//保存ajax逻辑结束
                      
                 	//绑定下发确定按钮处理器
        	    	 $("#assignConfirm").on('click',function(){
        	         	var assignTree = $.fn.zTree.getZTreeObj("assignTree");
        	         	var provinceList = assignTree.getCheckedNodes();
        	         	var isSynchStruct = '';
        	         	if($("#structCheck")[0].checked){
        	         		isSynchStruct=0;
        	         	}else{
        	         		isSynchStruct=1; 
        	          	}
        	         	if(confirm("确定下发吗？")){
        	         		 showShade({
              					css : {
              						theme : false,
              						top : '50%'
              					},
              					message : "正在处理操作...<br/><img src='js/jquery-plugin/blockUI/loading.gif'>"
              				});
        	         		var json = Hq.JSON.stringify(arrTable);
        	         		$.ajax({
           	                 url: "synch/execute/assign.do", 
           	                 data: {isSynchStruct:isSynchStruct,json:json },     
           	                 dataType: "json", 
           	                 success: function(data){ 
           	 					if(data.success != undefined){
           	 						alert("下发成功！");
           	 					}else{
           	 						alert("下发失败！");
           	 					}
           	 					hideShade();
           	                 },
           	                 error:function(data){
           	                	alert("下发失败！");
           	                	hideShade();
           	                 }
           	             });
        	         	}
        	         });
        	     });//下发按钮结束
        	     
            },
            columns: [ 
              {text: '导入选择',  datafield: 'isSynch',columntype:'checkbox',cellsalign: 'center',width: 100,
            	  cellbeginedit:function(row,datafield,columntype){
            		  var record = $("#jqxgrid").jqxGrid('getrowdata', row);
            		  if(record.readOnly==true){
            			  alert("该表已经在上一步选中，不能取消！");
            			  return false;
            		  }
            		  else
            			  return true;
            	  }
              },
              {text: '同步顺序',  datafield: 'synchOrder',columntype: 'numberinput',cellsalign: 'center',
            	  align: 'center', width: 100,validation: function (cell, value) {  
            		  var rowid = cell.row;  
					  var  row =$("#jqxgrid").jqxGrid("getrowdatabyid",rowid); 
            		  if(row.tableType=='0'){
            			  if(value>50){
            				  return {result: false, message: "系统表同步顺序必须小于50" }; 
            			  }
            		  }else if(row.tableType=='1'){
            			  if(value>1000||value<50){
            				  return {result: false, message: "普通表同步顺序必须大于50小于1000"}; 
            			  }
            		  }else if(row.tableType=='2'){ 
            			  if(value<1000){ 
            				  return {result: false, message: "附件表同步顺序必须大于1000" };  
            			  }
            		  }
					  return true;     
            	  },
            	  createeditor: function (row, cellvalue, editor) {
            		  editor.jqxNumberInput({ decimalDigits: 0, digits: 4 });                          
            	  }
              },
              {text: '物理表名',  datafield: 'physDBName', align: 'center', cellsalign: 'left', width: 200,editable:false}, 
              {text: '中文表名',  datafield: 'tableName', align: 'center', cellsalign: 'left', width: 200,editable:false},   
              {text: '是否存在大文本',  datafield: 'isBigDataCol', displayfield: 'isBigData',columntype:'dropdownlist'
            	  ,align: 'left',cellsalign: 'left', width: 150,editable:false, 
            	  createeditor: function (row, column, editor) {              
            		  editor.jqxDropDownList({
            			  width:150,    
            			  autoDropDownHeight: true,  
            			  source: yesornoAdapter,   
            			  displayMember:"label", 
            			  valueMember:"value" 
            		  });                         
            	  }
              }, 
              {text: '单次同步最大行数', datafield: 'maxRow',align: 'center', width: 150},   
              {text: '表类型', datafield: 'tableType',displayfield: 'tableTypeShow',columntype:'dropdownlist',width: 150, 
            	  createeditor: function (row, column, editor) {              
            		  editor.jqxDropDownList({
            			  width:150,    
            			  autoDropDownHeight: true,  
            			  source: tableTypeAdapter,  
            			  placeHolder:'请选择：',
            			  displayMember:"label", 
            			  valueMember:"value" 
            		  });                          
            	  },
            	  cellvaluechanging: function (row, column, columntype, oldvalue, newvalue) {   
            		  // return the old value, if the new value is empty.  
            		  var dataRecord = $("#jqxgrid").jqxGrid('getrowdata', row); 
            		  if (newvalue.value == "0"){ //系统表 0-50
            			  if(parseInt(dataRecord.synchOrder)>50){
            				  dataRecord.synchOrder="1";
            				  $("#jqxgrid").jqxGrid("updaterow",row,dataRecord); 
            			  }  
            		  }else if(newvalue.value=="1"){ //普通录入表 50-1000
            			  if(parseInt(dataRecord.synchOrder)<50||parseInt(dataRecord.synchOrder)>1000){
            				  dataRecord.synchOrder="51";
            				  $("#jqxgrid").jqxGrid("updaterow",row,dataRecord);
            			  }
            		  }else{//>1000
            			  if(parseInt(dataRecord.synchOrder)<1000){
            				  dataRecord.synchOrder="1001";
            				  $("#jqxgrid").jqxGrid("updaterow",row,dataRecord); 
            			  }
            		  }                     
            	  }
              }, 
              {text: '同步标识列', datafield: 'synchRecogCol',align: 'center',columntype:'combobox', width: 150,
            	  createeditor: function (row, column, editor) {              
            		//同步标识符适配器    
            		var dataRecord = $("#jqxgrid").jqxGrid('getrowdata', row);
            		editor.jqxComboBox({
            			  width:150,
            			  autoDropDownHeight: true,  
            			  displayMember:"columnName",    
            			  valueMember:"columnName" 
            		});                          
            	  },
            	  initeditor:function(row, column, editor){
            		  var value = editor.val(); 
            		  var dataRecord = $("#jqxgrid").jqxGrid('getrowdata', row);
            		  var synchRecogColUrl = "synch/config/getSynchRecogCol.do";
             	      var synchRecogSource =
             		         {
             		         	 datatype: "json",
             		             datafields: [
             		                 { name: 'columnName',type: 'string'}
             		             ], 
             		             root:'result',  
             		             pagenum: 0,                 
             		             url: synchRecogColUrl,   
             		          	 type:'post',  
             		             data: {
             		            	 physDBName: dataRecord.physDBName
             		             }

             		   };  
             	       var synchRecogAdapter = new $.jqx.dataAdapter(synchRecogSource, {autoBind: false,
             	    		 loadComplete: function (data) {  
             	    			editor.jqxComboBox('val',value);  
             	    		 }
             	       });   
            		   editor.jqxComboBox({source:synchRecogAdapter});
            	  }
              }, 
              {text: '主键设置', datafield: 'pkList',columntype:'button',align: 'center', width: 100,  
            	cellsrenderer:function(row, column, value){
            		if(value==null||value==''){
            			return "设置";  
            		}else{
						return "已设";              			
            		}
              	},
              	buttonclick:function(row,owner){
              		var dataRecord = $("#jqxgrid").jqxGrid('getrowdata', row);
              		$('#keysettingwindow').jqxWindow('open'); 
              		 var url = "synch/config/getTableColumnList.do"; 
	   		    	 var columnListSource =
	   		         {
	   		         	 datatype: "json",
	   		             datafields: [
	   		                 { name: 'columnName',type: 'string' },
	   		              	 { name: 'isKey',type: 'bool' },  
	   		             ], 
	   		             root:'result', 
	   		             pagenum: 0,                 
	   		             url: url, 
	   		          	 type:'post',  
	   		             data: {
	   		            	 physDBName: dataRecord.physDBName
	   		             }
	
	   		         };
	   		    	var pks = dataRecord.pkList;  
	   		    	var dataAdapter = new $.jqx.dataAdapter(columnListSource, {                
	   		    		beforeLoadComplete: function (records) {                    // get data records.  
	   		    			for(var i=0;i<records.length;i++){
	   		    				for(var t=0;t<pks.length;t++){
	   		    					if(pks[t]==records[i].columnName){
	   		    						records[i].isKey=1;  
	   		    					}
	   		    				}
	   		    			}
	   		    			return records;    
	   		    		}            
	   		    	});           
	   		    	$("#keysettinggrid").jqxGrid({source: dataAdapter});
	   		    	$('#keysettingconfirm').unbind("click"); 
	   		    	$('#keysettingcancel').unbind("click"); 
					$('#keysettingconfirm').on("click",function(){
						var rowid = $("#jqxgrid").jqxGrid("getselectedrowindex");
						var  row =$("#jqxgrid").jqxGrid("getrowdatabyid",rowid);  
						
						//取得选中的字段
						var rows = $('#keysettinggrid').jqxGrid('getrows');
						var selectKeyColums = new Array(); 
						for(var i=0;i<rows.length;i++){
							if(rows[i].isKey){ 
								selectKeyColums.push(rows[i].columnName); 
							}
						} 
						row.pkList = selectKeyColums;    
						
						if(typeof(args.owner.updaterow)=='undefined'){
							$("#jqxgrid").jqxGrid("updaterow",rowid,row);
						}else{
							args.owner.updaterow(rowid,row); 
						}
						 
						if(selectKeyColums.length>0){
							owner.target.value="已设";   
						}else{
							owner.target.value="设置";        
						}
						$('#keysettingwindow').jqxWindow('close');   
					});
					$('#keysettingcancel').on("click",function(){
						$('#keysettingwindow').jqxWindow('close');    
					})
              	}
              },  
              {text: '导出条件', datafield: 'synchCondition',columntype:'button',align: 'center', width: 100,                     
            	 cellsrenderer:function(row, column, value){
            	 	return "导出条件设置";   
              	 }
              },       
              {text: '导入后执行方法', datafield: 'synchedHandler',align: 'center', width: 100}   
            ]
        });
    	$("#jqxgrid").on('cellbeginedit', function (event) {  
      		var args = event.args;   
      	});
    	
    	$('#keysettingwindow').jqxWindow({                    
  			showCollapseButton: true,
  			maxHeight: 400, 
  			maxWidth: 700, 
  			minHeight: 200,
  			minWidth: 200, 
  			height: 330,
  			width: 500,   
  			title:'主键设置',
  			isModal:true,
  			autoOpen:false, 
  			position: {x:500, y: 200},     
  			initContent: function () {                         
      			$('#keysettingwindow').jqxWindow('focus');
      			$('#keysettingconfirm').jqxButton({ width: '65px',disabled: !editable});                    
     			 $('#keysettingcancel').jqxButton({ width: '65px',disabled: !editable});  
      			$("#keysettinggrid").jqxGrid({
      				 height:250, 
      				 width:'99%', 
      				 editable: true,  
      				 columns: [  
						{text: '字段名',  datafield: 'columnName', width: 350 },    
						{text: '是否主键',datafield: 'isKey',columntype:'checkbox',cellsalign: 'center', align: 'center', width: 100},   
      				 ]
       			});
      		}               
  		});  
    	
    	
    	$('#synchConditionWin').jqxWindow({                    
  			showCollapseButton: true, 
  			maxHeight: 400, 
  			maxWidth: 700, 
  			minHeight: 200,
  			minWidth: 200, 
  			height: 330,   
  			width: 520, 
  			isModal:true, 
  			autoOpen:false,
  			title:'导出条件设置', 
  			position: {x:500, y: 200},    
  			initContent: function () {                         
      			$('#synchCondition').jqxWindow('focus');
      			//导出条件
      			 $('#confirm').jqxButton({ width: '65px',disabled: !editable});                    
      			 $('#cancel').jqxButton({ width: '65px',disabled: !editable});  
      			 
      		}               
  		}); 
    	
    	
    	$("#jqxgrid").on('cellclick', function (event) {  
      		var args = event.args; 
			if(args.datafield=='synchCondition'){
				$('#synchConditionWin').jqxWindow('open');
				$('#synchCondition').val(args.value);
				$('#confirm').unbind("click");
				$('#cancel').unbind("click");
				$('#confirm').on("click",function(){
					var rowid = args.owner.getselectedrowindex();
					var  row = args.owner.getrowdatabyid(rowid);
					row.synchCondition = $('#synchCondition').val(); 
					args.owner.updaterow(rowid,row);   
					$('#synchConditionWin').jqxWindow('close');   
				});
				$('#cancel').on("click",function(){ 
					$('#synchConditionWin').jqxWindow('close');   
				});
			} 
      	}); 	      
	     
  	}
        
  	/**
  	 * * 显示遮罩
  	 * 
  	 * @param {}
  	 *            o
  	 */
  	function showShade(o) {
  		o = o || {};
  		$.blockUI(o);
  	}
  	/**
  	 * 因此遮罩
  	 * 
  	 * @param {}
  	 *            o
  	 */
  	function hideShade() {
  		try {
  			$.unblockUI();
  		} catch (e) {
  		}

  	}
	</script>
  </head>
  
  <body id="synchconfigMainBody">   
  <div id='jqxWidget'>
        <div id="mainSplitter">
            <div class="splitter-panel">
                选择系统：<select id="appSelect" ><option>请选择</option></select>
                <div id="suitTree" class="ztree"></div>
            </div>
            <div class="splitter-panel">
                <div id="tableInfo"></div>
                <div id="jqxgrid" style="display:none"></div>
        </div>
    </div>
    
    <div id="keysettingwindow" style="display:none;">  
    	<div>     
    		<div id="keysettinggrid"></div> 
    		<div style="float: right; margin-top: 5px;">               
	    	   <input type="button" id="keysettingconfirm" value="确定" style="margin-right: 10px" />                       
	    	   <input type="button" id="keysettingcancel" value="取消" /> 
	    	</div>      
	    </div> 
    </div>
    <div id="synchConditionWin" style="display:none;"> 
    	 <div> 
			<div>
		      	<textarea id="synchCondition" cols=60 rows=15 style="overflow:auto">    
		      	</textarea>  
	     	</div>  
	     	<div style="float: right; margin-top: 5px;">                   
	    	   <input type="button" id="confirm" value="确定" style="margin-right: 10px" />                       
	    	   <input type="button" id="cancel" value="取消" />  
	    	</div>                 
    	  </div>  
    </div>
    <div id="assignWindow" style="display:none;">  
    	<div>     
    		<div id="assignTree" class="ztree"></div> 
    		<div style="float: right; margin-top: 5px;">               
	    	   <input type="button" id="assignConfirm" value="确定" style="margin-right: 10px" /> 
	    	</div>      
	    </div> 
    </div>
</body>
</html>
