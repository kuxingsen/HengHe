/*閫氱敤鑴氭湰*/

$(document).ready(function () {
	// topbar宸︿笂瑙掔敤鎴蜂俊鎭睍绀�
	// var API_HOST = "/";
	// var paramString = "m=api&c=user&a=status";
	// window.getUserInfo = function(){
	// 	$.getJSON(API_HOST + "?" + paramString + "&jsoncallback=?", function (responseData) {
	// 		if (!responseData.status) {
	// 			return;
	// 		}
	//
	// 		var $_userinfoContainer = $("#userinfoContainer");
	// 		var $_usernameText = $("#usernameText");
	// 		var $_usernameShow = $("#usernameShow");
	// 		var $_userId = $("#userId");
	//
	// 		$_usernameShow.html(responseData.name);
	// 		$_userId.html("(ID:" + responseData.id + ")");
	// 		$("#serverCount").html(responseData.servercount);
	// 		$("#orderCount").html(responseData.ordercount);
	// 		$("#msgCount").html(responseData.msgcount);
	//
	//
	// 		var money = responseData.money || "0";
	// 		if (String(money).indexOf(".") == -1) {
	// 			money += ".00";
	// 		}
	// 		$("#userMoney").html(money);
	//
	// 		var verifyClassNameMap = {
	// 			"0": "auth-icon-unauth",
	// 			"1": "auth-icon-personal",
	// 			"2": "auth-icon-company"
	// 		};
	// 		$_usernameText.addClass(verifyClassNameMap[responseData.verify]);
	//
	// 		// 鍒濆鍖栨暟鎹箣鍚庯紝鏄剧ず 鐢ㄦ埛闈㈡澘
	// 		$_userinfoContainer.addClass("welcome-user");
	//
	// 		// 鍔ㄧ敾鏁堟灉
	// 		var PANEL_EXTEND_CLASSNAME = "userinfo-container--extend";
	// 		var EXTEND_DURATION = 300;
	// 		var $_usernameContainer = $("#usernameContainer");
	// 		var $_userPanel = $("#userPanel");
	// 		var panelHeight = $_userPanel.css("height");
	// 		$_userinfoContainer.on("mouseenter", ".userinfo-container", function () {
	// 			$_userinfoContainer.addClass(PANEL_EXTEND_CLASSNAME);
	//
	// 			// 鍙栨秷鍔ㄧ敾鍒楅槦锛屼笉瀹屾垚褰撳墠鍔ㄧ敾
	// 			$_userPanel.stop(true, false);
	// 			$_userPanel.css("height", 0);
	// 			$_userPanel.animate({height: panelHeight}, EXTEND_DURATION);
	// 		});
	// 		$_userinfoContainer.on("mouseleave", ".userinfo-container", function () {
	// 			// 鍙栨秷鍔ㄧ敾鍒楅槦锛屼笉瀹屾垚褰撳墠鍔ㄧ敾
	// 			$_userPanel.stop(true, false);
	// 			$_userPanel.animate({height: 0}, EXTEND_DURATION, function () {
	// 				$_userinfoContainer.removeClass(PANEL_EXTEND_CLASSNAME);
	// 			});
	// 		});
	// 	});
	// }
	// getUserInfo();

	//杩斿洖椤堕儴鎸夐挳
	var $toTop = $("#toTop");
	$toTop.hide();
	$(window).scroll(function () {
		if ($(window).scrollTop() > 100) {
			if($toTop.is(":hidden")) {
				$toTop.stop().fadeIn(500);
			}
		}
		else {
			if($toTop.is(":visible")) {
				$toTop.stop().fadeOut(500);
			}
		}
	});
	$toTop.click(function () {
		$('body,html').stop().animate({scrollTop: 0}, 300);
		return false;
	});

	//浜岀骇鑿滃崟浜や簰鏁堟灉
	var $headerNavUl = $(".header-nav>ul");
	$(".header-nav-li").mouseenter(function(){
		$headerNavUl.attr({"class":""});
		$headerNavUl.addClass("nav-bar-"+($(this).index()+1));
		$(this).addClass("header-nav-li--active").siblings().removeClass("header-nav-li--active");
	});
	$(".header-nav").mouseleave(function(){
		$(".header-nav-li").removeClass("header-nav-li--active");
		$headerNavUl.attr({"class":""});
	});
	//鈥滈椤碘€濅笉瑙﹀彂浜岀骇鑿滃崟
	$(".nav-1").hover(function(){
		$(".header-nav").addClass("hide-pop-list");
	}, function () {
		$(".header-nav").removeClass("hide-pop-list");
	});

	/*footer閮ㄥ垎鐨勮剼鏈�*/
	//渚ц竟鏍忓脊鍑�
	$(".suspension-tel, .suspension-qrcode").hover(function(){
		$(this).children(".pop-detail").fadeIn(300);
	},function(){
		$(this).children(".pop-detail").fadeOut(100);
	});

	$(".official-plat ul li:first-child").hover(function () {
		$(".weixin").show();
		$(".weibo").hide();
	});
	$("li[title='鐐瑰嚮鎵撳紑瀹樻柟寰崥']").hover(function () {
		$(".weixin").hide();
		$(".weibo").show();
	});

	//href="#a_null"鐨勭粺涓€璁剧疆涓烘棤鏁堥摼鎺�
	$("a[href='#a_null']").click(function(){
		return false;
	});

	//鍏ㄥ浘鍙偣鍨媌anner娣诲姞鎵撳紑閾炬帴鏂规硶
	$(".link-banner").each(function(){
		var $_self = $(this);
		var openURL = $_self.data("url");

		if(openURL) {
			$_self.click(function(){
				var openTarget = $_self.data("target") || "self";
				window.open(openURL,openTarget);
			});
		}
	});
	//PIE 缁熶竴璁剧疆
	if (window.PIE) {
		$('.pie-for-element').each(function () {
			PIE.attach(this);
		});
		$('.pie-for-children').children().each(function () {
			PIE.attach(this);
		});
		$('.pie-for-tree').find("*").each(function () {
			PIE.attach(this);
		});
	}
	//楠岃瘉鐮佸垏鎹�
	var $_showCaptcha = $(".show-captcha");
	var $_refreshCaptcha = $(".refresh-captcha");
	var captchaSrc = "/?m=api&c=captcha";
	var createCaptchaSrc = function () {
		return captchaSrc + '&rnd=' + Math.random();
	};
	var refreshCaptchaImg = function (captchaImg) {
		$(captchaImg).attr("src", createCaptchaSrc());
	};

	refreshCaptchaImg($_showCaptcha);
	$_showCaptcha.click(function(){
		refreshCaptchaImg(this);
	});
	$_refreshCaptcha.click(function(){
		refreshCaptchaImg($(this).parent().find(".show-captcha"));
	});
});

