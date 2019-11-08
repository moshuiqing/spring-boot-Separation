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

	//在线用户 列表
	tableIns = table.render({
		elem: '#online',
		url: webname + '/backsystem/main/getOnlineUser',
		cellMinWidth: 95,
		height: "full-125",
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
					field: 'sessionId',
					title: '会话Id',
					width: '25%',
					align: "center"
				},
				{
					field: 'userIp',
					title: 'ip地址',
					width: '15%',
					align: 'center'
				},
				{
					field: 'userName',
					title: '用户名',
					width: '10%',
					align: 'center'
				},
				{
					field: 'date',
					title: '最后访问时间',
					width: '15%',
					align: 'center'
				},
				{
					title: '操作',
					width: '25.2%',
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

});

function outUser(id) {

	$.post(webname + "/backsystem/main/deleteoutUser", {
		sessionId: id
	}, function(d) {
		if (qjty(d)) {
			layer.msg(d.msg);
			tableIns.reload({});
		}
	})

}
