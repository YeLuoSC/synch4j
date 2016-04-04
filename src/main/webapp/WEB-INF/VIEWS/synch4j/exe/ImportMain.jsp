<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE>
<html>
	<head>
		<%String path = request.getContextPath(); %>
		<title>执行导入</title>
		<link rel="stylesheet" href="<%=path %>/css/zTree/zTreeStyle/zTreeStyle.css" type="text/css"></link>
		<link rel="stylesheet" href="<%=path %>/scripts/synch4j/css/styles/jqx.base.css" type="text/css" /> 
		<link rel="stylesheet" href="<%=path %>/scripts/synch4j/css/styles/jqx.ui-smoothness.css" type="text/css" />
		<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/jquery-1.10.2.min.js"></script>
	  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/json2.js"></script> 
	  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/ajaxfileupload.js"></script>  
	  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/jqxGrid/jqx-all.js"></script>
	  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/jquery-plugin/blockUI/jquery.blockUI.js"></script>
	  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/zTree/jquery.ztree.all-3.5.min.js"></script>   
	</head>
	<script>
  $(document).ready(function () {
		$.jqx.theme = 'ui-smoothness';    
		$("#execExport").jqxButton({width: '100'}); 
        $("#refresh").jqxButton({width: '150'}); 
        $("#delete").jqxButton({width: '150'}); 
        $("#reset").jqxButton({width: '150'}); 
        
		var height = $(document).height()-60; 
	    
		$('#jqxTabs').jqxTabs({ 
		  width: '100%',  
		  height: height,   
		  position: 'top', 
		  animationType: 'fade' 
		});
		
		$('#jqxTabs').on('selected', function (event) {  
			
			var title = $('#jqxTabs').jqxTabs('getTitleAt', event.args.item); 
			//$('#jqxTabs').jqxTabs('setTitleAt', i, text);  
			var value = $("#jqxTabs").val();
			if(value==1){
				loadImportDetailData();
			}else{
				loadImportMainData();
			} 
		});
		
		 $("#delete").on('click',function(){
	        	var rows = $('#importMainGrid').jqxGrid('getrows');
				var selectKeyColums = new Array(); 
				for(var i=0;i<rows.length;i++){
					if(rows[i].check==1){ 
						selectKeyColums.push(rows[i].logId); 
					}
				} 
	        	$.ajax({
	                url: "deleteImportMainLog.do", 
	                data: {logId:selectKeyColums.join(',')},     
	                dataType: "json", 
	                success: function(data){ 
						if(data=='success'){
							alert("删除成功");
							loadImportMainData();  
						}
	                }
	            });
	        }); 
		 
		 $("#reset").on('click',function(){
			 if(confirm("请谨慎该操作！重置前，请确认当前服务后台没有正在导入的任务！该功能用于在上次导入时，服务由于某些原因停止了，未完成导入任务时使用！该操作完成后，会将上次未导入完成的任务，重新进行导入！")){
				 $.ajax({
		                url: "reset.do", 
		                success: function(data){ 
							alert("重置成功");
		                }
		            });
			 }
	        	
	        }); 
		
		function loadImportDetailData(){
			var rowid = $("#importMainGrid").jqxGrid("getselectedrowindex");
			var  row =$("#importMainGrid").jqxGrid("getrowdatabyid",rowid);
			var logId = '';
			if(rowid!=-1){
				logId = row.logId;
			}
			var importDetailSource = 
	        {
	        	datatype: "json",
	            datafields: [
					{ name: 'logId',type: 'string' },
	                { name: 'expFileName',type: 'string' },
	                { name: 'expPhysDBName', type: 'string' },
	                { name: 'expDatas',type:'string'},
	                { name: 'impUsedDate',type:'string'}, 
	                { name: 'insertDatas',type:'string'},
	                { name: 'updateDatas',type:'string'},
	                { name: 'failDatas',type:'string'},
	                { name: 'remark',type: 'string' }
	            ], 
	            root:'result',  
	            pagenum: 0,                 
	            pagesize: 50,                 
	            pager: function (pagenum, pagesize, oldpagenum) { 
	            	alert(pagenum);              
	            }, 
	            type:'post',    
	            data:{
	            	logId:logId
	            }, 
	            url: "getImportDetailLogList.do"
	        }; 
			
	        var importDetailAdapter = new $.jqx.dataAdapter(importDetailSource);
	        
	        $("#importDetailGrid").jqxGrid({
	        	source:importDetailAdapter 
	        });
		}
		
		function loadImportMainData(){
			$("#importMainGrid").jqxGrid({
				 source:importAdapter 
			}); 
		}
		
		
		var url = "getImportMainLogList.do";  
        var importSource =
        {
        	datatype: "json",
            datafields: [
				{ name: 'logId',type: 'string' },
                { name: 'userID',type: 'string' },
                { name: 'ipAddress', type: 'string' },
                { name: 'fileName',type:'string'},
                { name: 'startDate',type: 'string' },
                { name: 'endDate',type: 'string' }, 
                { name: 'usedDate',type: 'string' },  
                { name: 'remark',type: 'string' },
                { name: 'direction',type: 'string' },  
                { name: 'check',type: 'string' },
                { name: 'synStatus',type: 'string' }
            ], 
            root:'result',  
            pagenum: 0,                 
            pagesize: 50,                 
            pager: function (pagenum, pagesize, oldpagenum) { 
            	alert(pagenum);               
            }, 
            url: url
        }; 
        var importAdapter = new $.jqx.dataAdapter(importSource, {
            downloadComplete: function (data, status, xhr) {
            	for(var i=0;i<data.result.length;i++){
            		data.result[i].check=0;   
            	}
            	return data;
            },
            loadComplete: function (data) {
            	
            },
            loadError: function (xhr, status, error) {
            	alert(error);
            }                 
        }); 
		
        var numberrenderer = function (row, column, value) {
            return '<div style="text-align: center; margin-top: 5px;">' + (1 + value) + '</div>';
        } 
        
        var getLocalization = function () {               
        	var localizationobj = {};               
        	localizationobj.pagergotopagestring = "跳转到:";
        	localizationobj.pagershowrowsstring = "每页显示:";
        	return localizationobj;            
        }
		$("#importMainGrid").jqxGrid({
			 height:$('#jqxTabs').height()-35,    
			 width:$('#jqxTabs').width()-2,           
			 source:importAdapter, 
			 pageable: true,    
	         pagesizeoptions:[50,100,200], 
	         pagesize:50,
	         columnsresize: true, 
			 editable: true,   
			 localization: getLocalization(),   
			 columns: [  
				{text: '选择',  datafield: 'check',columntype:'checkbox',align: 'center',width: 80 },     
				{ text: '序号', exportable: false, editable:false,columntype: 'number',align: 'center',cellsrenderer: numberrenderer,width: 80},
				{text: '操作员',datafield: 'userID',editable:false,columngroup: 'operatorinfo',cellsalign: 'center', align: 'center', width: 150 },   
				{text: 'ip地址',datafield: 'ipAddress',editable:false,columngroup: 'operatorinfo',cellsalign: 'center', align: 'center', width: 150 },
				{ text: '导入状态',exportable: false,datafield: 'synStatus',editable:false,align: 'center',width: 150,cellsrenderer:function(row, column, value){
					if(value=='1'){
						return '<div style="text-align: center; margin-top: 5px;">正在导入中...</div>'; 
					}
					if(value=='2'){
						return '<div style="text-align: center; margin-top: 5px;">导入完成</div>'; 
					}
					if(value=='3'){
						return '<div style="text-align: center; margin-top: 5px;">导入失败</div>'; 
					}
					
				}}, 
				{ text: '导入文件名',exportable: false,datafield: 'fileName',editable:false,align: 'center',width: 280},    
				{text: '开始时间',datafield: 'startDate',editable:false,cellsalign: 'center', align: 'center', width: 200 }, 
				{text: '结束时间',datafield: 'endDate',editable:false,cellsalign: 'center', align: 'center', width: 200 },  
				{text: '导入使用时间',datafield: 'usedDate',editable:false,cellsalign: 'center', align: 'center', width: 200 },
				{text: '备注',datafield: 'remark',cellsalign: 'left', align: 'center', width: 350}   
				
			 ],  
			 columngroups:[ 
			    { text: '操作员信息', align: 'center', name: 'operatorinfo'}  
			 ]
		});
	    
		$("#importMainGrid").on('rowselect', function (event) {                
			  var rowid = event.args.rowindex;
			  var  row = args.owner.getrowdatabyid(rowid); 
			  //var detailTitle = $('#jqxTabs').jqxTabs('getTitleAt', 1);   
			  $('#jqxTabs').jqxTabs('setTitleAt', 1, row.fileName);    
		}); 
		
		$("#importDetailGrid").jqxGrid({
			 height:$('#jqxTabs').height()-35,     
			 width:$('#jqxTabs').width()-2,        
			 columns: [  
				{ text: '序号', exportable: false, editable:false,columntype: 'number',align: 'center',cellsrenderer: numberrenderer}, 
				{text: '文件名',datafield: 'expFileName',columngroup: 'exportinfo',cellsalign: 'center', align: 'center',cellsalign: 'left', width: 350 }, 
				{text: '导出记录',datafield: 'expDatas',columngroup: 'exportinfo',cellsalign: 'center', align: 'center',cellsalign: 'left', width: 250 },
				{text: '物理表',datafield: 'expPhysDBName',columngroup: 'importinfo',cellsalign: 'center', align: 'center',cellsalign: 'left', width: 250 }, 
				{text: '导入使用时间',datafield: 'impUsedDate',columngroup: 'importinfo',cellsalign: 'center', align: 'center',cellsalign: 'left', width: 250 },
				{text: '插入数据条数',datafield: 'insertDatas',columngroup: 'importinfo',cellsalign: 'center', align: 'center', width: 250 },
				{text: '更新数据条数',datafield: 'updateDatas',columngroup: 'importinfo',cellsalign: 'center', align: 'center', width: 250 },
				{text: '失败数据条数',datafield: 'failDatas',columngroup: 'importinfo',cellsalign: 'center', align: 'center',cellsalign: 'left', width: 250 },
				{text: '备注',datafield: 'remark',cellsalign: 'center', align: 'center', width: 350} 
			 ], 
			 columngroups:[ 
			    { text: '导出信息', align: 'center', name: 'exportinfo'}, 
			    { text: '导入信息', align: 'center', name: 'importinfo'}
			 ] 
		});  
		
		//刷新 
		$('#refresh').on('click',function(){
			var value = $("#jqxTabs").val();
			if(value==1){
				loadImportDetailData();  
			}else{
				$("#importMainGrid").jqxGrid('clearselection'); 
				$('#jqxTabs').jqxTabs('setTitleAt',1,"导入明细日志");   
				loadImportMainData();
			} 
		});
		$('#importFileDiv').jqxWindow({                    
  			showCollapseButton: true,
  			height: 180,    
  			width: 280,    
  			autoOpen:false,
  			resizable:false, 
  			isModal:true,  
  			title:'导入文件',  
  			position: {x:500, y: 200},  
  			okButton: $('#okbtn'), 
  			cancelButton: $('#cancelbtn'), 
  			initContent: function () {                         
      			$('#importFileDiv').jqxWindow('focus');
      			$('#okbtn').jqxButton({ width: '65px' });                    
    			 $('#cancelbtn').jqxButton({ width: '65px' }); 
    			 
      		}               
  		}); 
		
		$('#execExport').on('click',function(){ 
			$('#importFileDiv').jqxWindow('open');
			
		});
		
		$('#okbtn').on('click',function(){ 
	       	 showShade({
					css : {
						theme : false,
						top : '50%'
					},
					message : "正在导入中...<br/><img src='<%=path %>/scripts/synch4j/js/jquery-plugin/blockUI/loading.gif'>"
				});
			$.ajaxFileUpload({
				 fileElementId:'fileName', 
				 secureuri: false, //一般设置为false
				 url:'import.do',
				 dataType:'json', 
				 type:'post',  
				 uploadCallback : function(e){
				 	alert("导入成功！");
				 	hideShade();
				 },
				 success: function (data, status)            //相当于java中try语句块的用法
                {   hideShade();  
					 if(data.success){ 
						 alert("导入成功");    
					 }else{
						 alert(data.erroInfo);  
					 }
                 },
                 error : function(){
                 	hideShade(); 
					alert("导入失败");  
                 }
			 });  
		});
  });
  
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
	 * 隐藏遮罩
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
	<body>
		<div style="margin-left: 250px;">
			<input type="button" id="execExport" value="执行导入">
			<input type="button" id="refresh" value="刷新">
			<input type="button" id="delete" value="删除日志">
			<input type="button" id="reset" value="重置导入">
		</div>
		<div id='jqxTabs' style="top: 10px; position: relative;">
			<ul>
				<li>
					导入主日志
				</li>
				<li>
					导入明细日志
				</li>
			</ul>
			<div>
				<div id="importMainGrid">
				</div>
			</div>
			<div>
				<div id="importDetailGrid">
				</div>
			</div>
		</div>
		<div id="importFileDiv" style="display: none;">
			<div>
				<div>
					<form id="import" method="post"
						action="import.do"
						enctype="multipart/form-data">
						<div algin="center" style="margin-top: 30px;">
							文件：
							<input type="file" id="fileName" name="fileName">
						</div>
					</form>
				</div>
				<div align="center" style="margin-top: 35px;">
					<input type="button" id="okbtn" value="确定">
					<input type="button" id="cancelbtn" value="取消">
				</div>
			</div>
		</div>
	</body>
</html>
