
// <![CDATA[
var webname = "[[${application.webname}]]";
alert(webname)
var webip="[[${application.webip}]]";
var ftpSource="[[${application.ftpScource}]]";
/////////////////////////////////////////////////////
var tcpPort = "[[${application.tcpPort}]]";

 // ]]>



function qjty(d){
	var flag = true;
 	if(d.code==-4){
		layer.alert(d.msg,{icon:7,title:'警告'},function(){
			if(window.parent!=window){
				parent.window.location.reload();
			}else{
				window.location.reload();
			}
			
		
		})
		flag=false;
	}else  if(d.exception != null) {
			layer.msg(d.exception);
			flag=false;
	}
	return flag;
}
