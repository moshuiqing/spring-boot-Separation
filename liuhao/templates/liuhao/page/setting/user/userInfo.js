var form, $, areaData;
layui.config({
	base: webname + "/js/"
}).extend({
	"address": "address"
})
layui.use(
	['form', 'layer', 'upload', 'laydate', "address"],
	function() {
		form = layui.form;
		$ = layui.jquery;
		var layer = parent.layer === undefined ? layui.layer :
			top.layer,
			upload = layui.upload,
			laydate = layui.laydate,
			address = layui.address;

		// 上传头像
		upload.render({
			elem: '.userFaceBtn',
			url: webname + '/ftpup/uploadFile',
			method: "post", // 此处是为了演示之用，实际使用中请将此删除，默认用post方式提交
			field: 'file',
			accept: 'images',
			data: {
				INDEX: 0
			},
			size: '2048',
			done: function(res, index, upload) {
				if (res.code > 0) {

					var headImg = res.object;
					var param = {
						headimg: headImg,
						id: $("#pid").val()
					};

					//保存头像
					$.post(webname + "/backsystem/main/insertSysUser", param, function(d) {
						if (qjty(d)) {
							if (d.code > 0) {
								$('#userFace').attr('src', d.object.person.headimg);
								$(".userAvatar", window.parent.document).attr('src', d.object.person.headimg);
								window.sessionStorage.setItem('userFace',d.object.person.headimg);
								$("#pid").val(d.object.person.id);
								setTimeout(function() {
									layer.close(index);
									layer.msg("修改成功！");
								}, 2000);
							} else if (d.code < 0) {
								setTimeout(function() {
									layer.close(index);
									layer.msg(d.msg);
								}, 2000);
							} else {
								layer.close(index);
								layer.msg("刷新后重试");
							}
						}


					})


				}

			}
		});

		// 添加验证规则
		form.verify({
				userBirthday: function(value) {
					if (!
						/^(\d{4})[\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|1[0-2])([\u4e00-\u9fa5]|[-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/
						.test(value)) {
						return "出生日期格式不正确！";
					}
				}
			})
		// 选择出生日期
		laydate
			.render({
				elem: '.userBirthday',
				format: 'yyyy年MM月dd日',
				trigger: 'click',
				max: 0,
				mark: {
					"0-12-15": "生日"
				},
				done: function(value, date) {
					if (date.month === 12 && date.date === 15) { // 点击每年12月15日，弹出提示语
						layer.msg('今天是马哥的生日，也是layuicms2.0的发布日，快来送上祝福吧！');
					}
				}
			});


		// 提交个人资料
		form.on("submit(changeUser)", function(data) {
			console.log(data);

			var index = layer.msg('提交中，请稍候', {
				icon: 16,
				time: false,
				shade: 0.8
			});
			// 将填写的用户信息存到session以便下次调取
			var key, userInfoHtml = '';
			userInfoHtml = {
				'id': $("#pid").val(),
				'name': $(".realName").val(),
				'gender': data.field.sex,
				'phone': $(".userPhone").val(),
				'birthday': $(".userBirthday").val(),
				'city': $(".city").val(),
				'email': $(".userEmail").val(),
				'myself': $(".myself").val()
			};
			/*
			 * console.log(userInfoHtml); console.log(data);
			 */
			/*
			 * for(key in data.field){ if(key.indexOf("like") !=
			 * -1){ userInfoHtml[key] = "on"; } }
			 */
			$.post(webname + "/backsystem/main/insertSysUser", userInfoHtml,
				function(d) {
					if (qjty(d)) {
						if (d.code > 0) {
							$("#pid").val(d.object.person.id);
							window.sessionStorage.setItem("userInfo", JSON
								.stringify(d.object));
							setTimeout(function() {
								layer.close(index);
								layer.msg("提交成功！");
								window.location.reload();

							}, 2000);

						} else if (d.code < 0) {
							setTimeout(function() {
								layer.close(index);
								layer.msg(d.msg);
							}, 2000);
						} else {
							layer.close(index);
							layer.msg("刷新后重试");
						}
					}
				})





			return false; // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
		})

		// 修改密码
		form.on("submit(changePwd)", function(data) {
			var index = layer.msg('提交中，请稍候', {
				icon: 16,
				time: false,
				shade: 0.8
			});
			setTimeout(function() {
				layer.close(index);
				layer.msg("密码修改成功！");
				$(".pwd").val('');
			}, 2000);
			return false; // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
		})
	})
