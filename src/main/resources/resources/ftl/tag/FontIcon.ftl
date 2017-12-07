<div class="form-group" id="${uuid}">
    <label class="col-sm-3 control-label">${text}：</label>
    <div class="col-sm-8">
    	<div class="input-group">
		  <span class="input-group-btn">
	        <button class="btn btn-default icon-show btn-choose-icon" type="button"><icon class="${value}"></icon> 选择</button>
	      </span>
		  <input name="${name}" value="${value}" class="form-control" placeholder="填写或选择图标class">
		  <span class="input-group-btn">
	        <button class="btn btn-default btn-choose-color" type="button">颜色</button>
	      </span>
		</div>
        <!--
        <div class="input-group">
	      <span class="input-group-btn">
	        <button class="btn btn-default btn-choose-color" type="button">颜色</button>
	      </span>
	    </div>
	    -->
    </div>
</div>
<script type="text/javascript">
$(function(){
	$('#${uuid} .btn-choose-color').colpick({
		layout:'hex',
		submit:0,
		colorScheme:'dark',
		onChange:function(hsb,hex,rgb,el,bySetColor) {
			${onColorChange}
		}
	});
	
	$('#${uuid} .btn-choose-icon').click(function(){
		var ly1 = 
		layer.open({
            type: 2,
            title: '请选择一个图标',
            shadeClose: true,
            shade: false,
            maxmin: true, //开启最大化最小化按钮
            area: ['${iframeWidth}px', '${iframeHeight}px'],
            content: '${path}/awesome.html',
            btn: ['确认', '取消'],
		  	yes: function(index,layero){
		  	    var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
		  	    var icon = iframeWin.icondata.selected;
		  	    if(icon){
		  	    	layer.close(ly1);
		  	    	$('#${uuid} [name="${name}"]').val(icon);
		  	    	$('#${uuid} .icon-show icon').removeClass().addClass(icon);
		  	    }
		  	},
		  	btn2: function(){
		    	layer.close(ly1);
		  	}
        });
	});
	var sync = function(){
		$('#${uuid} .icon-show icon').removeClass().addClass($(this).val());
	};
	$('#${uuid} [name="${name}"]').keyup(sync).click(sync);
});
</script>