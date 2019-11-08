var tableIns;
var form;
//JavaScript代码区域
layui.use(['table','element','form','layer','laypage'], function(){
    var table = layui.table
    	,element = layui.element
        ,form = layui.form
        ,layer = layui.layer
        ,laypage = layui.laypage;
    
    /*表格赋值*/
    tableIns=table.render({
	    elem: '#flowlist'
	    ,url:webname+"/backsystem/flow/getflowList"
	    ,page: true //开启分页
	    ,limit:10
	    ,cols: [[ //表头
	            { type: 'numbers', width:"5%",title: '序号' },
	            {field:'flowName',title:'流程名称',width:"20%"},
	            {field:'flowContent',title:'流程说明',width:"30%"},
		        {field:'isendble',title:'流程状态',width:"20%",
	            	templet:function(row){	            		
		        		if(row.isendble=='0'){
		        			return "已停用";
		        		}
		        		if(row.isendble=='1'){
		        			return "已激活";
		        		}
		        	}
	            },
	  	        {field:'null',title:'操作',width:"25%",templet: function(row){
	  	        		var html='<a href="##" class=\"layui-btn layui-btn-sm ywsp-pop edit-btn\" name=\"' + row.flowId + '\"><i class=\"layui-icon\">&#xe642;</i>设计</a>';
	  	        		if(row.isendble=='0'){
	  	        			html+='&nbsp;<a href="##" class=\"layui-btn layui-btn-normal layui-btn-sm ywsp-pop sta-btn\" name=\"' + row.flowId + '\"><i class=\"layui-icon\">&#xe609;</i>激活</a>';
	  	        		}else if(row.isendble=='1'){
	  	        			html+='&nbsp;<a href="##" class=\"layui-btn layui-btn-warm layui-btn-sm ywsp-pop stp-btn\" name=\"' + row.flowId + '\"><i class=\"layui-icon\">&#xe64d;</i>停用</a>';
	  	        		}
	  	        	
	  	        		
	  	        		html+='&nbsp;<a href="##" class=\"layui-btn layui-btn-danger layui-btn-sm ywsp-pop delete-btn\" name=\"' + row.flowId + '\"><i class=\"layui-icon\">&#xe640;</i>删除</a>';
	  	        		html+='&nbsp;<a href="##" class=\"layui-btn layui-btn-sm ywsp-pop \" onclick="zidian(\'' + row.flowId + '\')"><i class=\"layui-icon\">&#xe656;</i>字典</a>';
	  	        		return html;
	  	        	}
	  	        }
	    ]]    
	});
    //添加账户弹窗
    $(".add-tjzx-btn").on('click', function(){
    	$("#flowName").val("");
		$("#flowContent").val("");
    	//清空新增框
    	$("#name").val("");
        layer.open({
            title: '新增', //标题
            type: 1,
            area: ['720px', '433px'],
            shadeClose: false, //点击遮罩关闭
            content: $('.add-tjzx-pop'),
            skin: 'pop-class'
        });
    });
    //添加按钮点击事件
	$(document).on("click", "#add", function(){
		var flowName = $("#flowName").val();
		var flowContent=$("#flowContent").val();
    	if(!flowName){
    		layer.alert("请填写流程名称", {icon: 2});
    		return;
    	}
    	layer.msg("数据保存中，请稍后", {
			icon: 16,
			shade: 0.3,
			time: 3000
		});
    	$.ajax({
			type : "POST",
			dataType : "text",
			url : webname+"/backsystem/flow/addflow",
			data : {flowName:flowName,flowContent:flowContent},
			success : function(data) {
					tableIns.reload({});
	        	    layer.msg(data,{time: 1000});
	        	    layer.closeAll(); 
			},
			error:function(){
				layer.msg("服务异常",{icon: 5,time: 1000});
			}
		});
	});
    //取消添加
    $(document).on("click", "#concelAdd", function(){
    	layer.closeAll();  //关闭所有窗口
    })
    //编辑按钮绑定事件
    $(document).on("click", ".edit-btn", function(){
    	var flowId = $(this).attr("name");
    	window.location.href=webname+"/backsystem/flow/sheji?flowId="+flowId;
    });
    
    //删除确认弹窗
    $(document).on("click", ".delete-btn", function(){
    	var delid = $(this).attr("name");
        layer.alert('您确定要删除？', {
            icon: 3,
            area: ['200px', '160px'],
            skin: 'pop-class',
            btn: ['确定', '取消'],
            yes: function(){   //确定的回调
                //code
            	$.post(webname+"/backsystem/flow/delflow",{flowId:delid},function(data){
            		 tableIns.reload({});
  	        	   layer.msg(data,{time: 1000});
                });
            }
            ,btn2: function(){  //取消的回调
                layer.closeAll();  //关闭所有窗口
            }
        });
    });
  	//激活按钮绑定事件
    $(document).on("click", ".sta-btn", function(){
    	var flowId = $(this).attr("name");
        layer.alert('您确定要激活？', {
            icon: 3,
            area: ['200px', '160px'],
            skin: 'pop-class',
            btn: ['确定', '取消'],
            yes: function(){   //确定的回调
                //code
            	$.post(webname+"/backsystem/flow/updateflow",{flowId:flowId,isendble:"1"},function(data){
            		  tableIns.reload({});
    	        	   layer.msg(data,{time: 1000});
                });
            }
            ,btn2: function(){  //取消的回调
                layer.closeAll();  //关闭所有窗口
            }
        });
    });
  	//停用按钮绑定事件
    $(document).on("click", ".stp-btn", function(){
    	var flowId = $(this).attr("name");
        layer.alert('您确定要停用？', {
            icon: 3,
            area: ['200px', '160px'],
            skin: 'pop-class',
            btn: ['确定', '取消'],
            yes: function(){   //确定的回调
                //code
            	$.post(webname+"/backsystem/flow/updateflow",{flowId:flowId,isendble:"0"},function(data){
             	  
             		   tableIns.reload({});
     	        	   layer.msg(data,{time: 1000});
             	
                });
            }
            ,btn2: function(){  //取消的回调
                layer.closeAll();  //关闭所有窗口
            }
        });
    });
});

//进入字典
function zidian(id){
	window.location.href=webname+"/backsystem/flow/proDictionary?flowId="+id;
}

