<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-首页</title>
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
                        <button type="button btn-primary" class="btn btn-box-tool">
                            <a href="/record/new"><i class="fa fa-plus"></i> 添加记录</a>
                        </button>
                    </div>
                </div>
                <div class="box-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <td>机会名称</td>
                            <td>关联客户</td>
                            <td>机会价值</td>
                            <td>当前进度</td>
                            <td>最后跟进时间</td>

                        </tr>
                        </thead>
                        <tbody>
                           <c:forEach items="${pageInfo.list}" var="saleChance">
                               <tr class="dataRow" rel="${saleChance.id}">
                                   <td>${saleChance.name}</td>
                                   <td>${saleChance.customer.custName}</td>
                                   <td id="digit"><fmt:formatNumber value="${saleChance.worth}"/> </td>
                                   <td>
                                       <c:choose>
                                           <c:when test="${saleChance.progress == '成交'}">
                                               <span class="label label-success">${saleChance.progress}</span>
                                           </c:when>
                                           <c:when test="${saleChance.progress == '暂时搁置'}">
                                               <span class="label label-danger">${saleChance.progress}</span>
                                           </c:when>
                                           <c:otherwise>
                                               ${saleChance.progress}
                                           </c:otherwise>
                                       </c:choose>
                                   </td>
                                   <td><fmt:formatDate value="${saleChance.lastTime}"/></td>

                               </tr>
                           </c:forEach>
                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
        <%--页码小于1页不显示--%>
        <c:if test="${pageInfo.pages > 1}">
            <div class="box-footer">
                <ul id="pagination-demo" class="pagination-sm"></ul>
            </div>
        </c:if>
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
    <%@include file="../include/footer.jsp"%>

</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>
<script src="/static/plugins/twbsPagination/jquery.twbsPagination.js"></script>

<script>
    $(function () {
        $('#pagination-demo').twbsPagination({
            totalPages: ${pageInfo.pages},
            visiblePages: 5,
            first:'首页',
            last:'末页',
            prev:'上一页',
            next:'下一页',
            href:"?&p={{number}}"
        });
        $(".dataRow").click(function () {
            var id = $(this).attr("rel");
            window.location.href = "/record/my/"+id;
        });

        /*function clickchange(obj){
            var aaval=obj.value;
            alert(aaval);
            document.getElementById("bb").value=ChinaCost(aaval);
        }

        function ChinaCost(numberValue){
            var numberValue=new String(Math.round(numberValue*100)); // 数字金额
            var chineseValue=""; // 转换后的汉字金额
            var String1 = "零壹贰叁肆伍陆柒捌玖"; // 汉字数字
            var String2 = "万仟佰拾亿仟佰拾万仟佰拾元角分"; // 对应单位
            var len=numberValue.length; // numberValue 的字符串长度
            var Ch1; // 数字的汉语读法
            var Ch2; // 数字位的汉字读法
            var nZero=0; // 用来计算连续的零值的个数
            var String3; // 指定位置的数值
            if(len>15){
                alert("超出计算范围");
                return "";
            }
            if (numberValue==0){
                chineseValue = "零元整";
                return chineseValue;
            }
            String2 = String2.substr(String2.length-len, len); // 取出对应位数的STRING2的值
            for(var i=0; i<len; i++){
                String3 = parseInt(numberValue.substr(i, 1),10); // 取出需转换的某一位的值
                if ( i != (len - 3) && i != (len - 7) && i != (len - 11) && i !=(len - 15) ){
                    if ( String3 == 0 ){
                        Ch1 = "";
                        Ch2 = "";
                        nZero = nZero + 1;
                    }
                    else if ( String3 != 0 && nZero != 0 ){
                        Ch1 = "零" + String1.substr(String3, 1);
                        Ch2 = String2.substr(i, 1);
                        nZero = 0;
                    }
                    else{
                        Ch1 = String1.substr(String3, 1);
                        Ch2 = String2.substr(i, 1);
                        nZero = 0;
                    }
                }
                else{ // 该位是万亿，亿，万，元位等关键位
                    if( String3 != 0 && nZero != 0 ){
                        Ch1 = "零" + String1.substr(String3, 1);
                        Ch2 = String2.substr(i, 1);
                        nZero = 0;
                    }
                    else if ( String3 != 0 && nZero == 0 ){
                        Ch1 = String1.substr(String3, 1);
                        Ch2 = String2.substr(i, 1);
                        nZero = 0;
                    }
                    else if( String3 == 0 && nZero >= 3 ){
                        Ch1 = "";
                        Ch2 = "";
                        nZero = nZero + 1;
                    }
                    else{
                        Ch1 = "";
                        Ch2 = String2.substr(i, 1);
                        nZero = nZero + 1;
                    }
                    if( i == (len - 11) || i == (len - 3)){ // 如果该位是亿位或元位，则必须写上
                        Ch2 = String2.substr(i, 1);
                    }
                }
                chineseValue = chineseValue + Ch1 + Ch2;
            }
            if ( String3 == 0 ){ // 最后一位（分）为0时，加上“整”
                chineseValue = chineseValue + "整";
            }
            return chineseValue;
        }*/
    });
</script>

</body>
</html>
