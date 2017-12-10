<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM | 待办事项列表</title>
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
        <jsp:param name="menu" value="task_list"/>
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
                        <a href="/task/new" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新增任务</a>
                        <c:choose>
                            <c:when test="${not (param.show == 'all')}">
                                <a href="/task/list?show=all" class="btn btn-primary btn-sm"><i class="fa fa-eye"></i> 显示所有任务</a>
                            </c:when>
                            <c:otherwise>
                                <a href="/task/list" class="btn btn-primary btn-sm"><i class="fa fa-eye"></i> 显示未完成任务</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="box-body">

                    <ul class="todo-list">
                        <c:forEach items="${taskList}" var="task">
                            <li class="${task.done==1 ? 'done' : ''}">
                                <input type="checkbox" class="taskCheckbox" ${task.done==1 ? 'checked' : ''} value="${task.id}">
                                <span class="text">${task.title}</span>
                                     <c:choose>
                                         <c:when test="${not empty task.customer and not empty task.customer.id}">
                                             <a href="/customer/my/${task.customer.id}"><i class="fa fa-user-o"></i> ${task.customer.custName}</a>
                                         </c:when>
                                         <c:when test="${not empty task.saleChance and not empty task.saleId}">
                                             <a href="/record/my/${task.saleChance.id}"><i class="fa fa-money"></i> ${task.saleChance.name}</a>
                                         </c:when>
                                     </c:choose>
                                <small class="label ${task.overTime ? 'label-danger' : 'label-success'}"><i class="fa fa-clock-o"></i> ${task.finishTime}</small>
                                <div class="tools">
                                    <i class="fa fa-edit"></i>
                                    <i class="fa fa-trash-o delTask" rel="${task.id}"></i>
                                </div>
                            </li>
                        </c:forEach>

                    </ul>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
            <div class="modal fade" id="editModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">编辑待办事项</h4>
                        </div>
                        <div class="modal-body">
                            <form action="/task/list/${task.id}/edit" id="editForm" method="post">
                                <textarea id="recordContent"  class="form-control" name="content"></textarea>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="editBtn">保存</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->

        </section>
        <!-- /.content -->
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

        //删除
        $(".delTask").click(function () {
            var id = $(this).attr("rel");
            layer.confirm("确定要删除吗",function () {
                window.location.href = "/task/"+id+"/del";
            });
        });

        //修改状态
        $(".taskCheckbox").click(function () {
            var id = $(this).val();
            var checked = $(this)[0].checked;
            if(checked) {
                window.location.href = "/task/"+id+"/state/done";
            } else {
                window.location.href = "/task/"+id+"/state/undone"
            }
        });


    });
</script>
</body>
</html>
