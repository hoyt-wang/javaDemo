<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM | 编辑员工信息</title>
    <%@ include file="../include/css.jsp"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../include/header.jsp"%>
    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->
    <jsp:include page="../include/sider.jsp">
        <jsp:param name="menu" value="employee"/>
    </jsp:include>
    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">新增员工</h3>
                    <div class="box-tools pull-right">
                        <a class="btn btn-primary btn-sm" href="/employee"><i class="fa fa-arrow-left"></i> 返回列表</a>
                    </div>
                </div>
                <div class="form-group">
                    <form  method="post" id="addForm">
                        <div class="form-group">
                            <label>姓名</label>
                            <input type="text" name="userName" class="form-control" value="${account.userName}">
                        </div>
                        <div class="form-group">
                            <label>密码(默认000000)</label>
                            <input type="password" class="form-control" name="password" value="${account.password}">
                        </div>
                        <div class="form-group">
                            <label>手机号码</label>
                            <input type="text" name="mobile" class="form-control" value="${account.mobile}">
                        </div>

                        <div class="form-group">
                            <button id="addBtn" class="btn btn-primary">提交</button>
                        </div>
                    </form>
                </div>

                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
    <%@ include file="../include/footer.jsp"%>

</div>
<!-- ./wrapper -->

<%@ include file="../include/js.jsp"%>
<script>
   $(function () {
     /*  $("#addBtn").click(function(){
           $("#addForm").submit();*/
   })
</script>

</body>
</html>
