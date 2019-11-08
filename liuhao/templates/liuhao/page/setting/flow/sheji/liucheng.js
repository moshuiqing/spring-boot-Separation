var tableIns;
var form;
var layer;
//JavaScript代码区域
layui.use(['table','element','form','layer','laypage'], function(){
    var table = layui.table
    	,element = layui.element
       ,laypage = layui.laypage;
    	layer = layui.layer;
    	form = layui.form;
});



//进入流程字典
$("#dictionary").click(function(){
	
	window.location.href=webname+"/backsystem/flow/proDictionary?flowId="+the_flow_id;
})

$("#back").click(function(){
	window.location.href=webname+"/backsystem/flow/flowList";
})




var _canvas;
		function huidiao(json) {

			_canvas.upProcess(json);
		}

		$(function() {

			var alertModal = $('#alertModal'), attributeModal = $("#attributeModal");

			//消息提示
			mAlert = function(messages, s) {

				if (!messages)
					messages = "";
				if (!s)
					s = 2000;
				alertModal.find(".modal-body").html(messages);
				alertModal.modal('toggle');

				setTimeout(function() {
					alertModal.modal("hide")
				}, s);
			}

			//属性设置
			attributeModal.on("hidden", function() {
				$(this).removeData("modal");//移除数据，防止缓存
			});
			ajaxModal = function(url, fn) {

				url += url.indexOf('?') ? '&' : '?';
				url += 'time=' + new Date().getTime();

				attributeModal.find(".modal-body").html('<img src="/liuhao/Public/images/loading.gif"/>');
				attributeModal.modal({
					remote : url
				});

				//加载完成执行
				if (fn) {

					attributeModal.on('shown', fn);

				}

			}
			//刷新页面
			function page_reload() {
				location.reload();
			}

			/*
			
			js 命名习惯：首字母小写 + 其它首字线大写
			 */
			/*步骤数据*/
			var aa = {
				"flowId" : "4",
				"icon" : "icon-ok",
				"id" : "61",
				"process_name" : "呵呵呵呵",
				"process_to" : "63,64",
				"style" : "width:121px;height:41px;line-height:41px;color:#0e76a8;left:193px;top:132px;"
			};

			//var processData = ${remap.data};

			/*创建流程设计器*/
			_canvas = $("#flowdesign_canvas").Flowdesign(
							{
								"processData" : processData,
								mtAfterDrop : function(params) {

									//alert("连接："+params.sourceId +" -> "+ params.targetId);
									//连线的时候直接保存
									var url=webname+"/backsystem/flow/saveLianJieXian";
									$.post(url, {
										"flowId" : the_flow_id,
										"id" : params.sourceId ,
										"proto" : params.targetId
									}, function(data) {
										if(data=="2"){
											layer.msg('无需重复连接！',{icon:2,time:1000});
										}
										
									});
									
									
								}
								/*画面右键*/
								,
								canvasMenus : {
									"cmAdd" : function(t) {

										var mLeft = $("#jqContextMenu").css(
												"left"), mTop = $(
												"#jqContextMenu").css("top");
										mLeft = mLeft.replace("px", "");
										mTop = mTop.replace("px", "");

										/*重要提示 start*/
										//alert("这里使用ajax提交，请参考官网示例，可使用Fiddler软件抓包获取返回格式11111");
										/*重要提示 end */

										var url = webname
												+ "/backsystem/flow/addJdx";
										$.post(url, {
											"flowId" : the_flow_id,
											"setleft" : mLeft,
											"settop" : mTop
										}, function(data) {

											if (data.code == 1) {
												//mAlert("添加成功！");
												layer.msg('添加成功！',{icon:6,time:1000});
												_canvas.addProcess(data.object);

											} else {
												//mAlert("添加失败！");
												layer.msg('添加失败！',{icon:5,time:1000});
											}

										}, 'json');

									},
									"cmSave" : function(t) {
										var processInfo = _canvas
												.getProcessInfo();//连接信息

										/*重要提示 start*/
										//alert("这里使用ajax提交，请参考官网示例，可使用Fiddler软件抓包获取返回格式");
										/*重要提示 end */

										var url = webname
												+ "/backsystem/flow/saveLfp";

										$.post(url, {
											"flowId" : the_flow_id,
											"processinfo" : processInfo
										}, function(data) {
											if(data==1){
												layer.msg("保存成功！",{icon:6,time:1000});
											}else{
												layer.msg("保存失败！",{icon:6,time:1000});
											}
											//mAlert(data);
										});
									},
									//刷新
									"cmRefresh" : function(t) {
										location.reload();//_canvas.refresh();
									},
									"cmPaste" : function(t) {
										var pasteId = _canvas.paste();//右键当前的ID
										if (pasteId <= 0) {
											alert("你未复制任何步骤");
											return;
										}
										alert("粘贴:" + pasteId);
									},
									"cmHelp" : function(t) {
										layer.alert('<span>1、单击节点查看备注</span></br><span>2、双击节点打开属性</span></br><span>3、点击Enter键视为点击确定按钮</span>'
												+'</br><span>4、点击Esc视为点击取消按钮</span>', {title:'流程操作说明'})
									}

								}
								/*步骤右键*/
								,
								processMenus : {

									"pmCopy" : function(t) {
										//var activeId = _canvas.getActiveId();//右键当前的ID
										_canvas.copy();//右键当前的ID
										alert("复制成功");
									},
									"pmDelete" : function(t) {
										 layer.confirm('您确定要删除这条数据吗？',{title:"提示",icon: 7, btn: ['确定',  '取消'] },function(){
										  	     
											var activeId = _canvas.getActiveId();//右键当前的ID

											var url = webname
													+ "/backsystem/flow/deleteNode";
											$.post(
															url,
															{
																"flowId" : the_flow_id,
																"id" : activeId
															},
															function(data) {
																if (data == "1") {
																	jsPlumb.removeAllEndpoints(t);
																	//清除步骤
																	_canvas.delProcess(activeId);
																	//mAlert("删除成功！");
																	layer.msg('删除成功！',{icon:6,time:1000});
																}else{
																	layer.msg('删除失败！',{icon:5,time:1000});
																}
																//mAlert("删除失败！");
																
															}, 'json');
										});
									},
									"pmAttribute" : function(t) {
										var activeId = _canvas.getActiveId();//右键当前的ID
										var mLeft = $(t).css("left").replace("px", ""), 
										mTop = $(t).css("top").replace("px", "");
										var url = "shuxin?id="
												+ activeId + "&flowId="
												+ the_flow_id + "&flag=" + 0
												+ "&setleft=" + mLeft
												+ "&settop=" + mTop;

										ajaxModal(url, function(data) {
											//alert('加载完成执行')
											if (data == "-1") {
												layer.msg('系统错误',{icon:5,time:1000});
											}
										});
									},
									"pmForm" : function(t) {
										var activeId = _canvas.getActiveId();//右键当前的ID
										var mLeft = $(t).css("left").replace("px", ""), 
										mTop = $(t).css("top").replace("px", "");
										/*重要提示 start*/
										//alert("这里使用ajax提交，请参考官网示例，可使用Fiddler软件抓包获取返回格式");
										/*重要提示 end */

										var url = "shuxin?id="
												+ activeId + "&flow_id="
												+ the_flow_id + "&flag=" + 1
												+ "&setleft=" + mLeft
												+ "&settop=" + mTop;
										ajaxModal(url, function() {
											//alert('加载完成执行')
										});
									},
									"pmSetting" : function(t) {
										var activeId = _canvas.getActiveId();//右键当前的ID
										var mLeft = $(t).css("left").replace("px", ""), 
										mTop = $(t).css("top").replace("px", "");
										
										var url = "shuxin?id="
												+ activeId + "&flowId="
												+ the_flow_id + "&flag=" + 5
												+ "&setleft=" + mLeft
												+ "&settop=" + mTop;

										ajaxModal(url, function() {
											//alert('加载完成执行')
										});
									},
									"pmMark" : function(t) {
										var activeId = _canvas.getActiveId();//右键当前的ID
										var mLeft = $(t).css("left").replace("px", ""), 
										mTop = $(t).css("top").replace("px", "");

										//var url = "/index.php?s=/Flowdesign/attribute/op/style/id/"+activeId+".html";
										var url = "shuxin?id="
												+ activeId + "&flowId="
												+ the_flow_id + "&flag=" + 6
												+ "&setleft=" + mLeft
												+ "&settop=" + mTop;
										ajaxModal(url, function() {
											//alert('加载完成执行')
										});
									}
								
								},
								fnRepeat : function() {
									//alert("步骤连接重复1");//可使用 jquery ui 或其它方式提示
									//mAlert("步骤连接重复了，请重新连接");
									layer.msg('步骤连接重复了，请重新连接',{icon:2,time:1000});

								},
								fnClick : function() {
									var activeId = _canvas.getActiveId();
									//mAlert("查看步骤信息 " + activeId);
									
									$.post( webname + "/backsystem/flow/getRark",{id:activeId,flowId:the_flow_id},function(msg){
										layer.alert(msg,{title:'备注'});
									})
									
								},
								fnDbClick : function(t) {
									//和 pmAttribute 一样
									var activeId = _canvas.getActiveId();//双击当前的ID
									
									var mLeft = $(t).css("left").replace("px", ""), 
									mTop = $(t).css("top").replace("px", "");
									var url = "shuxin?id="
											+ activeId + "&flowId="
											+ the_flow_id + "&flag=" + 0
											+ "&setleft=" + mLeft
											+ "&settop=" + mTop;


								ajaxModal(url, function() {
									//alert('加载完成执行')
								});
								}	
							});

			/*保存*/
			$("#leipi_save").bind('click', function() {
				var processInfo = _canvas.getProcessInfo();//连接信息

				/*重要提示 start*/
				//alert("这里使用ajax提交，请参考官网示例，可使用Fiddler软件抓包获取返回格式");
				/*重要提示 end */

				var url = webname + "/backsystem/flow/saveLfp";

				$.post(url, {
					"flowId" : the_flow_id,
					"processinfo" : processInfo
				}, function(data) {
					if(data=='1'){
						layer.msg("保存成功！",{icon:6,time:1000});
					}else{
						layer.msg("保存失败！",{icon:5,time:1000});
					}
					
					//mAlert(data);
				});
			});
			/*清除*/
			$("#leipi_clear").bind('click', function() {
				if (_canvas.clear()) {
					//alert("清空连接成功");
					//mAlert("清空连接成功，你可以重新连接");
					layer.msg('清空连接成功，你可以重新连接',{icon:6,time:1000});
				} else {
					//alert("清空连接失败");
					//mAlert("清空连接失败");
					layer.msg('清空连接失败',{icon:5,time:1000});
				}
			});

		});
		
		   document.onkeyup = function (event) {
	            var e = event || window.event;
	            var keyCode = e.keyCode || e.which;
	            switch (keyCode) {
	                case 13:
	                	$(".layui-layer-btn0").click();
	                    break;
	                
	                default:
	                    break;
	            }
	        }
