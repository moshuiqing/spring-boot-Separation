var index;
var layer;
var tableIns;
var form;
var ejindex;
var jeindex;
var tree;
layui.use(['element', 'form', 'layer', 'table', 'laytpl', 'tree'], function() {
	var $ = layui.jquery,
		laytpl = layui.laytpl,
		element = layui.element,
		table = layui.table;
	//layer = parent.layer === undefined ? layui.layer : top.layer;
	layer = layui.layer;
	form = layui.form;
	tree = layui.tree;

	//角色列表
	tableIns = table.render({
		elem: '#role',
		url: webname + '/backsystem/main/pageFoundRole',
		cellMinWidth: 95,
		height: "full-125",
		page: true,
		limits: [10, 15, 20],
		limit: 10,
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
					field: 'name',
					title: '角色名',
					width: '15%',
					align: "center"
				},
				{
					field: 'remark',
					title: '描述',
					width: '30.2%',
					align: 'center'
				},
				{
					field: 'modelid',
					title: '权限',
					width: '10%',
					align: 'center',
					templet: function(d) {
						var h = "";
						var getTpl = $("#quxian").html();
						laytpl(getTpl).render(d, function(html) {
							h = html;
						});
						return h;
					}
				},
				{
					field: 'menuid',
					title: '菜单',
					width: '10%',
					align: 'center',
					templet: function(d) {
						var h = "";
						var getTpl = $("#menu").html();
						laytpl(getTpl).render(d, function(html) {
							h = html;
						});
						return h;
					}
				},
				{
					title: '操作',
					width: '25%',
					align: "center",
					templet: function(d) {
						var h = "";
						var getTpl = $("#dome").html();
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
			
			//// 


		}
	});

	///提交监听
	form.on("submit(submit)", function(data) {
		var param = data.field;
		console.log(param);
		var url = "/backsystem/main/updateRoleById";
		if (param.name == param.copyname) {
			param.name = '';
		}
		if(param.id==null || param.id==""){
			url="/backsystem/main/addRole"
		}
		
		$.post(webname + url, param, function(d) {
			if (qjty(d)) {
				layer.close(jeindex);
				layer.msg(d.msg);
				tableIns.reload({});
			}

		})


		return false;

	})

	//查找监听
	form.on("submit(found)", function(data) {

		var param = data.field;
		tableIns.reload({
			page: {
				curr: 1 //重新从第 1 页开始
			},
			where: param
		})

		return false;
	})

});


//新增
function addRole() {
	form.val("addOrUp", {
		"id": "",
		"name": "",
		"copyname": "",
		"remark": ""
	});

	jeindex = layer.open({
		title: '新增角色', //标题
		type: 1,
		area: ['520px', '300px'],
		offset: 'auto',
		shadeClose: false, //点击遮罩关闭
		content: $('#tc'),
		skin: 'show'
	});
}



//查看权限
function lookRole(info) {
	$("#tree").html("");

	$.post(webname + "/backsystem/main/getModel", {
		info: info
	}, function(d) {

		if (d != null) {
			if (qjty(d)) {
				tree.render({
					elem: "#tree",
					showCheckbox: false,
					data: d,
					click: function(node) {

					}

				});
			}
		}
	});


	ejindex = layer.open({
		title: '查看权限', //标题
		type: 1,
		area: ['300px', '500px'],
		offset: 'auto',
		shadeClose: false, //点击遮罩关闭
		content: $('#ej'),
		skin: 'show'
	});
}


////////
function lookmenu(info, binfo) {
	$("#tree").html("");
	//	$("#type").val("menu");
	$.post(webname + "/backsystem/main/getMenus", {
		info: info,
		binfo: binfo
	}, function(d) {

		if (d != null) {
			if (qjty(d)) {
				tree.render({
					elem: "#tree",
					showCheckbox: false,
					data: d,
					click: function(node) {

					}

				});
			}
		}
	});

	ejindex = layer.open({
		title: '查看菜单', //标题
		type: 1,
		area: ['300px', '500px'],
		offset: 'auto',
		shadeClose: false, //点击遮罩关闭
		content: $('#ej'),
		skin: 'show'
	});
}
//////////////////////////////////////////////////////////////////启用 禁用 删除/////////////////////////////////////////
function change(id, isdesable) {
	if (isdesable == 0) {
		isdesable = 1;
	} else if (isdesable == 1) {
		isdesable = 0
	}

	$.post(webname + "/backsystem/main/updateDisable", {
		id: id,
		isdesable: isdesable
	}, function(d) {
		if (qjty(d)) {
			layer.msg(d.msg);
			tableIns.reload({});
		}
	})
}

