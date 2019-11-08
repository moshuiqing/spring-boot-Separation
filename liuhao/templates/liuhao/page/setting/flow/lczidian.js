var tableIns;
var form;
var laydate;
var index;

layui
		.use(
				[ 'table', 'form', 'element', 'form', 'layer', 'laydate' ],
				function() {
					var table = layui.table;
					var element = layui.element, layer = layui.layer;
					laydate = layui.laydate;
					form = layui.form;

					tableIns = table
							.render({
								elem : '#dg',
								url : webname + "/backsystem/flow/listNode",
								page : true // 开启分页
								,
								limit : 10,
								where : {
									flowId : flowId
								},
								cols : [ [ // 表头
										{
											type : 'numbers',
											width : "5%",
											title : '序号'
										},
										{
											field : 'processname',
											title : '节点名称',
											width : "20%"
										},
										{
											field : 'processtype',
											title : '节点类型',
											width : "15%",
											templet : function(row) {
												if (row.processtype == 0) {
													return "状态节点";
												} else if (row.processtype == 1) {
													return "前操作节点";
												} else if (row.processtype == 2) {
													return "后操作节点";
												}

											}
										},
										{
											field : 'nodeCode',
											title : '节点编号',
											width : "20%"
										},
										{
											field : 'mark',
											title : '备注',
											width : "20%"
										},
										{
											field : 'null',
											title : '操作',
											width : "20%",
											templet : function(row) {
												var html = '<a href="##" class=\"layui-btn layui-btn-sm ywsp-pop updateBtn\" onclick="update(\''
														+ row.id
														+ '\')"><i class=\"layui-icon\">&#xe642;</i>编辑</a>';
												html += '&nbsp;<a href="##" class=\"layui-btn layui-btn-danger layui-btn-sm ywsp-pop updateBtn\" onclick="del(\''
														+ row.id
														+ '\')"><i class=\"layui-icon\">&#xe640;</i>删除</a>';

												return html;
											}
										} ] ]
							});

					form.on('select(chage)',
							function(data) {

								if (data.value == 1) {

									$("#nodeCode").removeAttr("disabled");
									$("#nodeCode").attr("lay-verify",
											"required");

								} else {

									$("#nodeCode").attr("disabled", "disabled")
											.val("");
									$("#nodeCode").removeAttr("lay-verify");
								}

							});

					// form表单监听
					form.on(("submit(submit)"), function(data) {

						var param = {};
						param = data.field;
						 console.log(param);
						 param.flowId=flowId;
						$.ajax({
							url : webname + '/backsystem/flow/zidianInsert',
							type : 'post',
							dataType : 'text',
							data : param,
							success : function(data) {
								layer.msg(data, {
									time : 1000
								});

								layer.close(index);
								tableIns.reload({});
							}
						});

						return false;
					})
					
					
					//打开新增弹窗
					form.on(("submit(add)"), function(data) {
						form.val("node", {
							"id":"",
							"processname" : "",
							"pd" :  1,
							"nodeCode" :  "",
							"processtype" :  "",
							"mark" :  ""
						})
						tanchuang();
						return false;
					});
					

				});

function del(id) {

	var url = webname + "/backsystem/flow/deleteNode";
	layer.confirm('您确定要删除这条数据吗？', {
		title : "提示",
		icon : 7,
		btn : [ '确定', '取消' ]
	}, function() {
		$.post(url, {
			id : id,
			flowId : flowId
		}, function(data) {
			if (data == "1") {
				layer.msg('删除成功！', {
					icon : 6,
					time : 1000
				});
			} else {
				layer.msg('删除失败！', {
					icon : 5,
					time : 1000
				});
			}
			tableIns.reload({});
		})
	})
}

// 弹窗
function tanchuang() {
	index = layer.open({
		title : '节点', // 标题
		type : 1,
		area : [ '720px', '650px' ],
		shadeClose : false, // 点击遮罩关闭
		content : $('.czmm-pop'),
		skin : 'pop-class'
	});
}
// 打开修改弹窗
function update(id) {

	$("#id").val(id);
	foundInfo(id);
	tanchuang();

}

function foundInfo(id) {

	$.post(webname + "/backsystem/flow/getNodeById", {
		id : id,
		flowId : flowId
	}, function(data) {
		/*console.log(data);
		alert(data.pd)*/
		form.val("node", {
			"processname" : data.processname,
			"pd" : 1,
			"nodeCode" : data.nodeCode,
			"processtype" : data.processtype,
			"mark" : data.mark
		})
		form.render();
	})

}
