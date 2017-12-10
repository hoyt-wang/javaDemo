<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-新建记录</title>
    <%@include file="../include/css.jsp"%>
    <style>
        .name-avatar {
            display: inline-block;
            width: 50px;
            height: 50px;
            background-color: #ccc;
            border-radius: 50%;
            text-align: center;
            line-height: 50px;
            font-size: 24px;
            color: #FFF;
        }
        .table>tbody>tr:hover {
            cursor: pointer;
        }
        .table>tbody>tr>td {
            vertical-align: middle;
        }

    </style>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../include/header.jsp"%>

    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->
    <jsp:include page="../include/sider.jsp">
        <jsp:param name="menu" value="record_my"/>
    </jsp:include>
    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">我的工作记录</h3>

                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool">
                            <i class="fa fa-plus"></i> 添加记录
                        </button>
                    </div>
                </div>
                <div class="box-body">
                    <form action="" method="post" id="addForm">
                        <input type="hidden" name="accountId" value="<shiro:principal property="id"/> ">
                        <div class="form-group">
                            <label>记录名称</label>
                            <input type="text" name="name" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>关联客户</label>
                            <select name="custId" class="form-control">
                                <option value=""></option>
                                <c:forEach items="${customerList}" var="customer">
                                    <option value="${customer.id}">${customer.custName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label id="content">机会价值</label>
                            <input type="text" name="worth" id="worth" class="form-control">
                        </div>

                        <div class="form-group">
                            <label>当前进度</label>
                            <select name="progress" class="form-control">
                                <c:forEach items="${progressList}" var="progress">
                                    <option value="${progress}">${progress}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>详细内容</label>
                            <textarea name="content" class="form-control"></textarea>
                        </div>
                    </form>
                </div>
                <!-- /.box-body -->
                <div class="box-footer">
                    <button class="btn btn-primary" id="addBtn">保存</button>
                </div>
            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
    <%@include file="../include/footer.jsp"%>
</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>

<script>
    $(function () {
      $("#addBtn").click(function () {
          $("#addForm").submit();
      });

        $("#addForm").validate({
            errorClass:"text-danger",
            errorElement:"span",
            rules:{
                name:{
                    required:true
                },
                custId:{
                    required:true
                },
                worth:{
                    required:true,
                    number:true,
                    min:1
                }
            },
            messages:{
                name:{
                    required:"请输入机会名称"
                },
                custId:{
                    required:"请选择关联客户"
                },
                worth:{
                    required:"请输入机会价值",
                    number:"请输入有效的价值",
                    min:"请输入有效的价值"
                }
            }
        });

        $("#worth").keyup(function () {
            var temp = $("#worth").val();
            if(temp.length < 14){
                var unit = "千百拾亿千百拾万千百拾元", str = "";
                unit = unit.substr(unit.length - temp.length);
                for (var i=0; i < temp.length; i++){
                    str += '零壹贰叁肆伍陆柒捌玖'.charAt(temp.charAt(i)) + unit.charAt(i);
                    $("#content").html("机会价值("+str+")");
                }
            } else {
                layer.message("数字太大");
                $("#content").html("机会价值(元)");
            }
        });



    });
</script>

</body>
</html>
