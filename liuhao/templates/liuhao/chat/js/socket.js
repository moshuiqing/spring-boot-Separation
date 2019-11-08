
var layim;
var tiows;
layui.define(['jquery','contextMenu','layer'], function (exports) {
    var contextMenu = layui.contextMenu;
    var $ = layui.jquery;
    var layer = layui.layer;
    var cachedata =  layui.layim.cache(); 

    var im = {
    		config:function(options1,options2){
    			 layim = $.extend(layim, options1); //把layim继承出去，方便在register中使用
    			 tiows=$.extend(tiows, options2);
    		},
            contextMenu : function(){//定义右键操作
                var my_spread = $('.layim-list-friend >li');
                my_spread.mousedown(function(e){
                    var data = {
                        contextItem: "context-friend", // 添加class
                        target: function(ele) { // 当前元素
                            $(".context-friend").attr("data-id",ele[0].id.replace('layim-friend',"")).attr("data-name",ele.find("span").html());
                            $(".context-friend").attr("data-img",ele.find("img").attr('src')).attr("data-type",'friend');
                        },
                        menu:[]
                    };

                    data.menu.push(im.menuChat());
                    data.menu.push(im.menuInfo());
                    data.menu.push(im.menuChatLog());
                   // data.menu.push(im.menuNickName());
                    var currentGroupidx = $(this).find('h5').attr('groupidx');//当前分组id
                    if(my_spread.length >= 2){ //当至少有两个分组时
                        var html = '<ul>';
                        for (var i = 0; i < my_spread.length; i++) {
                            var groupidx = my_spread.eq(i).find('h5').attr('groupidx');
                            if (currentGroupidx != groupidx) {
                                var groupName = my_spread.eq(i).find('h5 span').html();
                                html += '<li class="ui-move-menu-item" data-groupidx="'+groupidx+'" data-groupName="'+groupName+'"><a href="javascript:void(0);"><span>'+groupName+'</span></a></li>'
                            };
                        };
                        html += '</ul>';
                        data.menu.push(im.menuMove(html));                
                    }
                    data.menu.push(im.menuRemove());
                    $(".layim-list-friend >li > ul > li").contextMenu(data);//好友右键事件
                });
                $(".layim-list-friend >li > h5").mousedown(function(e){
                    var data = {
                        contextItem: "context-mygroup", // 添加class
                        target: function(ele) { // 当前元素
                            $(".context-mygroup").attr("data-id",ele.attr('groupidx')).attr("data-name",ele.find("span").html());
                        },
                        menu: []                        
                    };             
                    data.menu.push(im.menuAddMyGroup());
                    data.menu.push(im.menuRename());
                    
                    if ($(this).parent().find('ul li').data('index') !== 0) {data.menu.push(im.menuDelMyGroup()); };
                                             
                $(this).contextMenu(data);  //好友分组右键事件          
                
                
                $(".ul-context-menu").on("mouseout",function(){
          		  console.log(1111)
          			
          			 
          		  });
            });


            $(".layim-list-group > li").mousedown(function(e){
                    var data = {
                        contextItem: "context-group", // 添加class
                        target: function(ele) { // 当前元素
                            $(".context-group").attr("data-id",ele[0].id.replace('layim-group',"")).attr("data-name",ele.find("span").html())
                            .attr("data-img",ele.find("img").attr('src')).attr("data-type",'group')   
                        },
                        menu: []                        
                    };             
                    data.menu.push(im.menuGroupChat());
                    data.menu.push(im.menuInfo());
                    data.menu.push(im.menuChatLog());
					data.menu.push(im.menuLeaveGroupBySelf());
                                             
                $(this).contextMenu(data);  //面板群组右键事件                                 
            });


          /*  $('.groupMembers > li').mousedown(function(e){//聊天页面群组右键事件
                var data = {
                    contextItem: "context-group-member", // 添加class
                    isfriend: $(".context-group-member").data("isfriend"), // 添加class
                    target: function(ele) { // 当前元素
                        $(".context-group-member").attr("data-id",ele[0].id.replace(/[^0-9]/ig,""));
                        $(".context-group-member").attr("data-img",ele.find("img").attr('src'));
                        $(".context-group-member").attr("data-name",ele.find("span").html());
                        $(".context-group-member").attr("data-isfriend",ele.attr('isfriend'));
                        $(".context-group-member").attr("data-manager",ele.attr('manager'));
                        $(".context-group-member").attr("data-groupidx",ele.parent().data('groupidx'));
                        $(".context-group-member").attr("data-type",'friend');
                    },
                    menu:[]
                    };    
                var _this = $(this);
                var groupInfo = conf.layim.thisChat().data;
                var _time = (new Date()).valueOf();//当前时间
                var _gagTime = parseInt(_this.attr('gagTime'));//当前禁言时间                  
                if (cachedata.mine.id !== _this.attr('id')) {
                    data.menu.push(im.menuChat()); 
                    data.menu.push(im.menuInfo());
                    if(3 == e.which && $(this).attr('isfriend') == 0 ){ //点击右键并且不是好友
                        data.menu.push(im.menuAddFriend())
                    }                                                   
                }else{
                    data.menu.push(im.menuEditGroupNickName());
                }                     
                if (groupInfo.manager == 1 && cachedata.mine.id !== _this.attr('id')) {//是群主且操作的对象不是自己
                    if (_this.attr('manager') == 2) {
                        data.menu.push(im.menuRemoveAdmin());
                    }else if (_this.attr('manager') == 3) {
                        data.menu.push(im.menuSetAdmin());
                    }    
                    data.menu.push(im.menuEditGroupNickName());
                    data.menu.push(im.menuLeaveGroup());
                    if (_gagTime < _time) {
                        data.menu.push(im.menuGroupMemberGag());
                    }else{
                        data.menu.push(im.menuLiftGroupMemberGag());
                    }    
                }*/
                
                //群主管理

//                layui.each(cachedata.group, function(index, item){                
//                    if (item.id == _this.parent().data('groupidx') && item.manager == 2 && _this.attr('manager') == 3 && cachedata.mine.id !== _this.attr('id')) {//管理员且操作的是群员
//                        data.menu.push(im.menuEditGroupNickName());
//                        data.menu.push(im.menuLeaveGroup());
//                        if (_gagTime < _time) {
//                            data.menu.push(im.menuGroupMemberGag());
//                        }else{
//                            data.menu.push(im.menuLiftGroupMemberGag());
//                        }
//                    }//管理员管理        
//                })
//                $(".groupMembers > li").contextMenu(data);    
//            })


        },        
        menuInfo: function(){
            return data =  {
                            text: "查看资料",
                            icon: "&#xe62a;",
                            callback: function(ele) {
                                var othis = ele.parent(),type = othis.data('type'),id = othis.data('id');
                                
                                im.getInformation({
                                    id: id,
                                    type:type
                                });                        
                            }
                        }         
        },
        menuChatLog: function(){
            return data =  {
                            text: "聊天记录",
                            icon: "&#xe60e;",
                            callback: function(ele) {
                            	 var othis = ele.parent(),
                                 friend_id = othis[0].dataset.id,
                                 friend_name = othis[0].dataset.name;
                            	 type = othis[0].dataset.type;

                                 var index = layer.open({
                                     type: 2
                                     ,maxmin: true
                                     ,title: '与 '+ friend_name +' 的聊天记录'
                                     ,area: ['450px', '600px']
                                     ,shade: false
                                     ,skin: 'layui-box'
                                     ,anim: 2
                                     ,id: 'layui-layim-chatlog'
                                     ,content: webname + '/chatUser/toRecord' + '?id=' + friend_id + '&type='+type
                                 });    
                            }
                        }       
        },               
        menuMove: function(html){
            return data = {
                            text: "移动联系人",
                            icon: "&#xe630;",
                            nav: "move",//子导航的样式
                            navIcon: "&#xe602;",//子导航的图标
                            navBody: html,//子导航html
                            callback: function(ele) {
                                var friend_id = ele.parent().data('id');//要移动的好友id
                                friend_name = ele.parent().data('name');
                                var avatar = ele.parent().data('img');
                                //var default_avatar = './uploads/person/empty2.jpg';
                                var signature = $('.layim-list-friend').find('#layim-friend'+friend_id).find('p').html();//获取签名
                                var item = ele.find("ul li");
                                item.hover(function() {
                                    var _this = item.index(this);
                                    var groupidx = item.eq(_this).data('groupidx');//将好友移动到分组的id
                                   $.post(webname+"/chatUser/movChatFriend",{userid:friend_id,Friendid:groupidx},function(res){
                                       var data = res;//eval('(' + res + ')');
                                        if (res.code > 0) {
                                            layim.removeList({//将好友从之前分组除去
                                                type: 'friend' 
                                                ,id: friend_id //好友ID
                                            });                                                          
                                            layim.addList({//将好友移动到新分组
                                                type: 'friend'
                                                , avatar: avatar //好友头像
                                                , username: friend_name //好友昵称
                                                , groupid: groupidx //所在的分组id
                                                , id: friend_id //好友ID
                                                , sign: signature //好友签名
                                            }); 
                                        }
                                        layer.msg(data.msg);
                                   });                                                                                                                                        
                                });
                            }
                        }     
        },
        menuRemove: function(){
            return data = {
                    text: "删除好友",
                    icon: "&#xe640;",
                    events: "removeFriends",
                    callback: function(ele) {
                        var othis = ele.parent(),friend_id = othis.data('id'),username,sign,avatar;
                        layui.each(cachedata.friend, function(index1, item1){
                            layui.each(item1.list, function(index, item){
                                if (item.id == friend_id) {
                                    username = item.username;
                                    sign = item.sign;
                                    avatar=item.avatar;
                                }
                            });
                        });
                        layer.confirm('删除后对方将从你的好友列表消失，您将无法发送消息给对方！<div class="layui-layim-list"><li layim-event="chat" data-type="friend" data-index="0"><img src="'+avatar+'"><span>'+username+'</span><p>'+sign+'</p></li></div>', {
                            btn: ['确定','取消'], //按钮
                            title:['删除好友','background:#b4bdb8'],
                            shade: 0
                        }, function(){
                           // im.removeFriends(friend_id); 
                            

                        	$.post(webname+"/chatUser/delChatFriendUid",{userid:friend_id},function(res){
                        		
                        		if(res.code>0){
                        		/*	  var index = layer.open();
                                      layer.close(index);*/
                        		
                        			
                        			layer.msg(res.msg);
                                      layim.removeList({//从我的列表删除
                                          type: 'friend' //或者group
                                          ,id: userid //好友或者群组ID
                                      });  
                                      im.removeHistory({//从我的历史列表删除
                                          type: 'friend' //或者group
                                          ,id: userid //好友或者群组ID
                                      });
                                      var  info = {
  	    	                    			type:'delFriend',
  	    	                    			toid:res.object.toid,
  	    	                    			sendid:res.object.sendid
  	    	                    				
  	    	                    	};
  	    	                    	 tiows.send(JSON.stringify(info));
                                      parent.location.reload();
                        		}else{
                        			layer.msg(res.msg);
                        		}
                        		
                        		
                        	})
                        
                        }, function(){
                            var index = layer.open();
                            layer.close(index);
                        });                                                    
                    }
                }         
    },
    menuAddMyGroup: function(){
        return  data =  { 
                        text: "添加分组",
                        icon: "&#xe654;",
                        callback: function(ele) {
                        	
                            im.addMyGroup();
                        }
                    }

    },        
    menuRename: function(){
        return  data =  {
                    text: "重命名",
                    icon: "&#xe642;",
                    callback: function(ele) {
                        var othis = ele.parent(),mygroupIdx = othis.data('id'),groupName = othis.data('name');
                        layer.prompt({title: '请输入分组名，并确认', formType: 0,value: groupName}, function(mygroupName, index){
                            if (mygroupName) {
                                $.post(webname+"/chatUser/upChatFriendName",{groupname:mygroupName,id:mygroupIdx},function(res){
                                    var data = res;//eval('(' + res + ')');
                                    if (data.code> 0) {
                                        var friend_group = $(".layim-list-friend li");
                                        for(var j = 0; j < friend_group.length; j++){
                                            var groupidx = friend_group.eq(j).find('h5').attr('groupidx');
                                            if(groupidx == mygroupIdx){//当前选择的分组
                                                friend_group.eq(j).find('h5').find('span').html(mygroupName);
                                            }
                                        }
                                        im.contextMenu();            
                                        layer.close(index);
                                    }
                                    layer.msg(data.msg);
                                });
                            }

                        });
                    }

                }
    },        
    menuDelMyGroup: function(){
        return  data =  { 
                    text: "删除该组",
                    icon: "&#x1006;",
                    callback: function(ele) {
                        var othis = ele.parent(),mygroupIdx = othis.data('id');
                        layer.confirm('<div style="float: left;width: 17%;margin-top: 14px;"><i class="layui-icon" style="font-size: 48px;color:#cc4a4a">&#xe607;</i></div><div style="width: 83%;float: left;"> 选定的分组将被删除，组内联系人将会移至默认分组。</div>', {
                            btn: ['确定','取消'], //按钮
                            title:['删除分组','background:#b4bdb8'],
                            shade: 0
                        }, function(){
                            im.delMyGroup(mygroupIdx); 
                        }, function(){
                            var index = layer.open();
                            layer.close(index);
                        });                      
                    }
                }
    		},  
		    addMyGroup: function(){//新增分组
		    	 layer.prompt({title: '请输入分组名，并确认', formType: 0}, function(mygroupName, index){
		    		 if(mygroupName){
			        $.post(webname+"/chatUser/insertFriendGroup", {groupname:mygroupName}, function (res) {
			            //var data = eval('(' + res + ')');
			        	var data = res;
			            if (data.code > 0) {
			                $('.layim-list-friend').append('<li><h5 layim-event="spread" lay-type="false" data-id="'+data.object.id+'"><i class="layui-icon">&#xe602;</i><span>'+data.object.groupname+'</span><em>(<cite class="layim-count"> 0</cite>)</em></h5><ul class="layui-layim-list"><span class="layim-null">该分组下暂无好友</span></ul></li>');
			                im.contextMenu();
			                location.reload();
			            }else{
			                layer.msg(data.msg);
			            }
			        }); 
		    		 }
		    	 });
		    },
	        delMyGroup: function(groupidx){//删除分组
   			 var group = $('.layim-list-friend li') || [];
   			 var id=group.eq(0).find('h5').attr('groupidx');//默认分组

	            $.post(webname+"/chatUser/reomveChatFriend", {id:groupidx,firstid:id}, function (res) {
	                var data = res;//eval('(' + res + ')');
	                if (data.code >0) {
	                    for(var j = 0; j < group.length; j++){//遍历每一个分组
	                        groupList = group.eq(j).find('h5').attr('groupidx');
	                        if(groupList == groupidx){//要删除的分组
	                            if (group.eq(j).find('ul li').hasClass('layim-null')) {//删除的分组下没有好友
	                                group.eq(j).remove();
	                            }else{
	                                // var html = group.eq(j).find('ul').html();//被删除分组的好友
	                                var friend = group.eq(j).find('ul li');
	                                var number = friend.length;//被删除分组的好友个数
	                                for (var i = 0; i < number; i++) {
	                                    var friend_id = friend.eq(i).attr('id').replace(/^layim-friend/g, '');//好友id
	                                    var friend_name = friend.eq(i).find('span').html();//好友id
	                                    var signature = friend.eq(i).find('p').html();//好友id
	                                    var avatar = friend.eq(i).find('img').attr('src');
	                                                      
	                                    layim.removeList({//将好友从之前分组除去
	                                        type: 'friend' 
	                                        ,id: friend_id //好友ID
	                                    });                                                          
	                                    layim.addList({//将好友移动到新分组
	                                        type: 'friend'
	                                        , avatar: avatar //好友头像
	                                        , username: friend_name //好友昵称
	                                        , groupid:  group.eq(0).find('h5').attr('groupidx') //将好友添加到默认分组
	                                        , id: friend_id //好友ID
	                                        , sign: signature //好友签名
	                                    });   
	                                    
	                                    
	                                };
	                            }

	                        }
	                    }
	                    im.contextMenu();                    
	                    layer.close(layer.index);
	                    layer.msg(data.msg);
	                  
	                    
	                }else{
	                    layer.msg(data.msg);
	                }
	            }); 
	        },  
	        getInformation: function(data){ //查看好友资料
	            var id = data.id || {},type = data.type || {};
	            var index = layer.open({
	                type: 2
	                ,title: type  == 'friend'?(cachedata.mine.id == id?'我的资料':'好友资料') :'群资料'
	                ,shade: false
	                ,maxmin: false
	                // ,closeBtn: 0
	                ,area:type  == 'friend'? ['400px', '670px'] :['300px','400px']
	                ,skin: 'layui-box layui-layer-border'
	                ,resize: true
	                ,content: cachedata.base.Information+'?id='+id+'&type='+type
	            });           
	        },
	        menuChat: function(){
	            return data = {
	                            text: "发送消息",
	                            icon: "&#xe63a;",
	                            callback: function(ele) {
	                            	  var othis = ele.parent(),
	                                  friend_id = othis[0].dataset.id.replace(/^layim-friend/g, ''),
	                                  friend_name = othis[0].dataset.name,
	                                  friend_avatar = othis[0].dataset.img;
	                              layim.chat({
	                                  name: friend_name
	                                  ,type: 'friend'
	                                  ,avatar: friend_avatar
	                                  ,id: friend_id
	                              });
	                            }
	                        }       
	        },  
	        menuGroupChat: function(){
	            return data = {
	                            text: "发送消息",
	                            icon: "&#xe63a;",
	                            callback: function(ele) {
	                            	  var othis = ele.parent(),
	                                  friend_id = othis[0].dataset.id.replace(/^layim-friend/g, ''),
	                                  friend_name = othis[0].dataset.name,
	                                  friend_avatar = othis[0].dataset.img;
	                              layim.chat({
	                                  name: friend_name
	                                  ,type: 'group'
	                                  ,avatar: friend_avatar
	                                  ,id: friend_id
	                              });
	                            }
	                        }       
	        },
	        menuLeaveGroupBySelf: function(){
	            return  data =  {
	                        text: "退出该群",
	                        icon: "&#xe613;",
	                        callback: function(ele) {
	                            var othis = ele.parent(),
	                                group_id = othis.data('id'),  
	                                groupname = othis.data('name'),
	                                avatar = othis.data('img');
									console.log(othis);
									
	                            layer.confirm('您真的要退出该群吗？退出后你将不会再接收此群的会话消息。<div class="layui-layim-list"><li layim-event="chat" data-type="friend" data-index="0"><img src="'+avatar+'"><span>'+groupname+'</span></li></div>', {
	                                btn: ['确定','取消'], //按钮
	                                title:['提示','background:#b4bdb8'],
	                                shade: 0
	                            }, function(){
	                                var user = cachedata.mine.id;
	                                var username = cachedata.mine.username;
	                                im.leaveGroupBySelf(user,groupname,group_id);  
	                            }, function(){
	                                var index = layer.open();  
	                                layer.close(index);
	                            }); 
	                        }
	                    }
	        },
	        //申请添加好友或者加群
	        addFriendGroup:function(othis,type,userid){
	            var li = othis.parents('li') || othis.parent()
	                    , uid = li.data('uid') || li.data('id')
	                    , name = li.data('name')
	                    ,avatar=li.data('avatar')
	                    ,sign=li.data("sign");
	            if (uid == 'undifine' || !uid) {
	                var uid = othis.parent().data('id'), name = othis.parent().data('name');
	            }
	           
	            var isAdd = false;
	            if (type == 'friend') {
	               
	                if(cachedata.mine.id == uid){//添加的是自己
	                    layer.msg('不能添加自己');
	                    return false;
	                }
	                layui.each(cachedata.friend, function(index1, item1){
	                    layui.each(item1.list, function(index, item){
	                        if (item.id == uid) {isAdd = true; }//是否已经是好友
	                    });
	                }); 
	                
	                if(isAdd){
		            	layer.msg('已经是好友！');
		            	return false;
		            }
	            }else{
	                
	                for (i in cachedata.group)//是否已经加群
	                {
	                    if (cachedata.group[i].id == uid) {isAdd = true;break;}
	                }
	                
	                if(isAdd){
		            	layer.msg('已经是该群成员！');
		            	return false;
		            }
	            }
	            
	         
	            
	            parent.layui.layim.add({//弹出添加好友对话框
	                isAdd: isAdd
	                ,username: name || []
	                ,uid:uid
	                ,avatar: avatar
	                ,group:  cachedata.friend || []
	                ,type: type
	                ,submit: function(group,remark,index){//确认发送添加请求
	                    if (type == 'friend') {
	                    	
	                        $.post(webname+"/chatUser/getSendInfo", {from:userid,uid: uid,type:1,remark:remark,from_group:group,content:"申请添加你为好友"}, function (res) {
	                            var data = eval('(' + res + ')');
	                            
	                            if (data.code > 0) {
	                            	var  info = {
	    	                    			type:'apply',
	    	                    			toid:uid,
	    	                    			usertype:'friend'
	    	                    				
	    	                    	};
	    	                    	 tiows.send(JSON.stringify(info));
	
	                                layer.msg('你申请添加'+name+'为好友的消息已发送。请等待对方确认');
	                            }else if(data.code==-3){
	                            	 layer.msg(data.msg);
	                            }else{
	                                layer.msg('你申请添加'+name+'为好友的消息发送失败。请刷新浏览器后重试');
	                            }
	                        });
	                        layer.close(layer.index);
	                    }else{
	                    	//uid 是群id
                                $.post(webname+"/chatUser/applyGroup", {from:userid,from_group:uid,type:2,remark:remark,content:"申请加入"+name+""}, function (res) {
                                    
                                    if (res.code > 0) {                    
         	                            	var  info = {
         	    	                    			type:'apply',
         	    	                    			toid:res.object,
         	    	                    			usertype:'friend'
         	    	                    				
         	    	                    	};
         	    	                    	 tiows.send(JSON.stringify(info));
                                    	
                                        layer.msg('你申请加入'+name+'的消息已发送。请等待群主确认');
                                    }else if(res.code==-3){
                                    	 layer.msg(res.msg);
                                    }else{
                                        layer.msg('你申请加入'+name+'的消息发送失败。请刷新浏览器后重试');
                                    }
                                   
                                });                                      
                                layer.close(layer.index);               
	                    }
	                },function(){
	                    layer.close(index);
	                }
	            });            

	        },
	        notice:function(obj,type,toid){	        	
	        	var content=obj.username+" 已经同意了你的好友请求";	        	
	        	//产生系统消息
	        	$.post(webname+"/chatUser/getSendInfo",{uid:toid,content:content,type:3},function(res){
	        		 if(res.code<0){
	        			 layer.msg('系统错误');
	        		 }else{
	        			 
	        			 var  info = {
	                 			type:type,
	                 			toid:toid,
	                 			send:obj,            				
	                 	};	        	
	     	        	tiows.send(JSON.stringify(info));//发送
	        			 
	        			 layer.msg('添加好友成功！');
	        		 }
	        	})
	        	
	        },
	        refused:function(username,toid,type,content){
	
	        	//产生系统消息
	        
	        	$.post(webname+"/chatUser/getSendInfo",{uid:toid,content:content,type:3},function(res){
	        		 if(res.code<0){
	        			 layer.msg('系统错误');
	        		 }else{
	        	        	//拒绝
	     	        	var  info = {
	                 			type:"systemagree",
	                 			toid:toid,   
	                 			send:{type:type}
	                 	};	 	
	     	        	tiows.send(JSON.stringify(info));//发送
	        			 layer.msg('拒绝成功！');
	        		 }
	        		
	        	})
	        	
	        },
	        noticeGroup:function(fromgroup,toid,usernam){
	        	//同意加群
	        	var content=username+" 已经同意你加入群聊";	   
	        	$.post(webname+"/chatUser/getSendInfo",{uid:toid,content:content,type:3},function(res){
	        		   var data = eval('(' + res + ')');
	        		
	        		if(data.code>0){
	        			console.log(data.code);
	        			 
	        			 //把用户加入到群里返回群的信息
	        			 $.post(webname+"/chatUser/insertUserToGroup",{fromgroup,toid},function(res){
	        				 var send={type:"friend"}; 
	        				 var  info = {
			                 			type:"systemagree",
			                 			toid:toid,
			                 			send:send,
			                 			group:res.object
			                 	};	
	        				 
	        				 tiows.send(JSON.stringify(info));//发送
	        			 })
		     	        	
	        			
	        			
	        		}else if(data.code==-3){
	        			layer.msg(res.msg);
	        		}else{
	        			 layer.msg('系统错误');
	        		}
	        		
	        	});
	        	
	        },
	        removeHistory: function(data){//删除好友或退出群后清除历史记录           
	            var history = cachedata.local.history;
					if(history!=null){
		            delete history[data.type+data.id];
		            cachedata.local.history = history;
		            layui.data('layim', {
		              key: cachedata.mine.id
		              ,value: cachedata.local
		            });
		            $('#layim-history'+data.id).remove();
		            var hisElem = $('.layui-layim').find('.layim-list-history');
		            var none = '<li class="layim-null">暂无历史会话</li>'        
		            if(hisElem.find('li').length === 0){
		              hisElem.html(none);
		            }   
					}
	        },
	        leaveGroupBySelf: function (to,groupname,roomId) {
	        	
	        	$.post(webname+"/chatUser/getChatGroupInfo",{id:roomId},function(d){
	        		
	        		
	        		if(d.object){
                          layer.confirm('由于您是群主，退出后群自动解散，您确定要解散吗？', {
                              btn: ['确定','取消'], //按钮
                              title:['提示','background:#b4bdb8'],
                              shade: 0
                          }, function(){
                        	//是群主解散
     	        			 $.post(webname+"/chatUser/dissolution", {id:roomId,groupname:groupname}, function (res) {
     	        				 layer.msg(res.msg);
     	        				if(res.code>0){
     	        					//发送通知
     	        					 var  info = {
     			                 			type:"systemGroupRefund",
     			                 			groupid:roomId,
     			                 	};	
     	        				 
     	        				 tiows.send(JSON.stringify(info));//发送
     	        					
     	        				}
     	        				 
     	        			 });

                          }, function(){
                              var index = layer.open();  
                              layer.close(index);
                          }); 
	        			
	        		}else{
	        			//不是群主
	        			//退群
	    	            $.post(webname+"/chatUser/refundGroup", {groupid:roomId,userid:to,groupname:groupname}, function (res) {
	    	                var data = res;//eval('(' + res + ')');
	    	                layer.msg(data.msg);
	    	                if (data.code > 0) {
	    	                	
	    	                	 layim.removeList({
	                                 type: 'group' //或者group
	                                 ,id: roomId //好友或者群组ID
	                             });
	                             im.removeHistory({//从我的历史列表删除
	                                 type: 'group' //或者group
	                                 ,id: roomId //好友或者群组ID
	                             });
	                            
	                             
	    	                }
	    	         
	    	            });  
	        		}
	        		
	        		
	        		
	        	})
	        	                            
	        }, 
	      
        
    };
                







  exports('socket', im);
}); 