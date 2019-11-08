//确定保存时调用的方式//
function saveAttribute(data)
{
	
    try{
        if(data=="保存失败！")
        {
        	  layer.msg(data,{icon:5,time:1000});//失败时不关闭 attributeModal 所以用 alert
        }else
        {
            $("#attributeModal").modal("hide");
            layer.msg(data,{icon:6,time:1000});
            //mAlert(data);
            //刷新加载样式，体验不太好 万一未保存设计
            //location.reload();
        }
    }catch(e)
    {
        alert(data.msg);
    }
}





   
 


$(function(){
	
//TAB
    $('#attributeTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
      if($(this).attr("href")=='#attrJudge')
      {
          //加载下一步数据 处理 决策项目 
    	 
      }
    })

  //步骤类型
  $('input[name="processtype"]').on('click',function(){
     
      if($(this).val()=='is_child')
      {
          $('#current_flow').hide();
          $('#child_flow').show();
      }else
      {
          $('#current_flow').show();
          $('#child_flow').hide();
      }
  });
  //返回步骤
  $('input[name="child_after"]').on('click',function(){
     
      if($(this).val()==2)
      {
          $("#child_back_id").show();
      }else
      {
          $("#child_back_id").hide();
      }
  });
  
    //步骤select 2
  $('#process_multiple').multiselect2side({
      selectedPosition: 'left',
      moveOptions: true,
      labelTop: '最顶',
      labelBottom: '最底',
      labelUp: '上移',
      labelDown: '下移',
      labelSort: '排序',
      labelsx: '<i class="icon-ok"></i> 下一步节点',
      labeldx: '<i class="icon-list"></i> 备选节点',
      autoSort: false,
      autoSortAvailable: true,
      minSize: 7
    });







  /*样式*/
  $('.colors li').click(function() {
      var self = $(this);
      if (!self.hasClass('active')) {
        self.siblings().removeClass('active');
      }
      var color = self.attr('org-data') ? self.attr('org-data') : '';

     
      var parentDiv = self.parents(".colors");
      var orgBind = parentDiv.attr("org-bind");
      if(orgBind == 'style_icon')
      {
          /*$("#"+orgBind).css({ color:'#fff',background: color });*/
          $("#"+orgBind).val(color);
          $("#style_icon_preview").attr("class",color + " icon-white");
      }else//颜色
      {
          $("#"+orgBind).css({ color:'#fff',background: color });
          $("#"+orgBind).val(color);
      }
      self.addClass('active');
  });




/*  //表单提交前检测
  $("#flow_attribute").submit(function(){
	  
     
  });*/

   //条件设置
/*  fnSetCondition();*/

});