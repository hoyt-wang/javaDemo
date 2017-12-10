<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-首页</title>
  <%@include file="include/css.jsp"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
   <%@include file="include/header.jsp"%>

    <%@include file="include/sider.jsp"%>
    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <c:if test="${not empty message}">
            ${message}
        </c:if>
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">修改密码</h3>

                </div>
                <div class="box-body">
                    <form action="" id="settingForm">
                        <div class="form-group">
                            <label>原始密码</label>
                            <input type="password" class="form-control" name="password" >
                        </div>
                        <div class="form-group">
                            <label>新密码</label>
                            <input type="password" class="form-control" id="newPassword" name="newPassword">
                        </div>
                        <div class="form-group">
                            <label>确认密码</label>
                            <input type="password" class="form-control" name="confirmPassword">
                        </div>

                    </form>
                    <div class="box-footer">
                        <button class="btn btn-primary" id="settingBtn">保存</button>
                    </div>
                </div>

                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
   <%@include file="include/footer.jsp"%>

</div>
<!-- ./wrapper -->

<%@include file="include/js.jsp"%>
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script>
    $(function () {
        $("#settingBtn").click(function () {
            $("#settingForm").submit();
        });

        $("#settingForm").validate({
            errorClass:"text-danger",
            errorElement:"span",
            rules:{
                password:{
                    required:true
                },
                newPassword:{
                    required:true
                },
                confirmPassword:{
                    required:true,
                    equalTo:"#newPassword"
                }
            },
            messages:{
                password:{
                    required:"请输入原始密码"
                },
                newPassword:{
                    required:"请输入新密码"
                },
                confirmPassword:{
                    required:"请输入确认密码",
                    equalTo:"两次密码不一致"
                }
            },
            submitHandler:function () {
                $.post("/profile",$("#settingForm").serialize()).done(function (data) {
                    if(data.state == "success") {
                        layer.alert("密码修改成功，请重新登录",function () {
                            window.location.href = "/";
                        })
                    } else {
                        layer.msg(data.message);
                    }
                }).error(function () {
                    layer.msg("服务器异常");
                });
            }
        });
    });
</script>
</body>
</html>
