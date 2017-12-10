<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-编辑客户</title>
    <%@include file="../include/css.jsp"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->

    <%@include file="../include/header.jsp"%>
    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->

    <jsp:include page="../include/sider.jsp">
        <jsp:param name="menu" value="customer"/>
    </jsp:include>
    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">新增客户</h3>
                    <div class="box-tools pull-right">
                        <a href="/customer/my"><button class="btn btn-primary btn-sm"><i class="fa fa-arrow-left"></i> 返回列表</button></a>
                    </div>
                </div>
                <div class="box-body">
                    <form action="" method="post" id="editForm">
                        <div class="form-group">
                            <label>姓名</label>
                            <input type="text" name="custName" class="form-control" value="${customer.custName}" >
                        </div>
                        <div class="form-group">
                            <label>职位</label>
                            <input type="text" name="jobTitle" class="form-control" value="${customer.jobTitle}">
                        </div>
                        <div class="form-group">
                            <label>联系方式</label>
                            <input type="text" name="mobile" class="form-control" value="${customer.mobile}">
                        </div>
                        <div class="form-group">
                            <label>地址</label>
                            <input type="text" name="address" class="form-control" value="${customer.address}">
                        </div>

                        <div class="form-group">
                            <label>所属行业</label>
                            <select name="trade" class="form-control">
                                <option value=""></option>
                                <c:forEach items="${trades}" var="trade">
                                    <option value="${trade}" ${customer.trade == trade ?  'selected' : ''}>${trade}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>客户来源</label>
                            <select name="source" class="form-control">
                                <option value=""></option>
                               <c:forEach items="${sources}" var="source">
                                   <option value="${source}" ${customer.source == source ? 'selected' : ''}>${source}</option>
                               </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>级别</label>
                            <select name="level" class="form-control">
                                <option value=""></option>
                                <option ${customer.level == '★' ? 'selected' : ''} value="★">★</option>
                                <option ${customer.level == '★★' ? 'selected' : ''} value="★★">★★</option>
                                <option ${customer.level == '★★★' ? 'selected' : ''} value="★★★">★★★</option>
                                <option ${customer.level == '★★★★' ? 'selected' : ''} value="★★★★">★★★★</option>
                                <option ${customer.level == '★★★★★' ? 'selected' : ''} value="★★★★★">★★★★★</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>备注</label>
                            <input type="text" name="mark" class="form-control"value="${customer.mark}">
                        </div>
                    </form>
                </div>
                <div class="box-footer">
                    <button class="btn btn-primary" id="editBtn">保存</button>
                </div>
                <!-- /.box-body -->
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
        $("#editBtn").click(function () {
            $("#editForm").submit();
        });
        $("#editForm").validate({
            errorClass:"text-danger",
            errorElement:"span",
            rules:{
                custName:{
                    required:true
                },
                mobile:{
                    required:true
                },
            },
            messages:{
                custName:{
                    required:"请输入客户姓名"
                },
                mobile:{
                    required:"请输入手机号码"
                }
            }
        });
    });
</script>
</body>
</html>
