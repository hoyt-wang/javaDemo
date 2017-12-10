<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>凯盛软件CRM-销售机会详情</title>
    <%@ include file="../include/css.jsp"%>
    <style>
        .td_title {
            font-weight: bold;
        }
        .star {
            font-size: 20px;
            color: #ff7400;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
    <%@include file="../include/header.jsp"%>
    <!-- =============================================== -->

    <jsp:include page="../include/sider.jsp">
        <jsp:param name="menu" value="sales_my"/>
    </jsp:include>
    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">销售机会基本资料</h3>
                    <div class="box-tools">
                        <a href="/record/my" class="btn btn-primary btn-sm"><i class="fa fa-arrow-left"></i> 返回列表</a>
                        <button id="delBtn" class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> 删除</button>
                    </div>
                </div>
                <div class="box-body no-padding">
                    <table class="table">
                        <tr>
                            <td class="td_title">机会名称</td>
                            <td>${saleChance.name}</td>
                            <td class="td_title">价值</td>
                            <td><fmt:formatNumber value="${saleChance.worth}"/> </td>
                            <td class="td_title">当前进度</td>
                            <td>
                                ${saleChance.progress}
                                <button class="btn btn-xs btn-success" id="showChangeProgressModalBtn"><i class="fa fa-pencil"></i></button>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="box-footer">
                    <span style="color: #ccc" class="pull-right">
                        创建日期：<fmt:formatDate value="${saleChance.createTime}" pattern="yyyy-MM-dd HH:mm"/>
                    </span>
                </div>
            </div>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">关联客户资料</h3>
                </div>
                <div class="box-body no-padding">
                    <table class="table">
                        <tr>
                            <td class="td_title">姓名</td>
                            <td>${saleChance.customer.custName}</td>
                            <td class="td_title">职位</td>
                            <td>${saleChance.customer.jobTitle}</td>
                            <td class="td_title">联系电话</td>
                            <td>${saleChance.customer.mobile}</td>
                        </tr>
                        <tr>
                            <td class="td_title">所属行业</td>
                            <td>${saleChance.customer.trade}</td>
                            <td class="td_title">客户来源</td>
                            <td>${saleChance.customer.source}</td>
                            <td class="td_title">级别</td>
                            <td class="star">${saleChance.customer.level}</td>
                        </tr>
                        <c:if test="${not empty saleChance.customer.address}">
                            <tr>
                                <td class="td_title">地址</td>
                                <td colspan="5">${saleChance.customer.address}</td>
                            </tr>
                        </c:if>
                        <c:if test="${not empty saleChance.customer.mark}">
                            <tr>
                                <td class="td_title">备注</td>
                                <td colspan="5">${saleChance.customer.mark}</td>
                            </tr>
                        </c:if>
                    </table>
                </div>
            </div>

            <div class="row">
                <div class="col-md-8">
                    <h4>跟进记录
                        <small><button id="showRecordModalBtn" class="btn btn-success btn-xs"><i class="fa fa-plus"></i></button></small>
                    </h4>
                    <ul class="timeline">
                        <c:if test="${empty recordList}">
                            <li>
                                <!-- timeline icon -->
                                <i class="fa fa-circle-o bg-red"></i>
                                <div class="timeline-item">
                                    <div class="timeline-body">
                                        暂无跟进记录
                                    </div>
                                </div>
                            </li>
                        </c:if>
                        <c:forEach items="${recordList}" var="record">
                            <c:choose>
                                <c:when test="${record.content == '将当前进度修改为：[ 成交 ]'}">
                                    <li>
                                        <!-- timeline icon -->
                                        <i class="fa fa-check bg-green"></i>
                                        <div class="timeline-item">
                                            <span class="time"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${record.createTime}"/></span>
                                            <div class="timeline-body">
                                                    ${record.content}
                                            </div>
                                        </div>
                                    </li>
                                </c:when>
                                <c:when test="${record.content == '将当前进度修改为：[ 暂时搁置 ]'}">
                                    <li>
                                        <!-- timeline icon -->
                                        <i class="fa fa-close bg-red"></i>
                                        <div class="timeline-item">
                                            <span class="time"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${record.createTime}"/></span>
                                            <div class="timeline-body">
                                                    ${record.content}
                                            </div>
                                        </div>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li>
                                        <!-- timeline icon -->
                                        <i class="fa fa-info bg-blue"></i>
                                        <div class="timeline-item">
                                            <span class="time"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${record.createTime}"/></span>
                                            <div class="timeline-body">
                                                    ${record.content}
                                            </div>
                                        </div>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </ul>
                </div>
                <div class="col-md-4">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">待办事项</h3>
                            <div class="box-tools">
                                <button class="btn btn-sm btn-default" id="showAddTaskModal"><i class="fa fa-plus"></i></button>
                            </div>
                        </div>
                        <div class="box-body">
                            <ul class="todo-list">
                                <c:forEach items="${taskList}" var="task">
                                    <c:if test="${task.done==0}">
                                        <%-- <li class="${task.done==1 ? 'done' : ''}">
                                             <input type="checkbox" class="taskCheckbox" ${task.done==1 ? 'checked' : ''} value="${task.id}">
     --%>

                                            <div class="form-group">
                                                <a href="/task/list"> <span class="text">${task.title}</span></a>
                                                <small class="label label-danger"><i class="fa fa-clock-o"></i> ${task.finishTime}</small>
                                            </div>

                                        <%--<div class="tools">
                                            <i class="fa fa-edit"></i>
                                            <i class="fa fa-trash-o"></i>
                                        </div>--%>

                                        <%--</li>--%>
                                    </c:if>
                                </c:forEach>
                            </ul>

                        </div>
                    </div>
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">相关资料</h3>
                        </div>
                        <div class="box-body">

                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="recordModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">添加跟进记录</h4>
                        </div>
                        <div class="modal-body">
                            <form action="/record/my/new/record" id="recordForm" method="post">
                                <input type="hidden" name="saleId" value="${saleChance.id}">
                                <textarea id="recordContent"  class="form-control" name="content"></textarea>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="saveRecordBtn">保存</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->

            <%--更改当前进度Modal--%>
            <div class="modal fade" id="changeProgressModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">更新当前进度</h4>
                        </div>
                        <div class="modal-body">
                            <form method="post" action="/record/my/${saleChance.id}/progress/update" id="updateProgressForm">
                                <input type="hidden" name="id" value="${saleChance.id}">
                                <select name="progress" class="form-control">
                                    <c:forEach items="${progressList}" var="progress">
                                        <option value="${progress}">${progress}</option>
                                    </c:forEach>
                                </select>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="saveProgress">更新</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->

            <%--添加新任务Modal--%>
            <div class="modal fade" id="taskModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">新增待办事项</h4>
                        </div>
                        <div class="modal-body">
                            <form action="/record/my/${saleChance.id}/task/new" method="post" id="saveTaskForm">
                                <input type="hidden" name="accountId" value="${sessionScope.curr_account.id}">
                                <input type="hidden" name="saleId" value="${saleChance.id}">
                                <div class="form-group">
                                    <label>任务名称</label>
                                    <input type="text" class="form-control" name="title">
                                </div>
                                <div class="form-group">
                                    <label>完成日期</label>
                                    <input type="text" class="form-control" id="datepicker" name="finishTime">
                                </div>
                                <div class="form-group">
                                    <label>提醒时间</label>
                                    <input type="text" class="form-control" id="datepicker2" name="remindTime">
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="saveTaskBtn">保存</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <%@ include file="../include/footer.jsp"%>

</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script src="/static/plugins/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script src="/static/plugins/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="/static/plugins/moment/moment.js"></script>
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script>
    $(function () {

        var salesId = ${saleChance.id};
        //删除
        $("#delBtn").click(function () {
            layer.confirm("确定要删除该机会吗?",function () {
                window.location.href = "/record/my/"+salesId+"/delete";
            });
        });

        //添加跟进记录
        $("#showRecordModalBtn").click(function () {
            $("#recordModal").modal({
                show:true,
                backdrop:'static'
            });
        });
        $("#saveRecordBtn").click(function () {
            if(!$("#recordContent").val()) {
                layer.msg("请输入跟进内容");
                return ;
            }
            $("#recordForm").submit();
        });
        //更改当前进度
        $("#showChangeProgressModalBtn").click(function () {
            $("#changeProgressModal").modal({
                show : true,
                backdrop:'static'
            });
        });


        $("#saveProgress").click(function () {
            $("#updateProgressForm").submit();
        });

        var picker = $('#datepicker').datepicker({
            format: "yyyy-mm-dd",
            language: "zh-CN",
            autoclose: true,
            todayHighlight: true,
            startDate:moment().format("yyyy-MM-dd")
        });
        picker.on("changeDate",function (e) {
            var today = moment().format("YYYY-MM-DD");
            $('#datepicker2').datetimepicker('setStartDate',today);
            $('#datepicker2').datetimepicker('setEndDate', e.format('yyyy-mm-dd'));
        });


        var timepicker = $('#datepicker2').datetimepicker({
            format: "yyyy-mm-dd hh:ii",
            language: "zh-CN",
            autoclose: true,
            todayHighlight: true
        });
        //添加待办事项
        $("#showAddTaskModal").click(function () {
            $("#taskModal").modal({
                show:true,
                backdrop:'static'
            });
        });
        $("#saveTaskBtn").click(function () {
            $("#saveTaskForm").submit();
        });

        $("#saveTaskForm").validate({
            errorClass:"text-danger",
            errorElement:"span",
            rules:{
                title:{
                    required:true
                },
                finishTime:{
                    required:true
                }
            },
            messages:{
                title:{
                    required:"请输入任务内容"
                },
                finishTime:{
                    required:"请选择完成时间"
                }
            }
        });




    })
</script>

</body>
</html>
