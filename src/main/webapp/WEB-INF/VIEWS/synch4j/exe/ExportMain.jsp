<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE>  
<html>
  <head>
    <%String path = request.getContextPath(); %>
    <title>执行导出</title>
	
	<link rel="stylesheet" href="<%=path %>/css/zTree/zTreeStyle/zTreeStyle.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/scripts/synch4j/css/styles/jqx.base.css" type="text/css" /> 
	<link rel="stylesheet" href="<%=path %>/scripts/synch4j/css/styles/jqx.ui-smoothness.css" type="text/css" />
	<script type="text/javascript" src="<%=path %>/scripts/jquery.js"></script>
  	<script type="text/javascript" src="<%=path %>/scripts/json2.js"></script>   
  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/jqxGrid/jqx-all.js"></script>
  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/jquery-plugin/blockUI/jquery.blockUI.js"></script>
  	<script type="text/javascript" src="<%=path %>/scripts/jquery.ztree.all-3.5.min.js"></script>   

  	
  </head>
  <script>
  $(document).ready(function () {
		$.jqx.theme = 'ui-smoothness';   
		$("#execExport").jqxButton({width: '100'}); 
        $("#refresh").jqxButton({width: '150'}); 
        $("#delete").jqxButton({width: '150'}); 
        $("#assign").jqxButton({width: '100'});
        
        $('#assignWindow').jqxWindow({
        	showCollapseButton: true,
  			height: 530,
  			width: 600,   
  			title:'请选择下发的地区',
  			isModal:true,
  			autoOpen:false, 
  			resizable : false, 
  			position: {x:($(window).width()/2)-150, y: $(window).height()/10},     
  			initContent: function () { 
  				var setting = {
  						 async : {
  							 enable : true,
  							 otherParam:{},
  							 url :  "getProvinceTree.do"
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
     							 enable: true,
    							 chkboxType: { "Y": "", "N": "" }
  						}
  							
  					 };
  					 var assignTree = $.fn.zTree.init($("#assignTree"),setting,null);
      		}     
   	 });
        
        $('#reportWindow').jqxWindow({
        	showCollapseButton: true,
  			height: 530,
  			width: 600,   
  			title:'请选择上报的地区',
  			isModal:true,
  			autoOpen:false, 
  			position: {x:($(window).width()/2)-150, y: $(window).height()/10},     
  			initContent: function () { 
  				var setting = {
  						 async : {
  							 enable : true,
  							 otherParam:{},
  							 url :  "getReportProvinceTree.do"
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
     							 enable: true,
    							 chkboxType: { "Y": "", "N": "" }
  						},
  							
  					 };
  					 var reportTree = $.fn.zTree.init($("#reportTree"),setting,null);
      		}     
   	 });
        
        $("#execExport").on('click',function(){
        	var isSynchStruct = '';
        	if($('#isSynchStruct')[0].checked){
        		isSynchStruct=0;
        	}else{
        		isSynchStruct=1; 
         	}
        	exportFile("synch2/execute/export.do?"+tokenParam,{
        		isSynchStruct:isSynchStruct 
        	});   
        }); 
        
        $("#refresh").on('click',function(){
        	var value = $("#jqxTabs").val();
        	if(value==1){
        		loadExportDetailData();
        	}else{
        		loadExportMainData(); 
        	}
        });
        
        $("#delete").on('click',function(){
        	var rows = $('#exportMainGrid').jqxGrid('getrows');
			var selectKeyColums = new Array();
			var order = 0;
			for(var i=0;i<rows.length;i++){
				if(rows[i].check==1){ 
					if(rows[i].uid!=order){
						alert("请按顺序删除!");
						return false;
					}
					order=order+1;
					selectKeyColums.push(rows[i].logId); 
				}
			}
			 
        	$.ajax({
                url: "deleteExportMainLog.do", 
                data: {logId:selectKeyColums.join(',')},     
                dataType: "json", 
                success: function(data){ 
					if(data=='success'){
						alert("删除成功");
						loadExportMainData();  
					}
                }
            });
        }); 
        
        
        $("#assign").on('click',function(){
        	//$('#assignWindow').jqxWindow('open');
        	$.ajax({
                url: "assign.do", 
                type:"post",
                dataType: "json", 
                success: function(data){ 
					if(data.success != undefined){
						alert(data.success);
					}else{
						alert(data.fail);
					}
					loadExportMainData();  
					hideShade();
                }
            });
        }); 
        $("#assignConfirm").on('click',function(){
        	 showShade({
					css : {
						theme : false,
						top : '50%'
					},
					message : "正在处理操作...<br/><img src='static/app/synch2/js/jquery-plugin/blockUI/loading.gif'>"
				});
        	var assignTree = $.fn.zTree.getZTreeObj("assignTree");
        	var provinceList = assignTree.getCheckedNodes();
        	
        	/*var isSynchStruct = '';
        	if($('#isSynchStruct')[0].checked){
        		isSynchStruct=0;
        	}else{
        		isSynchStruct=1; 
         	}*/
        	var provinceCode = new Array();
        	$.each(provinceList,function(index,item){
        		provinceCode.push(item.CODE);
        	});
        	var provinceStr = Hq.JSON.stringify(provinceCode);
        	$.ajax({
                url: "synch2/execute/assign.do", 
                type:"post",
                data: {
                	taskTypeId:$("#taskTypeSelect").val(),
                	provinceList : provinceStr
                },   //下发必须同步表结构  
                dataType: "json", 
                success: function(data){ 
					if(data.success != undefined){
						alert(data.success);
					}else{
						alert(data.fail);
					}
					loadExportMainData();  
					hideShade();
                }
            });
        });
        /*$("#report").on('click',function(){
        	$('#reportWindow').jqxWindow('open');
       }); )*/
        $("#reportConfirm").on('click',function(){
       	 showShade({
					css : {
						theme : false,
						top : '50%'
					},
					message : "正在处理操作...<br/><img src='static/app/synch2/js/jquery-plugin/blockUI/loading.gif'>"
				});
       	var reportTree = $.fn.zTree.getZTreeObj("reportTree");
       	var provinceList = reportTree.getCheckedNodes();
       	var provinceCode = new Array();
       	$.each(provinceList,function(index,item){
       		provinceCode.push(item.CODE);
       	});
       	var provinceStr = Hq.JSON.stringify(provinceCode);
        $.ajax({
            url: "synch2/simulate/reportSimulate.do", 
            type:"post",
            data: {
            	taskTypeId:$("#taskTypeSelect").val(),
            	provinceList : provinceStr
            },   
            dataType: "json", 
            success: function(data){ 
				if(data.success != undefined){
					alert(data.success);
				}else{
					alert(data.fail);
				}
				loadExportMainData();  
				hideShade();
            }
        });
       });
       
        
		var height = $(document).height()-60;  
	    
		$('#jqxTabs').jqxTabs({ 
		  width: '100%',  
		  height: height,   
		  position: 'top', 
		  animationType: 'fade' 
		});
		
		function loadExportDetailData(){
			var rowid = $("#exportMainGrid").jqxGrid("getselectedrowindex");
			var  row =$("#exportMainGrid").jqxGrid("getrowdatabyid",rowid);
			var exportDetailSource = 
	        {
	        	datatype: "json",
	            datafields: [
					{ name: 'logId',type: 'string' },
	                { name: 'expFileName',type: 'string' },
	                { name: 'expPhysDBName', type: 'string' },
	                { name: 'expDatas',type:'string'},
	                { name: 'lastDate',type: 'string' },
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
	            	logId:row.logId
	            }, 
	            url: "synch2/execute/getExportDetailLogMain.do?"+tokenParam
	        }; 
			
	        var exportDetailAdapter = new $.jqx.dataAdapter(exportDetailSource);
	        
	        $("#exportDetailGrid").jqxGrid({
	        	source:exportDetailAdapter 
	        });
		}
		
		function loadExportMainData(){
			$("#exportMainGrid").jqxGrid({
				 source:exportAdapter
			});
		} 
		
		
		$('#jqxTabs').on('selected', function (event) {  
			
			var title = $('#jqxTabs').jqxTabs('getTitleAt', event.args.item); 
			//$('#jqxTabs').jqxTabs('setTitleAt', i, text);  
			var value = $("#jqxTabs").val();
			if(value==1){
				loadExportDetailData();
			}else{
				loadExportMainData();
			} 
		});
		
		var url = "getExportMainLogList.do"; 
        var exportSource =
        {
        	datatype: "json",
            datafields: [
				{ name: 'logId',type: 'string' },
                { name: 'userId',type: 'string' },
                { name: 'ipAddress', type: 'string' },
                { name: 'fileName',type:'string'},
                { name: 'startDate',type: 'string' },
                { name: 'endDate',type: 'string' }, 
                { name: 'usedDate',type: 'string' }, 
                { name: 'districtId',type: 'string' }, 
                { name: 'remark',type: 'string' },
                { name: 'direction',type: 'string' },
                { name: 'check',type: 'string' }
            ], 
            root:'result',  
            pagenum: 0,                 
            pagesize: 50,                 
            pager: function (pagenum, pagesize, oldpagenum) { 
            	alert(pagenum);              
            }, 
            url: url
        }; 
        var exportAdapter = new $.jqx.dataAdapter(exportSource, {
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
        
		$("#exportMainGrid").jqxGrid({
			 source:exportAdapter,
			 height:$('#jqxTabs').height()-35,    
			 width:$('#jqxTabs').width()-2, 
			 editable: true, 
			 pageable: true,
			 pagesizeoptions:[50,100,200],  
	         pagesize: 50,
	         localization: getLocalization(),  
			 columns: [   
				{text: '选择',  datafield: 'check',editable:true,columntype:'checkbox',align: 'center',width: 150 },    
				{ text: '序号', exportable: false, editable:false,columntype: 'number',align: 'center',cellsrenderer: numberrenderer}, 
				{text: '开始时间',datafield: 'startDate',editable:false,cellsalign: 'center', align: 'center', width: 350 }, 
				{text: '结束时间',datafield: 'endDate',editable:false,cellsalign: 'center', align: 'center', width: 350 }, 
				{text: '下发省份编码',datafield: 'districtId',editable:false,cellsalign: 'center', align: 'center', width: 350 }, 
				{text: '操作员',datafield: 'userId',editable:false,columngroup: 'operatorinfo',cellsalign: 'center', align: 'center', width: 250 },   
				{text: 'ip地址',datafield: 'ipAddress',editable:false,columngroup: 'operatorinfo',cellsalign: 'center', align: 'center', width: 250 },
				{text: '备注',datafield: 'remark',cellsalign: 'left', align: 'center', width: 350}
			 ],  
			 columngroups:[ 
			    { text: '操作员信息', align: 'center', name: 'operatorinfo'}  
			 ]
		});
	    
		$("#exportDetailGrid").jqxGrid({
			 height:$('#jqxTabs').height()-35,     
			 width:$('#jqxTabs').width()-2,        
			 columns: [  
				{ text: '序号', exportable: false, editable:false,columntype: 'number',align: 'center',cellsrenderer: numberrenderer}, 
				{text: '文件名',datafield: 'expFileName',cellsalign: 'center', align: 'center',cellsalign: 'left', width: 350 }, 
				{text: '物理表',datafield: 'expPhysDBName',cellsalign: 'center', align: 'center',cellsalign: 'left', width: 250 },  
				{text: '导出记录',datafield: 'expDatas',cellsalign: 'center', align: 'center', width: 250 },
				{text: '最后一条记录时间',datafield: 'lastDate',cellsalign: 'center', align: 'center',cellsalign: 'left', width: 150}
			 ] 
		}); 
		
		$("#exportMainGrid").on('rowselect', function (event) {                
			  var rowid = event.args.rowindex;
			  var  row = args.owner.getrowdatabyid(rowid); 
			  //var detailTitle = $('#jqxTabs').jqxTabs('getTitleAt', 1);   
			  $('#jqxTabs').jqxTabs('setTitleAt', 1, "导出日志明细"+row.startDate);  
		}); 
		
		var name,form; 
		function exportFile(url, data) {
			var name,form; 
			form = createElement('form', {
				method: 'post', 
				action: url
			}, { 
				display: 'none'
			}, document.body);
			for (name in data) {
				createElement('input', {
					type: 'hidden', 
					name: name,
					value: data[name]
				}, null, form);
			}
			form.submit(); 
		};
		
		function createElement(tag, attribs, styles, parent, nopad) {
			var el = document.createElement(tag); 
			if (attribs) {
				extend(el, attribs);
			}
			if (nopad) {
				css(el, {padding: 0, border: 'none', margin: 0});
			}
			if (styles) {
				css(el, styles);
			}
			if (parent) {
				parent.appendChild(el);
			}
			return el;
		}

		function extend(a, b) {
			var n;
			if (!a) {
				a = {};
			}
			for (n in b) {
				a[n] = b[n];
			}
			return a;
		} 
		
		function css(el, styles) {
			if (styles && styles.opacity !== 'undefined') {
				styles.filter = 'alpha(opacity=' + (styles.opacity * 100) + ')';
			}
			extend(el.style, styles);
		}
		
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
  <body>
  	 <div style="margin-left:250px;">  
  	 	<div style="float:left;">
	  		<input type="button" id="execExport" value="执行导出" style="display:none">  
	  		<input type="button" id="refresh" value="刷新">  
	  		<input type="button" id="delete" value="删除日志"> 
	  		<input type="button" id="assign" value="下发"/>
	  		<%--<input type="button" id="report" value="上报仿真"/>
	  		<select id="taskTypeSelect" <%=taskHideStyle%> ><option value="#">全部</option></select>
  		--%></div> 
  		<%--<div style="float:left;margin-left:150px;display:none">
  			<input type="checkbox" id="isSynchStruct" checked>不导出数据结构</input> 
  		</div>
  	--%></div>
  	<div id="assignWindow" style="display:none;">  
    	<div>     
    		<div id="assignTree" class="ztree"></div> 
    		<div style="float: right; margin-top: 5px;">               
	    	   <input type="button" id="assignConfirm" value="确定" style="margin-right: 10px" />                       
	    	   <%--<input type="button" id="assignCancel" value="取消" /> 
	    	--%></div>      
	    </div> 
    </div> 
    <div id="reportWindow" style="display:none;">  
    	<div>     
    		<div id="reportTree" class="ztree"></div> 
    		<div style="float: right; margin-top: 5px;">               
	    	   <input type="button" id="reportConfirm" value="确定" style="margin-right: 10px" />                       
	    	   <input type="button" id="reportCancel" value="取消" /> 
	    	</div>      
	    </div> 
    </div> 
     <div id='jqxTabs' style="top:10px;position:relative;">             
	      <ul>                 
	      	<li>导出主日志</li>    
	      	<li>导出明细日志</li>                
	      </ul> 
	      <div>
	      		<div id="exportMainGrid">
	      		</div>
	      </div>
	      <div> 
	      		<div id="exportDetailGrid"> 
	      		</div>
	      </div> 
      </div>
  </body>
</html>
