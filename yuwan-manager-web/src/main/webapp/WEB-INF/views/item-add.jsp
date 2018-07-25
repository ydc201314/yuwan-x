<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="itemAddForm" class="itemForm" method="post">
	    <table cellpadding="5">
	        <tr>
	            <td>商品类目:</td>
	            <td>
	            	<a href="javascript:void(0)" class="easyui-linkbutton selectItemCat">选择类目</a>
	            	<span ></span>
	            	<input type="hidden" name="cid" style="width: 280px;"></input>
	            </td>
	        </tr>
	        <tr>
	            <td>商品标题:</td>
	            <td><input class="easyui-textbox" type="text" name="title" data-options="required:true" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>商品卖点:</td>
	            <td><input class="easyui-textbox" name="sellPoint" data-options="multiline:true,validType:'length[0,150]'" style="height:60px;width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>商品价格:</td>
	            <td><input class="easyui-numberbox" type="text" name="priceView" data-options="min:1,max:99999999,precision:2,required:true" />
	            	<input type="hidden" name="price"/>
	            </td>
	        </tr>
	        <tr>
	            <td>库存数量:</td>
	            <td><input class="easyui-numberbox" type="text" name="num" data-options="min:1,max:99999999,precision:0,required:true" /></td>
	        </tr>
	        <tr>
	            <td>条形码:</td>
	            <td>
	                <input class="easyui-textbox" type="text" name="barcode" data-options="validType:'length[1,30]'" />
	            </td>
	        </tr>
	        <tr>
	            <td>商品图片:</td>
	            <td>
	            	 <a href="javascript:void(0)" class="easyui-linkbutton picFileUpload">上传图片</a>
	            	 <div class="pics"><ul></ul></div>
	                 <input type="hidden" name="image"/>
	            </td>
	        </tr>
	        <tr>
	            <td>商品描述:</td>
	            <td>
	                <textarea style="width:800px;height:300px;visibility:hidden;" name="desc"></textarea>
	            </td>
	        </tr>
	        <tr class="params hide">
	        	<td>商品规格:</td>
	        	<td>
	        		
	        	</td>
	        </tr>
	    </table>
	    <input type="hidden" name="itemParams"/>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
	//编辑器参数
	kingEditorParams = {
		filePostName  : "uploadFile",   //文件上传的文件名
		uploadJson : '/rest/pic/upload',	//文明上传的请求路径
		dir : "image"      //文件类型
	};
	
	var itemAddEditor ;
	//当页面加载完成后，执行以下逻辑
	$(function(){
		//创建富文本编辑器
		itemAddEditor = KindEditor.create("#itemAddForm [name=desc]", kingEditorParams);
		//初始化类目选择
		initItemCat();
		//初始化图片上传
		initPicUpload();
	});
	
	//提交商品信息到后台
	function submitForm(){
		//校验表单
		if(!$('#itemAddForm').form('validate')){
			$.messager.alert('提示','表单还未填写完成!');
			return ;
		}
		//id选择器找到form表单，里面name=price，设置价格,其实就是把显示的价格乘以100，再放进来
		//"1"+"1"="11"   eval:  "1"+"1"="2"
		$("#itemAddForm [name=price]").val(eval($("#itemAddForm [name=priceView]").val()) * 100);
		
		//同步方法，把富文本编辑器的编辑区域内容，放到多行文本域中，目的提交后台
		itemAddEditor.sync();
				
		//提交到后台的RESTful
		$.ajax({
		   type: "POST",
		   url: "/rest/item",
		   data: $("#itemAddForm").serialize(),
		   success: function(msg){
			   $.messager.alert('提示','新增商品成功!');
		   },
		   error: function(){
			   $.messager.alert('提示','新增商品失败!');
		   }
		});
	}
	
	function clearForm(){
		$('#itemAddForm').form('reset');
		itemAddEditor.html('');
	}
	
	//类目选择初始化
	function initItemCat(){
		//使用class选择器找到类目选择按钮
		var selectItemCat = $(".selectItemCat");
		//给类目选择按钮绑定点击事件
   		selectItemCat.click(function(){
   			//创建div标签，在div中写入ul标签，开启一个窗口
   			$("<div>").css({padding:"5px"}).html("<ul>")
   			.window({
   				width:'500',
   			    height:"450",
   			    modal:true,
   			    closed:true,
   			    iconCls:'icon-save',
   			    title:'选择类目啊啊啊',
   			    //窗口打开后执行以下逻辑
   			    onOpen : function(){
   			    	//获取当前窗口
   			    	var _win = this;
   			    	//$("ul",_win):标签选择器，从当前窗口中进行选择，其实就是找到之前设置的ul标签
   			    	//创建树
   			    	$("ul",_win).tree({
   			    		url:'/rest/item/cat',
   			    		method:'GET',
   			    		animate:true,
   			    		//给树上所有的节点绑定了点击事件
   			    		onClick : function(node){
   			    			//判断节点是否是叶子节点，如果是叶子节点，执行以下逻辑
   			    			if($(this).tree("isLeaf",node.target)){
   			    				// 填写到cid中
   			    				//selectItemCat.parent().find("[name=cid]")获取类目选择按钮的父，查找name=cid的input
   			    				//把点击的节点id复制给input
   			    				selectItemCat.parent().find("[name=cid]").val(node.id);
   			    				//获取类目选择按钮的下一个元素，设置类目名称，其实就是回显选中的类目
   			    				selectItemCat.next().text(node.text);
   			    				//关闭当前窗口
   			    				$(_win).window('close');
   			    			}
   			    		}
   			    	});
   			    },
   			    onClose : function(){
   			    	$(this).window("destroy");
   			    }
   			}).window('open');
   		});
    }
	
	//图片上传初始化
	function initPicUpload(){
		//使用class选择器找到图片上传按钮，绑定点击事件
       	$(".picFileUpload").click(function(){
       		//id选择器，获取的是form表单
       		var form = $('#itemAddForm');
       		//加载富文本编辑器的批量上传图片组件，根据api编写
       		KindEditor.editor(kingEditorParams).loadPlugin('multiimage',function(){
       			//获取编辑器本身
       			var editor = this;
       			//显示图片上传界面
       			editor.plugin.multiImageDialog({
       				//clickFn给“全部插入”按钮绑定点击事件
					clickFn : function(urlList) {
						//找到class为pics的，找到里面的li标签，进行清除，没有li，为啥要删除？清除原来的图片回显
						$(".pics li").remove();
						//声明数组
						var imgArray = [];
						//执行遍历urlList，里面存放的是上传成功图片的url路径
						KindEditor.each(urlList, function(i, data) {
							//把图片的url放到数组中
							imgArray.push(data.url);
							//找到class为pics里的ul，在里面追加li标签，其实就是为了回显
							$(".pics ul").append("<li><a href='"+data.url+"' target='_blank'><img src='"+data.url+"' width='80' height='50' /></a></li>");
						});
						//在表单中获取name=image的元素，赋值  把图片的url拼接成字符串，中间用，分隔
						form.find("[name=image]").val(imgArray.join(","));
						//隐藏上传界面
						editor.hideDialog();
					}
				});
       		});
       	});
	}
	
</script>
