var index;
var layer;
var tableIns;
var form;
layui.use(['form', 'layer', 'table', 'laytpl'], function() {
	var $ = layui.jquery,
		laytpl = layui.laytpl,
		table = layui.table;
	//layer = parent.layer === undefined ? layui.layer : top.layer;
	layer = layui.layer;
	form = layui.form;

	//用户列表
	tableIns = table.render({
		elem: '#userList',
		url: webname + '/backsystem/main/pageFoundSysUsers',
		cellMinWidth: 95,
		page: true,
		height: "full-125",
		limits: [10, 15, 20],
		limit: 10,
		id: "userListTable",
		cols: [
			[{
					type: "checkbox",
					fixed: "left",
					width: '5%',
				},
				{
					type: 'numbers',
					width: '5%',
					title: '序号'
				},
				{
					field: 'userName',
					title: '用户名',
					width: '10%',
					align: "center"
				},
				{
					field: 'person.email',
					title: '用户邮箱',
					width: '15%',
					align: 'center',
					templet: function(d) {
						var email = "暂无数据";
						if (d.person != null) {
							email = d.person.email;
						}

						return '<a class="layui-blue" href="mailto:' + email + '">' + email + '</a>';
					}
				},
				{
					field: 'role.name',
					title: '用户角色',
					width: '15%',
					align: 'center',
					templet: function(d) {
						var rname = "暂无角色";
						if (d.role != null) {
							rname = d.role.name;
						}
						return rname;

					}
				},
				{
					field: 'isDisable',
					title: '用户状态',
					width: '10%',
					align: 'center',
					templet: function(d) {
						return d.isDisable == "0" ? "正常使用" : "禁止使用";
					}
				},
				{
					field: 'userEndTime',
					title: '最后登录时间',
					width: '15.2%',
					align: 'center'
				},
				{
					title: '操作',
					width: '25%',
					templet: '#userListBar',
					align: "center",
					templet: function(d) {

						var h = "";
						var getTpl = $("#caozuo").html();
						laytpl(getTpl).render(d, function(html) {
							h = html;
						});
						return h;

					}
				}
			]
		],
		done: function(res, curr, count) {

			var d = res.data;
			for (var i = 0; i < d.length; i++) {
				if (d[i].id == "Administrator") {
					
					$(".layui-table tr[data-index=" + i + "] input[type='checkbox']").prop('disabled', true);
					$(".layui-table tr[data-index=" + i + "] input[type='checkbox']").next().addClass('layui-btn-disabled');
					$('.layui-table tr[data-index=' + i + '] input[type="checkbox"]').prop('name', 'caib');

				}
			}


		}
	});



	//提交监听
	form.on('submit(submit)', function(d) {
		var param = d.field;
		var copyname = $("#copyname").val();
		if (copyname == param.userName) {
			param.userName = null;
		}
		var url = "/backsystem/main/addSystemUser";
		if(param.id!=null && param.id!=''){
			url = "/backsystem/main/updateUser";
		}
		
		
		$.post(webname + url, param, function(d) {
			if (qjty(d)) {
				layer.msg(d.msg);

				if (d.code > 0) {
					layer.close(index);
					tableIns.reload({});
				}
			}
		})


		return false;
	})



	$(".search_btn").on("click", function() {

		tableIns.reload({
			page: {
				curr: 1 //重新从第 1 页开始
			},
			where: {
				userName: $(".searchVal").val() //搜索的关键字
			}
		})

	});

	//添加用户
	function addUser(edit) {
		$("#uid").val("");
		$("#username").val("");
		$("#password").val("");
		$("#roleid").val("");
		$("#copyname").val("");		
		$.post(webname + "/backsystem/main/getAllRole", function(d) {
			if (qjty(d)) {
				index = layer.open({
					title: '添加系统用户', //标题
					type: 1,
					area: ['600px', '383px'],
					offset: 'auto',
					shadeClose: false, //点击遮罩关闭
					content: $('#tc'),
					skin: 'show'
				});
				
				
				var o = d.object;
				 var html= $("#roleid");
				 for(var i=0;i<o.length;i++){
					  html.append("<option value="+o[i].id+">"+o[i].name+"</option>"); 
				 }
				form.render();
				
				
				
			}
		})
		
		
		
		
	}
	$(".addNews_btn").click(function() {
		addUser();
	})



	//批量删除
	$(".delAll_btn").click(function() {
		var checkStatus = table.checkStatus('userListTable'),
			data = checkStatus.data,
			uids = "";
		if (data.length > 0) {
			for (var i in data) {
				uids += data[i].id + ",";
			}
			//  console.log(uids);

			layer.confirm('确定删除选中的用户？', {
				icon: 3,
				title: '提示信息'
			}, function(index) {
				$.post(webname + "/backsystem/main/deletes", {
					uids: uids
				}, function(d) {
					if (qjty(d)) {

						layer.msg(d.msg);
						tableIns.reload();
						layer.close(index);
					}
				})


				// })
			})
		} else {
			layer.msg("请选择需要删除的用户");
		}
	})


})
//////////////////////////////////////////////////////////////////////
//打开编辑弹窗
function update(id, name, roleid) {

	$("#uid").val(id);
	$("#username").val(name);
	$("#password").val("");
	$("#roleid").val(roleid);
	$("#copyname").val(name);
	form.render();
	index = layer.open({
		title: '修改系统用户', //标题
		type: 1,
		area: ['600px', '383px'],
		offset: 'auto',
		shadeClose: false, //点击遮罩关闭
		content: $('#tc'),
		skin: 'show'
	});
}
//启用 禁用
function change(id, state) {

	if (state == '0') {
		state = "1";
	} else if (state == '1') {
		state = '0';
	}
	var param = {
		id: id,
		isDisable: state
	};
	$.post(webname + "/backsystem/main/change", param, function(d) {
		if (qjty(d)) {
			layer.msg(d.msg);
			if (d.code > 0) {
				tableIns.reload({});
			}
		}

	})


}

//删除用户
function del(id) {
	layer.confirm('确定要删除吗？', {
		icon: 3,
		title: '提示'
	}, function(index) {
		$.post(webname + "/backsystem/main/delete", {
			id: id
		}, function(d) {
			if (qjty(d)) {
				layer.msg(d.msg, {
					time: 2
				});
				if (d.code > 0) {
					tableIns.reload({});
				}
			}
		})
	});

}