// 灏佽宸ュ叿绫绘柟娉�
$(function () {
	window.NY = window.NY || {};

	// add feedback tips: warn/success/error
	if ($.dialog && $.dialog.tips) {
		var DEFAULT_TIPS_SHOW_DURATION = 2;
		var tipsTypeList = ["warn", "success", "error", "loading"];
		var tipsTypeMap = {
			warn: "alert"
		};

		$.each(tipsTypeList, function (i, tipsType) {
			var basicMethodType = tipsTypeMap[tipsType] || tipsType;

			window.NY[tipsType] = function (text, duration, callback) {
				duration = duration || DEFAULT_TIPS_SHOW_DURATION;

				return $.dialog.tips(text, duration, basicMethodType, callback);
			};
		});
	}

	// 浣滀负ajax璇锋眰澶辫触鏃� 鎻愮ず浣跨敤
	if (NY.warn) {
		NY.showBusy = function (duration, callback) {
			return NY.warn("鏈嶅姟鍣ㄧ箒蹇欙紝璇风◢鍚庨噸璇曪紒", duration, callback);
		};
	}


	// 灏佽鍥炶溅閿簨浠跺搷搴旀柟娉�
	NY.enterKey = function (element, handler, options) {
		options = options || {};
		var eventType = options.eventType || "keypress";
		var eventData = options.eventData;
		var isCtrlKey = options.isCtrlKey;
		var isShiftKey = options.isShiftKey;
		var isAltKey = options.isAltKey;

		var isBoolean = function (param) {
			return (typeof param === "boolean");
		};
		// 灏藉湪鎸変笅鍥炶溅閿� 涓旂粍鍚堥敭绗﹀悎璁剧疆鏃� 鎵嶈Е鍙戝洖璋冧簨浠�
		var myHandler = function (e) {
			var keyCode = e.which;
			var that = this;

			if ((keyCode == 10) || (keyCode == 13)) {
				// 濡傛灉鎸囧畾浜咰trl銆丼hift銆丄lt绛夛紝鍒欎弗鏍煎尮閰嶇浉搴旂粍鍚堥敭
				if (isBoolean(isCtrlKey) && (isCtrlKey !== e.ctrlKey)) {
					return;
				}
				else if (isBoolean(isShiftKey) && (isShiftKey !== e.shiftKey)) {
					return;
				}
				else if (isBoolean(isAltKey) && (isAltKey !== e.altKey)) {
					return;
				}

				handler.call(that, e);
			}
		};

		// 鐩稿綋浜� 灏嗐€�$(element).keypress(eventData, myHandler);銆戜腑鐨刱eypress鎹㈡垚鍙橀噺
		return $(element)[eventType](eventData, myHandler);
	};
});

// 鍩嬬偣缁熻
$(function () {
	var deleteCookie = function (name) {
		document.cookie = name + "=;path=/;expires=" + (new Date().toUTCString());
	};

	// #id=richu-xxx[-yyy]
	var hash = location.hash.slice(1);
	var matchGroups = hash.match(/(\-\d+)/g);
	if (!matchGroups || (matchGroups.length > 2)) {
		// 涓嶇鍚堣鍒欐椂锛宺eturn浣嗕笉鍒犻櫎璇嗗埆鐮�
		// deleteCookie("channelID");
		// deleteCookie("channelType");
		return;
	}

	var channelType = (matchGroups[0] || "").slice(1);
	var channelID = (matchGroups[1] || "").slice(1);
	if (!channelID) {
		channelID = channelType;
		deleteCookie("channelType");
	}
	else {
		document.cookie = "channelType=" + channelType + ";path=/";
	}
	document.cookie = "channelID=" + channelID + "; path=/";
});


//娉㈡氮鍔ㄧ敾
$(function(){
	var marqueeScroll = function(id1,id2,id3,timer){
		var $parent = $("#"+id1);
		var $goal = $("#"+id2);
		var $closegoal = $("#"+id3);
		$closegoal.html($goal.html());
		function Marquee (){
			if(parseInt($parent.scrollLeft())-$closegoal.width()>=0)
			{
				$parent.scrollLeft(parseInt($parent.scrollLeft())-$goal.width());
			}
			else
			{
				$parent.scrollLeft($parent.scrollLeft()+1);
			}
		}
		setInterval(Marquee,timer);
	}
	var marqueeScroll1 = new marqueeScroll("marquee-box","wave-list-box1","wave-list-box2",20);
	var marqueeScroll2 = new marqueeScroll("marquee-box3","wave-list-box4","wave-list-box5",40);
})