function del(id) {

	layer.confirm('确定要删除吗？', {
		icon: 3,
		title: '提示'
	}, function(index) {
		$.post(webname + "/backsystem/main/deleteRole", {
			id: id
		}, function(d) {
			if (qjty(d)) {
				layer.msg(d.msg);
				tableIns.reload({});
			}
		})
	});

}





////////////////////////////////////////////////////////////////////////编辑/////////////////////////////////////////

function edtRole(id, info) {
	$("#xtree").html("");
	var href = webname + "/backsystem/main/updateFoundRole";

	$.post(href, {
		id: id,
		modelid: info
	}, function(d) {

		if (d != null) {
			if (qjty(d)) {
				tree.render({
					elem: "#xtree",
					showCheckbox: true,
					data: d,
					id: 'qx',
					oncheck: function(obj){
						$(".layui-tree div[data-id='4'] .layui-form-checkbox ").addClass("layui-form-checked");
					}

				});
			$(".layui-tree div[data-id='4'] .layui-form-checkbox ").addClass("layui-form-checked");
			}
		}
	});


	jeindex = layer.open({
		title: '权限设置', //标题
		type: 1,
		btn: ['确定', '取消'],
		area: ['300px', '500px'],
		offset: 'auto',
		shadeClose: false, //点击遮罩关闭
		content: $('#je'),
		skin: 'show',
		yes: function(index, layero) {

			var checkData = tree.getChecked('qx');
			console.log(checkData);
			var ids = "";
			for (var i = 0; i < checkData.length; i++) {
				ids += checkData[i].id + ",";
			}
			ids = ids.substring(0, ids.lastIndexOf(','));

			$.post(webname + "/backsystem/main/updateRole", {
				id: id,
				modelid: ids
			}, function(d) {
				if (qjty(d)) {
					layer.close(jeindex);
					layer.msg(d.msg);
					tableIns.reload({});
				}
			})


		}
	});
}



function edtMenu(rid, bigmenuid, menuid) {
	$("#xtree").html("");
	var href = webname + "/backsystem/main/updateFoundMenu";

	$.post(href, {
		bigmenuid: bigmenuid,
		menuid: menuid
	}, function(d) {
		if (d != null) {
			if (qjty(d)) {
				tree.render({
					elem: "#xtree",
					showCheckbox: true,
					data: d,
					id: 'menu',
					oncheck: function(node) {

					}

				});
			}
		}
	});




	jeindex = layer.open({
		title: '菜单设置', //标题
		type: 1,
		btn: ['确定', '取消'],
		area: ['300px', '500px'],
		offset: 'auto',
		shadeClose: false, //点击遮罩关闭
		content: $('#je'),
		skin: 'show',
		yes: function(index, layero) {

			var checkData = tree.getChecked('menu');
			console.log(checkData);
			var ids = "";
			var bids = ""
			for (var i = 0; i < checkData.length; i++) {
					bids += checkData[i].id + ",";
					if(checkData[i].children!=null){
						var one = checkData[i].children;
						for(var j = 0;j<one.length;j++){
							ids +=one[j].id+",";							
							if(one[j].children!=null){
								var two = one[j].children;
								for(var k=0;k<two.length;k++){
									ids+=two[k].id+",";
								}
							}
							
						}
						
					}
			}
			console.log(ids+"&&&&&&&&&&&"+bids);
			 $.post(webname + "/backsystem/main/updateRole", {
				id: rid,
				menuid: ids,
				bigmenuid: bids
			}, function(d) {
				if (qjty(d)) {
					layer.close(jeindex);
					layer.msg(d.msg);
					tableIns.reload({});
				}
			}) 


		}
	});
}

function update(rid, rname, remake) {

	form.val("addOrUp", {
		"id": rid,
		"name": rname,
		"copyname": rname,
		"remark": remake
	});

	jeindex = layer.open({
		title: '新增角色', //标题
		type: 1,
		area: ['520px', '300px'],
		offset: 'auto',
		shadeClose: false, //点击遮罩关闭
		content: $('#tc'),
		skin: 'show'
	});
}
