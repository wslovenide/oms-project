$(function () {



    var fun = true;
    var loginsucc = false;
    var page = "";


    var login = (function () {

        var sliderW = $(".login-slider")[0].offsetWidth;
        var sliderBoxW = $(".login-box")[0].offsetWidth;
        var moveW = " ";
        var starX = " ";
        var moveChange = false;

        $(".login-box").mousedown(function (e) {
            e.preventDefault();
            if (fun) {
                loginsucc = false;
                moveChange = true;
                starX = e.offsetX;
            }
        });
        $(".login-slider").mousemove(function (e) {
            e.preventDefault();
            if (moveChange && fun) {
                var endX = e.pageX - $(this).offset().left;
                moveW = endX - starX;
                if (moveW < sliderW - sliderBoxW && moveW >= 0) {
                    $(this).children(".login-slider-bg").css("width", moveW + "px");
                    $(this).children(".login-box").css("left", moveW + "px");
                } else if (moveW >= sliderW - sliderBoxW) {
                    $(this).children(".login-text").html("验证成功").addClass("anim");
                    $(this).children(".login-slider-bg").css("width", sliderW - sliderBoxW + "px");
                    $(this).children(".login-box").css({"left": sliderW - sliderBoxW + "px"}).addClass("succ");
                    fun = false;
                    moveChange = false;
                    loginsucc = true;
                    loginText.postBtn();
                }
            }
        });
        $(window).mouseup(function (e) {
            e.preventDefault();
            if (moveChange && fun) {
                moveChange = false;
                if (moveW < sliderW - sliderBoxW && moveW >= 0) {
                    if (moveW < sliderW - sliderBoxW && moveW >= 0) {
                        if (moveW < sliderW - sliderBoxW && moveW >= 0) {
                            $(".login-slider-bg").animate({"width": 0}, 500);
                            $(".login-box").stop().animate({"left": 0}, 500);
                        } else {
                            fun = false;
                        }
                    }
                }}});


    })();

    $(".main-rt").click(function (e) {
        if ($(e.target).attr('data-type') == 'ewm') {
            $(this).attr({'data-type': "dn", "class": "main-rt rt-dn"});
            $(".box").css("display", "none");
            $(".ewmdl").css("display", "block");
        } else if ($(e.target).attr('data-type') == 'dn') {
            $(this).attr({'data-type': "ewm", "class": "main-rt rt-ewm"});
            $(".ewmdl").css("display", "none");
            $(".box").css("display", "block");
        }
    });
    $(".tran-btn").click(function (e) {
        var type = $(e.target).attr("data-type");
        page = type;
        loginsucc = false;
        $(".main-tran").addClass("switching");
        if (type == 'login') {
            setTimeout(function () {
                startUp();
                $("title").html("登录-链向财经");
                $(".login-top").css("display", "block");
                $(".register-top").css("display", "none");
            }, 400)
        } else if (type == 'register') {
            setTimeout(function () {
                $("title").html("注册-链向财经");
                $(".login-top").css("display", "none");
                $(".register-top").css("display", "block");
                $(".login-input input").val("");
                $(".login-input p span").html('').removeClass("show").parent("p").css("height", "55px");
                $(".submit").removeClass("show").attr("data-type" , "no");
            }, 400)
        }
        setTimeout(function () {
            startUp();
            $(".main-tran").removeClass("switching");
        }, 810)
    });
    $(".ewm-link").on("click", "span", function (e) {
        var type = $(e.target).attr("data-type");
        $(".main-rt").attr({'data-type': "ewm", "class": "main-rt rt-ewm"});
        $(".ewmdl").css("display", "none");
        $(".box").css("display", "block");
        if (type == 'login') {
            $(".login-top").css("display", "block");
            $(".register-top").css("display", "none");
        } else if (type == 'register') {
            $(".login-top").css("display", "none");
            $(".register-top").css("display", "block");
        }
    });

    function startUp() {
        $(".login-text").html("请按住滑块，拖动到最右边").removeClass("anim");
        $(".login-slider-bg").css("width", 0);
        $(".login-box").css({"left": 0}).removeClass("succ");
        fun = true;
    }

    var href = {
        link: '',
        num: '',
        str: '',
        arr: ' ',
        init: function () {
            this.link = window.location.href;
            this.num = this.link.indexOf("?");
            this.str = this.link.substr(this.num + 1);
            this.arr = this.str.split("&");
            this.strData();
            this.show();
        },
        strData: function () {
            for (var i = 0; i < this.arr.length; i++) {
                var num = this.arr[i].indexOf("=");
                var val = this.arr[i].substr(num + 1);
                this.arr[i] = {key: val};
            }
            page = this.arr[0].key;

        },
        show: function () {
            if (this.arr[0].key == "login") {
                $("title").html("登录-点链");
                $(".login-top").css("display", "block");
                $(".register-top").css("display", "none");
            } else if (this.arr[0].key == "register") {
                $("title").html("注册-点链");
                $(".login-top").css("display", "none");
                $(".register-top").css("display", "block");
            }
        }
    };
    href.init();

    var loginText = {
        text: {
            userText: '用户名长度5-10位，支持数字、字母、字符组合',
            pwdText: '密码长度6-16位，支持数字、字母、字符组合',
            pwdTexts: '密码不一致'
        },
        nameReg: new RegExp(/^[a-zA-Z0-9`~!@#$^&*()=|':;',\[\].<>\/?~！@#￥……&*（）——{}【】‘；：”“。，、？]{5,10}$/),
        pwdReg: new RegExp(/^[a-zA-Z0-9`~!@#$^&*()=|':;',\[\].<>\/?~！@#￥……&*（）——{}【】‘；：”“。，、？]{6,16}$/),
        inputClass: '',
        data: ' ',
        init: function () {
            this.remindJudge();
            this.deleteVal();
        },
        remindJudge: function () {
            var thiss = this;
            $(".login-input input")
                .focus(function (e) {
                    thiss.detailBtn(e.target);
                })
                .blur(function (e) {
                    var _this = this;
                    setTimeout(function () {
                        $(_this).next("i").removeClass("show");
                    }, 200);
                })
                .keyup(function (e) {
                    this.inputClass = $(e.target).attr("class");
                    thiss.detailBtn(e.target);
                    var len = $(e.target).val();
                    thiss.postBtn();
                    switch (this.inputClass) {
                        case 'register-input-text' :
                            thiss.textUserData(e.target, len);
                            break;
                        case 'register-input-pwd' :
                            thiss.textPwdData(e.target, len);
                            break;
                        case 'register-input-pwds' :
                            thiss.textPwdsData(e.target);
                            break;
                    }
                })
        },
        remindShow: function (_this, text) {
            $(_this).siblings("span").html(text).addClass('show').parent("p").css("height", "70px");

        },
        remindHide: function (_this) {
            $(_this).siblings("span").html('').removeClass("show").parent("p").css("height", "55px");
        },
        detailBtn: function (_this) {
            if ($(_this).val() != "") {
                $(_this).siblings("i").addClass("show");
            } else {
                $(_this).siblings("i").removeClass("show");
            }
        },
        deleteVal: function () {
            $(".login-input i").click(function (e) {
                $(e.target).removeClass("show").siblings("input").val("");
            });
        },
        textUserData: function (_index, n) {
            if (this.nameReg.test(n)) {
                this.remindHide(_index);
                this.postBtn();
            } else {
                this.remindShow(_index, this.text.userText);
            }
        },
        textPwdData: function (_index, n) {
            if (this.pwdReg.test(n)) {
                this.remindHide(_index);
                var pwd = $(_index).val();
                var pwds = $(".register-input-pwds").val();

                if(pwds != 0){
                    if (pwd == pwds) {
                        this.remindHide(".register-input-pwds");
                    } else {
                        this.remindShow(".register-input-pwds", this.text.pwdTexts);
                    }
                }
                this.postBtn();
            } else {
                this.remindShow(_index, this.text.pwdText);
            }
        },
        textPwdsData: function (_index) {
            var pwd = $(".register-input-pwd").val();
            var pwds = $(_index).val();

            if (pwd == pwds) {
                this.remindHide(_index);
                this.postBtn();
            } else {
                this.remindShow(_index, this.text.pwdTexts);
            }
        },
        postBtn: function () {
            if (page == 'register') {

                var userName = $('.register-input-text').val();
                var userPwd = $('.register-input-pwd').val();
                var userPwds = $('.register-input-pwds').val();

                if (
                    this.nameReg.test(userName) && this.pwdReg.test(userPwd) && userPwd == userPwds &&
                    $('.register-input-check')[0].checked &&loginsucc
                ) {
                    $(".register-submit").addClass("show").attr("data-type", "yes");
                    return true;
                } else {
                    $(".register-submit").removeClass("show").attr("data-type", "no");
                    return false;
                }
            } else if (page == "login") {
                if (
                    $('.login-input-text').val() != "" && $('.login-input-pwd').val() != "" && loginsucc) {
                    $(".login-submit").addClass("show").attr("data-type", "yes");
                } else {
                    $(".login-submit").removeClass("show").attr("data-type", "no");
                }
            }
        }
    }
    loginText.init();

    $('.register-input-check').change(function () {
        loginText.postBtn();
    });






    $(".register-submit").click(function (e) {
        if($(e.target).attr("data-type") == "yes"){

            var userName = $('.register-input-text').val();
            var userPwd = $('.register-input-pwd').val();
            var userPwds = $('.register-input-pwds').val();
            var isKill = $("input[name='isKill']").val();

            if (loginText.postBtn) {
                $.ajax({
                    type:"POST",
                    url:'/user/register/doregister?account='+userName+'&pwd='+userPwd+'&repwd='+userPwds+"&isKill="+isKill,
                    success:function (data) {
                        var tmp = eval("("+data+")");
                        if(tmp.success){
                            if(tmp.obj.isKill == 1){
                                localStorage.hb = 1 ;
                                localStorage.rain = 2 ;
                                window.opener.location.reload();
                                window.close();
                            }else{
                                window.location.href =   '/user/profile/bindemail';
                            }
                        }else{
                            var msg = tmp.msg;
                            if(msg!=null&&msg!=""){
                                $(".register-prompt").addClass("show").html(msg);
                                loginsucc = false;
                                startUp();
                                loginText.postBtn();
                            }
                        }
                    }
                })
            }

        }
    });


    $(".login-submit").click(function (e) {
        if($(e.target).attr("data-type") == "yes"){
            var userName = $('.login-input-text').val() ;
            var userPwd = $('.login-input-pwd').val() ;
            var isKill = $("input[name='isKill']").val();
            var phoneExp = new RegExp(/^1(3|4|5|7|8)\d{9}$/);
            var emailExp = new RegExp(/^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/);
            var data = {"pwd" : userPwd , "isKill" : isKill};
            if (phoneExp.test(userName)) {
                data.phone = userName ;
            }else if (emailExp.test(userName)) {
                data.mail = userName ;
            }else if (userName.length >= 5 && userName.length <= 10) {
                data.account = userName ;
            }else {
                layer.msg("请输入正确的用户名、邮箱或手机号！");
                return
            }

            $.ajax({
                type:"POST",
                url: '/user/login/dologin',
                data:data,
                success:function (data) {
                    var tmp = eval("("+data+")");
                    if(tmp.success){
                        if(tmp.obj == 1){
                            localStorage.hb = 1 ;
                            localStorage.rain = 2 ;
                            window.opener.location.reload();
                            window.close();
                        }else{
                            if(tmp.msg!=1){
                                window.location.href = tmp.msg;
                            }else window.location.href = "/";
                        }
                    }else{
                        var msg = tmp.msg;
                        if(msg!=null&&msg!=''){
                            $(".login-prompt").addClass("show").html(msg);
                            loginsucc = false;
                            startUp();
                            loginText.postBtn();
                        }
                    }
                }
            })
        }
    })



})