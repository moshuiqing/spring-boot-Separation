var index;
var layer;
var tableIns;
var form;
layui.use(['form', 'layer', 'table', 'laytpl'], function() {
	var $ = layui.jquery,
		laytpl = layui.laytpl,
		table = layui.table;
		layer = layui.layer;
		form = layui.form;
		
		//用户列表
		tableIns = table.render({
			elem: '#webuserList',
			url: webname + '/backsystem/main/foundWebUsers',
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
						width: '15%',
						align: "center"
					},
					{
						field: 'isdisable',
						title: '用户状态',
						width: '10%',
						align: 'center',
						templet: function(d) {
							return d.isdisable == "0" ? "正常使用" : "禁止使用";
						}
					},
					{
						field: 'userEndTime',
						title: '最后登录时间',
						width: '30.2%',
						align: 'center'
					},
					{
						title: '操作',
						width: '35%',
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
			]
		});
		///////////////////////////////////////////////////
		  //自定义验证规则  
		  form.verify({  
				phone: [/^1[3|4|5|7|8]\d{9}$/, '手机必须11位，只能是数字！']  
		        ,email: [/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/, '邮箱格式不对']
				,pwd:[/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$/, '密码格式不对']
		  });  
		
		///////////////////////////////
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
		
		
		
		///////////////////////////////		
		//提交监听
		form.on('submit(submit)', function(d) {
			var param = d.field;
			
			if(param.copyName==param.userName){
				param.userName=null;
			}
			var url = "/backsystem/main/addWebUser";
			if(param.id!=null && param.id!=''){
				url = "/backsystem/main/updatewebUser";
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
		
		
		//////////////////////
		
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
					$.post(webname + "/backsystem/main/deleteWebUsers", {
						strid: uids
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


});

	//添加用户
	function addUser(edit) {
		
		//给表单赋值
		form.val("tanchuang", { 
		 "id":"",
		 "copyName":"",
		 "userName":"",
		 "password":""
		});
		form.render();
		index = layer.open({
			title: '添加前台用户', //标题
			type: 1,
			area: ['580px', '350px'],
			offset: 'auto',
			shadeClose: false, //点击遮罩关闭
			content: $('#tc'),
			skin: 'show'
		});
		
	}
	$(".addNews_btn").click(function() {
		addUser();
	})
	
	//////////////
	//打开编辑弹窗
	function update(id,name) {
		
		//给表单赋值
		form.val("tanchuang", { 
		 "id":id,
		 "copyName":name,
		 "userName":name,
		 "password":""
		});
		form.render();
		
		index = layer.open({
			title: '修改前台用户', //标题
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
			isdisable: state
		};
		$.post(webname + "/backsystem/main/updateChange", param, function(d) {
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
			$.post(webname + "/backsystem/main/deletewebUser", {
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
	
	
