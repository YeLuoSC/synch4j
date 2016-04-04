<%@ page contentType="text/html;charset=UTF-8"%>
<html>
  <head>
    <%String path = request.getContextPath(); %>
  	<meta charset="UTF-8"/>
    <title>数据同步设置</title>
	<link rel="stylesheet" href="<%=path %>/scripts/synch4j/css/styles/jqx.base.css" type="text/css" /> 
	<link rel="stylesheet" href="<%=path %>/scripts/synch4j/css/styles/jqx.ui-smoothness.css" type="text/css" />
	<script type="text/javascript" src="<%=path %>/scripts/jquery.js"></script>
  	<script type="text/javascript" src="<%=path %>/scripts/json2.js"></script>   
  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/jqxGrid/jqx-all.js"></script>
  	    
  	<script type="text/javascript"> 
  	$(document).ready(function () {  
  		var orignalData = null;
  		var editable = true;
  		
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
  		var url = "getSynchConfigList.do"; 
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
                { name: 'filterCol', type: 'string' },
                { name: 'remark', type: 'string' }
            ], 
            root:'result', 
            pagenum: 0,                 
            pagesize: 50,
            data:{physDBName:physDBName,tableName:tableName}, 
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
        	width:'99%',  
        	height:height-60,  
            source: dataAdapter,                 
            pageable: true,    
            sortable: true,
            editable: editable, 
            columnsresize: true,
            localization: getLocalization(),  
            pagesizeoptions:[50,100,200],  
            pagesize: 50,  
            columns: [ 
              {text: '导入选择',  datafield: 'isSynch',columntype:'checkbox',cellsalign: 'center',width: 100},    
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
            		  var synchRecogColUrl = "getSynchRecogCol.do";
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
              		 var url = "getTableColumnList.do"; 
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
              {text: '过滤列', datafield: 'filterCol',align: 'center', width: 100},
              {text: '备注信息', datafield: 'remark',align: 'center', width: 100} 
              //{text: '导入后执行方法', datafield: 'synchedHandler',align: 'center', width: 100}   
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
    	$("#saveBtn").jqxButton({
    		width: '100',
    		height:'23',
    		disabled: !editable
    	}); 
        $("#keyValidate").jqxButton({width: '150', height:'23'});  
        $("#oneButtonToSet").jqxButton({width: '150', height:'23'});  
        $("#queryBtn").jqxButton({width: '100',height:'23'});      
        $("#saveBtn").on("click",function(){
        	var originaldata = dataAdapter.originaldata;
        	var data = $('#jqxgrid').jqxGrid('getrows'); 
        	var page = $('#jqxgrid').jqxGrid('getpaginginformation').pagenum;  
        	var pagesize = $('#jqxgrid').jqxGrid('getpaginginformation').pagesize;
        	var pagescount = $('#jqxgrid').jqxGrid('getpaginginformation').pagescount;
        	var map = {};
        	//for(var i= page*pagesize;i<(page+1)*pagesize;i++){ //current page 
        	for(var i=0;i<data.length;i++){ // all	
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
                 url: "saveSynchPO.do",
                 data: {datas:Hq.JSON.stringify(data),map:Hq.JSON.stringify(map)},    
                 dataType: "json", 
                 success: function(data){ 
                	 alert("保存成功");
					if(data=='success'){
						alert("保存成功");
						$('#jqxgrid').jqxGrid({source:dataAdapter});
					}
                 }
             });
        });
        
        $("#keyValidate").on("click",function(){
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
	      });
        
        $("#oneButtonToSet").on("click",function(){
        	$.ajax({
                contentType: "application/json",   
                url: "oneButtonToSet.do",  
                dataType: "json", 
                success: function(data){ 
                	location.reload();
                }
            });
	      })
	      
	      $('#queryBtn').on('click',function(){
		        var physDBName = $('#physDBName').val();
		        var tableName = $('#tableName').val();
		        if($('#physDBName').jqxInput('placeHolder')==$('#physDBName').val()){
		        	physDBName='';
		        }
		        if($('#tableName').jqxInput('placeHolder')==$('#tableName').val()){
		        	tableName='';  
		        } 
		        dataAdapter._source.data={ 
	        		physDBName:physDBName,  
	        		tableName:tableName
		        } 
 		        $("#jqxgrid").jqxGrid({
		            source: dataAdapter 
		        });
	      });
	      $("#physDBName").jqxInput({placeHolder: "物理表名", height: 20, width: 200, minLength: 1});
          $("#tableName").jqxInput({placeHolder: "中文表名", height: 20, width: 200, minLength: 1,});  
    });
	</script>
  </head>
  
  <body id="synchconfigMainBody">   
  	<div align="left">    
  		<div style="width:600px;padding-left:100px;float:left;"> 
	  		<input type="text" id="physDBName"/>
	  		<input type="text" id="tableName"/>
	  		<input type="button" id="queryBtn" value="查询"/>    
  		</div>
  		<input type="button" id="saveBtn" value="保存"/>  
  		<input type="button" id="keyValidate" value="主键校验"/> 
  		<input type="button" id="oneButtonToSet" value="一键设置主键及过滤列"/>(需要先勾好表格，设置后刷新页面，检查是否有错误的备注信息提示) 
  	</div>   
  	 
  	<div id='jqxWidget' align="center" style="margin-top:10px;font-size: 13px; font-family: Verdana; float: center;width:100%;">
        <div id="jqxgrid" align="center" style="width:100%;height:100%">     
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
  </body>
</html>
