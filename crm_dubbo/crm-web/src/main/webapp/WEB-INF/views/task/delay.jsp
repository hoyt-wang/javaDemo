<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM | 账号管理</title>
    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/tree/css/metroStyle/metroStyle.css">
    <link rel="stylesheet" href="/static/plugins/datatables/jquery.dataTables.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../include/header.jsp"%>
    <!-- =============================================== -->

    <jsp:include page="../include/sider.jsp">
        <jsp:param name="menu" value="task_delay"/>
    </jsp:include>

    <!-- =============================================== -->


    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">计划任务</h3>

                    <div class="box-tools pull-right">
                        <button class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新增任务</button>
                        <button class="btn btn-primary btn-sm"><i class="fa fa-eye"></i> 显示所有任务</button>
                    </div>
                </div>
                <div class="box-body">

                    <ul class="todo-list">
                        <li class="done">
                            <input type="checkbox">
                            <span class="text">给张三打电话联系</span>
                            <a href=""><i class="fa fa-user-o"></i> 张三</a>
                            <small class="label label-danger"><i class="fa fa-clock-o"></i> 7月15日</small>
                            <div class="tools">
                                <i class="fa fa-edit"></i>
                                <i class="fa fa-trash-o"></i>
                            </div>
                        </li>
                        <li>
                            <input type="checkbox">
                            <span class="text">给张三打电话联系</span>
                            <a href=""><i class="fa fa-money"></i> 9号楼23#</a>
                            <small class="label label-danger"><i class="fa fa-clock-o"></i> 8月3日</small>
                            <div class="tools">
                                <i class="fa fa-edit"></i>
                                <i class="fa fa-trash-o"></i>
                            </div>
                        </li>
                        <li>
                            <input type="checkbox">
                            <span class="text">给张三打电话联系</span>
                            <small class="label label-danger"><i class="fa fa-clock-o"></i> 8月5日</small>
                            <div class="tools">
                                <i class="fa fa-edit"></i>
                                <i class="fa fa-trash-o"></i>
                            </div>
                        </li>
                    </ul>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

        </section>

    </div>
    <!-- /.content-wrapper -->

    <%@include file="../include/footer.jsp"%>

</div>
<!-- ./wrapper -->
<%@include file="../include/js.jsp"%>
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/datatables/jquery.dataTables.js"></script>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script>
    $(function(){




    });
</script>
</body>
</html>
