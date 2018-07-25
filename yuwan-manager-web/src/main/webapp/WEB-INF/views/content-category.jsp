<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">
    </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
//页面加载完成后执行以下逻辑
$(function(){
	//id选择器，其实就是树组件所在的ul标签，创建树
	$("#contentCategory").tree({
		//创建树需要的数据时发起的请求
		url : '/rest/content/category',
		animate: true,
		method : "GET",
		//点击鼠标右键，执行以下逻辑
		onContextMenu: function(e,node){
			//关闭鼠标右键默认事件
            e.preventDefault();
			//执行树的select方法，选中鼠标所在的节点
            $(this).tree('select',node.target);
			//id选择器获取Menu组件，执行组件的show方法，其实就是显示菜单
            $('#contentCategoryMenu').menu('show',{
            	//设置菜单显示的位置，其实就是鼠标所在的位置
                left: e.pageX,
                top: e.pageY
            });
        },
        //在编辑之后执行，node就是编辑的节点
        onAfterEdit : function(node){
        	//获取树本身
        	var _tree = $(this);
        	//判断编辑的节点id是否为0，其实就是判断这个节点是否为新增节点
        	if(node.id == 0){
        		// 新增节点
        		//发起了Ajax的post请求，让后台新增内容分类
        		$.post("/rest/content/category/add",{parentId:node.parentId,name:node.text},function(data){
        			//update：执行树的更新方法
        			_tree.tree("update",{
        				//需要更新的节点
        				target : node.target,
        				//设置id，其实就是设置返回的内容分类的Id
        				id : data.id
        			});
        		});
        	}else{
        		//如果节点的id不为0，表示这里是更新
        		$.ajax({
        			   type: "POST",
        			   url: "/rest/content/category/update",
        			   data: {id:node.id,name:node.text},
        			   success: function(msg){
        				   //$.messager.alert('提示','新增商品成功!');
        			   },
        			   error: function(){
        				   $.messager.alert('提示','重命名失败!');
        			   }
        			});
        	}
        }
	});
});
function menuHandler(item){
	//获取树
	var tree = $("#contentCategory");
	//获取选中的节点
	var node = tree.tree("getSelected");
	//传递进来的item就是选择的菜单项
	//==:  1==1  true    1=="1"   true      
	//===:  1===1 true   1==="1" false
	//判断选中的菜单项是否是添加
	if(item.name === "add"){
		//执行树的添加节点方法
		tree.tree('append', {
			//指定追加的节点的父，就是之前选中的节点
            parent: (node?node.target:null),
            //设置新增节点的数据
            data: [{
                text: '新建分类',
                id : 0,
                parentId : node.id
            }]
        }); 
		//获取新增节点
		var _node = tree.tree('find',0);
		//选中新增的节点，beginEdit：开始编辑新增节点
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	//判断选中的菜单项是否是重命名
	}else if(item.name === "rename"){
		//开始编辑节点
		tree.tree('beginEdit',node.target);
	//判断选中的菜单项是否是删除
	}else if(item.name === "delete"){
		//提示用户，确认是否删除
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			//如果用户选择是，则执行以下逻辑
			if(r){
				$.ajax({
     			   type: "POST",
     			   url: "/rest/content/category/delete",
     			   data : {parentId:node.parentId,id:node.id},
     			   success: function(msg){
     				   //$.messager.alert('提示','新增商品成功!');
     				  //remove：删除树上的节点
     				  tree.tree("remove",node.target);
     			   },
     			   error: function(){
     				   //如果请求异常，提示删除失败
     				   $.messager.alert('提示','删除失败!');
     			   }
     			});
			}
		});
	}
}
</script>