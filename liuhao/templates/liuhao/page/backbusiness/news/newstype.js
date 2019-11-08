var index;
var layer;
var tableIns;
var form;

layui.use(['element', 'form', 'layer', 'table', 'laytpl', 'tree'], function() {
	var $ = layui.jquery,
		laytpl = layui.laytpl,
		element = layui.element,
		table = layui.table;
	//layer = parent.layer === undefined ? layui.layer : top.layer;
	layer = layui.layer;
	form = layui.form;

	//新闻类型
	tableIns = table.render({
		elem: '#newstype',
		url: webname + '/backsystem/news/foundPageNewsType',
		cellMinWidth: 95,
		page: true,
		height: "full-125",
		imits: [10, 15, 20],
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
					field: 'typeName',
					title: '类型名称',
					width: '35%',
					align: "center"
				},
				{
					field: 'createTime',
					title: '创建时间',
					width: '25%',
					align: 'center'
				},
				{
					title: '操作',
					width: '30.2%',
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
		]
	});

	//新增/修改修改提交
	form.on("submit(submit)", function(data) {
		var param = data.field;

		if (param.id != '') {
			//修改
			$.post(webname + "/backsystem/news/updateNewsType", param, function(d) {
				if (qjty(d)) {
					layer.msg(d.msg);
					if (d.code > 0) {
						tableIns.reload({});
						layer.close(index);
					}

				}
			})

		} else {
			//新增
			$.post(webname + "/backsystem/news/addNewsType", param, function(d) {
				if (qjty(d)) {
					layer.msg(d.msg);
					if (d.code > 0) {
						tableIns.reload({});
						layer.close(index);
					}
				}
			})

		}


		//console.log(param);
	})



});

//打开添加用户弹窗
function addNewsType() {
	$("#typeName").val("");

	form.render();
	index = layer.open({
		title: '添加新闻类型', //标题
		type: 1,
		area: ['580px', '243px'],
		offset: 'auto',
		shadeClose: false, //点击遮罩关闭
		content: $('#tc'),
		skin: 'show'
	});
}
//打开修改用户弹窗
function updateNewsType(id, typeName) {
	$("#id").val(id);
	$("#copytypeName").val(typeName);
	$("#typeName").val(typeName);
	form.render();
	index = layer.open({
		title: '修改新闻类型', //标题
		type: 1,
		area: ['580px', '243px'],
		offset: 'auto',
		shadeClose: false, //点击遮罩关闭
		content: $('#tc'),
		skin: 'show'
	});

}
//删除
function delNewsType(id) {
	layer.confirm('确定要删除吗？', {
		icon: 3,
		title: '提示'
	}, function(index) {
		$.post(webname + "/backsystem/news/deleteNewsType", {
			id: id
		}, function(d) {
			if (qjty(d)) {
				layer.msg(d.msg);
				if (d.code > 0) {
					tableIns.reload({});
					layer.close(index);
				}
			}
		})
	});
}
