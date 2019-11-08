	var tableIns;
	var form;
	var layer;
	//JavaScript代码区域
	layui.use([ 'table', 'element', 'form', 'layer', 'laypage' ],
					function() {
						var table = layui.table, element = layui.element, laypage = layui.laypage;
						layer = layui.layer;
						form = layui.form;
					});

	$(function() {


		if (flag == 0) {
			$('#attributeTab li:eq(0) a').tab('show');
		} else if (flag == 1) {
			$('#attributeTab li:eq(1) a').tab('show');
		} else if (flag == 2) {
			$('#attributeTab li:eq(2) a').tab('show');
		} else if (flag == 3) {
			$('#attributeTab li:eq(3) a').tab('show');
		} else if (flag == 4) {
			$('#attributeTab li:eq(4) a').tab('show');
		} else if (flag == 5) {
			$('#attributeTab li:eq(1) a').tab('show');
		} else if (flag == 6) {
			$('#attributeTab li:eq(2) a').tab('show');
		}

		$("form").submit(function(e) {
			var id = $("#id").val();
			var flowId = $("#flowId").val();
			var processname = $("#processname").val();
			var nodeCode = $("#nodeCode").val();
			if (processname == "") {
				layer.msg('节点名称不能为空！', {
					icon : 5,
					time : 1000
				});
				$("#processname").focus();
				return false;
			}
			if (nodeCode == "") {
				layer.msg('节点编码不能为空！', {
					icon : 5,
					time : 1000
				});
				$("#nodeCode").focus();
				return false;
			}
			$.ajaxSettings.async = false;
			$.post(webname+"/backsystem/flow/foundCode", {
				flowId : flowId,
				nodeCode : nodeCode,
				id:id
			}, function(data) {
					if(data=="1"){
						
						flag=1;
					}else{
						flag=2;
					}
			});
			if(flag==1){
				return true;
			}else{
				layer.msg('编码重复！', {
					icon : 5,
					time : 1000
				});
				$("#nodeCode").focus();
				return false;
			}
			
		});

		$("#flow_attribute").ajaxForm(function(data) {
	
			var json = $.parseJSON(data);//字符串转json
			console.log(json);
			saveAttribute(json.msg);

			huidiao(json);

		});

	});

	document.onkeyup = function(event) {
		var e = event || window.event;
		var keyCode = e.keyCode || e.which;
		switch (keyCode) {
		case 13:
			$("#attributeOK").click();
			break;

		default:
			break;
		}
	}
////////////////////////////////////////////////
	
	if (color != null && color != '') {

		$("#style_color").css({
			color : "#fff",
			background : color
		});
	}


	if (icon != null && icon != '') {
		$("#style_icon_preview").attr("class",
				icon + " icon-white");
	}
