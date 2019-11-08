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
		elem: '#reply',
		url: webname + '/backsystem/replaymsg/pageFound',
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
					width: '5%'
				},
				{
					type: 'numbers',
					width: '5%',
					title: '序号'
				},
				{
					field: 'problem',
					title: '问题',
					width: '30%',
					align: "center"
				},
				{
					field: 'answer',
					title: '答案',
					width: '45%',
					align: "center"
				},
				{
					title: '操作',
					width: '15%',
					fixed: "right",
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


		}
	});

	///////////////

	//添加用户
	function addUser() {

		form.val("biaodan", {
			"id": "",
			"copyproblem": "",
			"problem": "",
			"answer": ""
		});
		form.render();
		index = layer.open({
			title: '添加微信回复信息', //标题
			type: 1,
			area: ['600px', '383px'],
			offset: 'auto',
			shadeClose: false, //点击遮罩关闭
			content: $('#tc'),
			skin: 'show'
		});
	}
	$(".addNews_btn").click(function() {
		addUser();
	})

	$(".updateReply").click(function() {
		$.post(webname + "/backsystem/replaymsg/updateSearchReply", function(d) {
			if (qjty(d)) {
				layer.msg(d.msg);
			}
		})

	})
	////////////////////////////////




	//提交监听
	form.on('submit(submit)', function(d) {
		var param = d.field;
		var copyproblem = param.copyproblem;
		if (copyproblem == param.problem) {
			param.problem = null;
		}
		var url = "/backsystem/replaymsg/addReply";
		if (param.id != null && param.id != '') {
			url = "/backsystem/replaymsg/updateReply";
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




});


//打开编辑弹窗
function update(id, problem, answer) {
	form.val("biaodan", {
		"id": id,
		"copyproblem": problem,
		"problem": problem,
		"answer": answer
	});
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
