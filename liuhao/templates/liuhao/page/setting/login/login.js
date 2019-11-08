layui.use(['form', 'layer', 'jquery'], function() {
	var form = layui.form,
		layer = parent.layer == undefined ? layui.layer : top.layer
		$ = layui.jquery;
		
        if(warn!=null && warn!='null' && warn!=''){
        	layer.msg(warn, { icon: 0,time: 4000});
        }
		
		setTimeout(function(){
			setInterval(() => {
				document.getElementById("codeImage").src=webname+"/captcha/kaptcha.jpg?"+Math.random();
			}, 60000);
		},60000);
		
	//登录按钮
	form.on("submit(login)", function(data) {
		$(this).text("登录中...").attr("disabled", "disabled").addClass("layui-disabled");
		var json = data.field;

		$.ajax({
			url: webname + "/backsystem/sysuser/sysLogin", //请求的url地址
			dataType: "json", //返回格式为json
			async: true, //请求是否异步，默认为异步，这也是ajax重要特性
			data: json, //参数值
			type: "POST", //请求方式
			beforeSend: function() {
				//请求前的处理
			},
			success: function(d) {
				//请求成功时处理
				if (d.code == 1) {
					/* console.log(d.object);
					var data = d.object;
					sessionStorage.setItem("user", JSON.stringify(data.user));
					sessionStorage.setItem("bigmenu", JSON.stringify(data.bigmenu)); */
					layer.msg(d.msg, {
						time: 2000
					});					
					setTimeout(() => {
  						window.location.href=webname+"/backsystem/main/toMain";
					}, 1000);


				} else if (d.exception != null) {
					layer.msg(d.exception, {
						time: 2000
					});
					$("#login").text("登录").removeAttr("disabled").removeClass("layui-disabled")
				} else {
					if (d.msg != null) {
						layer.msg(d.msg, {
							time: 2000
						});
					}
					$("#login").text("登录").removeAttr("disabled").removeClass("layui-disabled")

				}
			},
			error: function() {
				//请求出错处理
				layer.msg("服务器异常", {
					time: 2000
				});
			}
		});











		return false;
	})

	//表单输入效果
	$(".loginBody .input-item").click(function(e) {
		e.stopPropagation();
		$(this).addClass("layui-input-focus").find(".layui-input").focus();
	})
	$(".loginBody .layui-form-item .layui-input").focus(function() {
		$(this).parent().addClass("layui-input-focus");
	})
	$(".loginBody .layui-form-item .layui-input").blur(function() {
		$(this).parent().removeClass("layui-input-focus");
		if ($(this).val() != '') {
			$(this).parent().addClass("layui-input-active");
		} else {
			$(this).parent().removeClass("layui-input-active");
		}
	})
})
