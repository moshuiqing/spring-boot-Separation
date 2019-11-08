layui.config({
    base : webname+"/js/"
}).use(['form','jquery',"address"],function() {
    var form = layui.form,
        $ = layui.jquery,
        address = layui.address;

      //判断是否设置过头像，如果设置过则修改顶部、左侧和个人资料中的头像，否则使用默认头像
	  var face = window.sessionStorage.getItem('userFace');
     if(face!=null && face !="null" && face!=''){
        $("#userFace").attr("src",webip+window.sessionStorage.getItem('userFace'));
        $(".userAvatar", window.parent.document).attr('src', webip+window.sessionStorage.getItem('userFace'));
    }else{
        $("#userFace").attr("src",webip+webname+"/images/face.jpg");
    }
	

})