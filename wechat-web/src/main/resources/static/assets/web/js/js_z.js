$(function(){
	var w_w=$(window).width();
	var m_l=(1920-w_w)/2;
	$('.head span').click(function(){
		$(this).siblings('.nav').slideToggle();	
	});
	$('.i_m li:nth-child(2n)').css({'margin-right':0,'padding-left':10+'px'});
	$('.i_m li:nth-child(2n+1)').css({'padding-right':10+'px'});
	$('.zs li:nth-child(2n)').css({'padding-left':10+'px'});
	$('.zs li:nth-child(2n+1)').css({'padding-right':10+'px'});
	
	$('.news li:last-child').css('border',0);
	$('.scd_ma ul li:nth-child(2n)').css({'margin-top':130+'px','float':'left'});
	
	
	$(".tabBox .tabCont:first").css("display","block");
	$(".tabBox .tabNav li").click(function(){
		$(this).siblings("li").removeClass("now");
		$(this).addClass("now");
		$(this).parents(".tabBox").find(".tabCont").css("display","none");
		var i=$(this).index();
		$(this).parents(".tabBox").find(".tabCont:eq("+i+")").css("display","block");
	});
	$('.pro_d .tabBox .tabNav li:first-child').css('background','none');
})
