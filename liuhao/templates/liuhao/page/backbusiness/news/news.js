var index;
var layer;
var tableIns;
var form;
var table;

layui.use(['element', 'form', 'layer', 'table', 'laytpl', 'tree'], function() {
	var $ = layui.jquery,
		laytpl = layui.laytpl,
		element = layui.element;
	var table = layui.table;
	//layer = parent.layer === undefined ? layui.layer : top.layer;
	layer = layui.layer;
	form = layui.form;

	//新闻类型
	tableIns = table.render({
		elem: '#news',
		url: webname + '/backsystem/news/foundPageNews',
		cellMinWidth: 95,
		page: true,
		height: "full-125",
		imits: [10, 15, 20],
		limit: 10,
		id: "newsListTable",
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
					field: 'title',
					title: '标题',
					width: '20%',
					align: "center"
				},
				{
					field: 'typeName',
					title: '类型',
					width: '15%',
					align: "center"
				},
				{
					field: 'author',
					title: '作者',
					width: '15%',
					align: "center"
				},
				{
					field: 'createTime',
					title: '创建时间',
					width: '15%',
					align: 'center'
				},
				{
					title: '操作',
					width: '25%',
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

	//批量新闻
	$(".delAll_btn").on("click", function() {
		var checkStatus = table.checkStatus('newsListTable'),
			data = checkStatus.data,
			ids = "";
		if (data.length > 0) {
			for (var i in data) {
				ids += data[i].id + ",";
			}
		}
		delNews(ids);


	});
	//搜索
	$(".search_btn").on("click", function() {
		tableIns.reload({
			page: {
				curr: 1 //重新从第 1 页开始
			},
			where: {
				title: $(".searchVal").val(), //搜索的关键字
				typeid: $("#typeid").val()
			}
		})
	});
	//跳转新增
	$(".addNews_btn").on("click", function() {
		window.location.href = webname + "/backsystem/news/addToNews";

	});

});



//跳转修改
function updateToNews(id) {
	window.location.href = webname + "/backsystem/news/updateToNews?id=" + id;
}
//删除
function delNews(ids) {
	layer.confirm("确实能够要删除吗？", {
		icon: 3,
		title: '提示'
	}, function() {
		$.post(webname + "/backsystem/news/deleteNews", {
			ids: ids
		}, function(d) {
			if (qjty(d)) {
				layer.msg(d.msg);
				setTimeout(() => {
					if (d.code > 0) {
						tableIns.reload({});
					}
				}, 1000);
			}
		})
	})
}
