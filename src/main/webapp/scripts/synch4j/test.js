$(function(){
	Hq.create({
		renderId : "testDiv",
		xType : "grid",
		height : $(window).height()-20,
		width : $(window).width(),
		definePath : "synch2/test/getDefine.do",
		getDataPath : "synch2/test/getData.do",
		tbar:[{
			type : 'button',
			text : "重新导入",
			disabled : false,
			icon : "add",
			id : "addPatternBtn",
			handler :function(){
				var data = Hq.getCmp("testDiv").getSelectedRows();
				if(data.length == 0){
					Hq.MessageBox.alert("请选择一条记录！");
				}
				Hq.Msg.confirm("确定重新导入吗？",
					function(){
						var fileGuidArr = new Array();
						$.each(data,function(index,items){
							fileGuidArr.push(items.FILEGUID);
						});
						Hq.ajax({
							url : "synch2/test/reset.do",
							cache : false,
							type : "post",
							data : {
								fileGuidArr:Hq.JSON.stringify(fileGuidArr)
							},
							dataType : "json",
							success : function(jsonReturn) {
								if(jsonReturn == "1"){
									Hq.getCmp("testDiv").refreshTable();
									Hq.MessageBox.alert("重置成功！");
								}
									
							}
						});
				});
				
			}
		}],
		callback:{
			completed : function(){
				setInterval(function(){
					Hq.getCmp("testDiv").refreshTable();
				},60000);
			}
		}
	});
})