<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html> 
<html>
  <head>
    <%String path = request.getContextPath(); %>
    <title>远程脚本配置</title>
    <script type="text/javascript" src="<%=path %>/scripts/synch4j/js/jquery-1.10.2.min.js"></script>
	<link rel="stylesheet" href="<%=path %>/scripts/synch4j/css/styles/jqx.base.css" type="text/css" /> 
	<link rel="stylesheet" href="<%=path %>/scripts/synch4j/css/styles/jqx.ui-smoothness.css" type="text/css" />      
  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/json2.js"></script>   
  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/jqxGrid/jqx-all.js"></script>
  	    
  	<script type="text/javascript"> 
  	var orignalData;
  	var dataAdapter;
  	$(function(){
  		$("#programWindow").jqxWindow({                    
  			showCollapseButton: true, 
  			maxHeight: 400, 
  			maxWidth: 700, 
  			minHeight: 200,
  			minWidth: 200, 
  			height: 330,   
  			width: 520, 
  			isModal:true, 
  			autoOpen:false,
  			title:'运行代码', 
  			position: {x:500, y: 200},    
  			initContent: function () {                         
      			$("#programWindow").jqxWindow('focus');
      			
      			 $("#confirm").jqxButton({ width: '65px'});                    
      			 $("#cancel").jqxButton({ width: '65px'}); 
      			$("#confirm").on("click",function(){
    				var rowid = args.owner.getselectedrowindex();
    				var  row = args.owner.getrowdatabyid(rowid);
    				row.programCode = $("#programCodeArea").val();
    				$("#programWindow").jqxWindow('close');
    			});
    			 $("#cancel").on("click",function(){
    				$("#programWindow").jqxWindow('close');
    			 });
      		}               
  		});
  		
  		var yesornolist = [{label:'是',value:1},{label:'否',value:0}];     
        var yesornoSource =   {                 
        		datatype: "array",                 
        		datafields: [ { name: 'label', type: 'string' },{ name: 'value', type: 'string' } ],                 
        		localdata: yesornolist           
        };           
        var yesornoAdapter = new $.jqx.dataAdapter(yesornoSource, {autoBind: true});
  		
  		var source =
        {
        	datatype: "json",
        	type:'post', 
            datafields: [
                { name: 'no',type: 'string'},
                { name: 'guid',type: 'string'},
                { name: 'name',type: 'string' },
                { name: 'describe', type: 'string' },
                { name: 'available', type: 'string' },
                { name: 'availableList',value:'available',values: {source: yesornoAdapter.records, value: 'value', name: 'label' }},
                { name: 'programCode',type:'string'},
                { name: 'isCheck',type:'string'}
            ],
            url: "procedureConfig/getRemoteProcedure.do"
        }; 
  		 dataAdapter = new $.jqx.dataAdapter(source, {
             downloadComplete: function (data, status, xhr) {
            	 var count = 1;
            	 $.each(data,function(index,item){
            		 item.no = count++;
            		 item.isCheck = false;
            	 });
             },
             loadComplete: function (data) {
            	
            	 orignalData = data;
             },
             loadError: function (xhr, status, error) {
             	alert(error);
             }
         });
  		/*$('#addWindow').jqxWindow({
        	showCollapseButton: true,
  			height: 530,
  			width: 600,   
  			title:'新增',
  			isModal:true,
  			autoOpen:false, 
  			position: {x:($(window).width()/2)-150, y: $(window).height()/10},     
  			initContent: function () { 
  				var contentPanel = $("#addContent");
  				
      		}     
   	    });*/
  		$("#jqxgrid").jqxGrid({
        	width:$(window).width(),  
        	height:$(window).height(),  
            source: dataAdapter,                 
            pageable: false,    
            sortable: false,
            editable: true,
            showtoolbar: true,
            columnsresize: true,
            rendertoolbar: function (toolbar) {
            	toolbar.empty();
              	var container = $("<div style='margin: 5px;'></div>");
                var addBtn = $("<input type='button' id='addBtn' value='新增'/>");
                var delBtn = $("<input type='button' id='delBtn' value='删除'/>");
                var saveBtn = $("<input type='button' id='saveBtn' value='保存'/>");
                toolbar.append(container);
                container.append(addBtn);
                container.append(delBtn);
                container.append(saveBtn);
                
                addBtn.on('click',addBtnHandler);
                saveBtn.on('click',saveBtnHandler);
                delBtn.on('click',delBtnHandler);
            },
            columns:[
					 {text: '选择',  datafield: 'isCheck',columntype:'checkbox',cellsalign: 'center',width: 100}, 
                     {text:'序号',datafield:'no',align: 'center', cellsalign: 'left', width: 50,editable:false},
                     {text: '名称',datafield: 'name', align: 'center', cellsalign: 'left', width: 200,editable:true},
                     {text: '作用描述',  datafield: 'describe', align: 'center', cellsalign: 'left', width: 400,editable:true},
                     {text: '是否可用',  datafield: 'available', displayfield: 'availableList',columntype:'dropdownlist',width: 100,editable:true,
                    	 createeditor: function (row, column, editor) {              
                      		  editor.jqxDropDownList({
                      			  width:100,    
                      			  autoDropDownHeight: true,  
                      			  source: yesornoAdapter,  
                      			  placeHolder:'请选择：',
                      			  displayMember:"label", 
                      			  valueMember:"value" 
                      		  }); }
                     },
                     {text: '运行代码',  datafield: 'programCode',columntype:'button', align: 'center', cellsalign: 'left', width: 150,
                    	 cellsrenderer:function(row, column, value){
                     	 	return "录入代码";   
                       	 },
                    	 buttonclick:function(row,owner){
                    		 $("#programWindow").jqxWindow('open'); 
                    		 var dataRecord = $("#jqxgrid").jqxGrid('getrowdata', row);
                    		 $("#programCodeArea").val(dataRecord.programCode);
                    	 }}
                    ]
  		});
  	});
  	
  	function addBtnHandler(){
  		var addRow = {
  	  			guid:"",
  	  			name:"",
  	  			describe:"",
  	  			available:"0",
  	  			programCode:"",
  	  			flag:"I"
  	  	};
  		 $("#jqxgrid").jqxGrid('addrow', null, addRow);
  		//$('#addWindow').jqxWindow('open');
  	}
  	
  	function delBtnHandler(){
  		var rows = $("#jqxgrid").jqxGrid('getrows');
  		var delArr = new Array();
  		$.each(rows,function(index,rowData){
  			if(rowData.isCheck==true){
  				delArr.push(rowData.guid);
  			}
  		});
  		var json = Hq.JSON.stringify(delArr);
  		$.ajax({  
            type:"post",
            url: "delRemoteProcedure.do",  
            dataType: "json", 
            data:{procedureIdList:json},
            success: function(data){ 
            	if(data.success != undefined){
            		alert(data.success);
            	}else{
            		alert("删除失败！");
            	}
            	$("#jqxgrid").jqxGrid({source:dataAdapter});
            }
        });
  	}
  	
  	function saveBtnHandler(){
  		var rows = $("#jqxgrid").jqxGrid('getrows');
  		var objArr = new Array();
  		$.each(rows,function(index,rowData){
  			if(rowData.flag != undefined){
  				objArr.push(rowData);
  			}else{
  				$.each(orignalData,function(i,oData){
  					 if(oData.guid == rowData.guid && ((oData.name != rowData.name || oData.describe != rowData.describe
  		  					|| oData.available != rowData.available || oData.programCode != rowData.programCode))){
  						objArr.push(rowData);
  					 }
  				});
  			
  				
  			}
  		});
  		var objJson = JSON.stringify(objArr);
  		$.ajax({   
            type:"post",
            url: "procedureConfig/saveRemoteProcedure.do", 
            data:{objList:objJson},
            success: function(data){ 
            	if(data.success != undefined){
            		alert(data.success);
            	}else{
            		alert("保存失败！");
            	}
            	$("#jqxgrid").jqxGrid({source:dataAdapter});
            }
        });
  		
  		
  	}
	</script>
  </head>
  
  <body>   
  	<div id="jqxgrid" align="center" style="width:100%;height:100%"></div>
  	<div id="programWindow" style="display:none;">  
    	<div> 
			<div>
		      	<textarea id="programCodeArea" cols=60 rows=15 style="overflow:auto">    
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
