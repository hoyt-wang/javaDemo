<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>凯盛软件CRM-客户相关报表</title>
    <%@ include file="../include/css.jsp" %>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
    <jsp:include page="../include/sider.jsp">
        <jsp:param name="active" value="charts_customer"/>
    </jsp:include>
    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <div class="box">
                <div class="box-header with-border ">
                    <h3 class="box-title">客户当前进度统计</h3>
                </div>
                <div class="box-body">
                    <div id="bar" style="height: 300px;width: 100%"></div>
                </div>
            </div>


            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">每月客户增加统计</h3>
                </div>
                <div class="box-body">
                    <div id="line" style="height: 300px;width: 100%"></div>
                </div>
            </div>


        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <%@ include file="../include/footer.jsp" %>

</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp" %>
<script src="/static/plugins/echarts/echarts.min.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script>
    $(function () {


        var bar = echarts.init(document.getElementById("bar"));

        option = {
            title: {
                left:'left'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c}%"
            },
            legend: {
                data: []
            },
            calculable: true,
            series: [
                {
                    name:'客户当前进度统计',
                    type:'funnel',
                    left: '10%',
                    top: 60,
                    //x2: 80,
                    bottom: 60,
                    width: '80%',
                    // height: {totalHeight} - y - y2,
                    min: 0,
                    max: 100,
                    minSize: '0%',
                    maxSize: '100%',
                    sort: 'descending',
                    gap: 2,
                    label: {
                        normal: {
                            show: true,
                            position: 'inside'
                        },
                        emphasis: {
                            textStyle: {
                                fontSize: 20
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            length: 10,
                            lineStyle: {
                                width: 1,
                                type: 'solid'
                            }
                        }
                    },
                    itemStyle: {
                        normal: {
                            borderColor: '#fff',
                            borderWidth: 1
                        }
                    },
                    data: []
                }
            ]
        };
        bar.setOption(option);

        $.get("/charts/list/bar.json").done(function (resp) {
            var progressArray = [];
            var dataArray = [];

            for(var i = 0;i < resp.data.length;i++) {
                var obj = resp.data[i];
                progressArray.push(obj.progress);
                dataArray.push({value:obj.count,name:obj.progress});

            }
            bar.setOption({
                legend: {
                    data:progressArray
                },
                series: {
                    data: dataArray
                }
            });


        }).error(function () {
            layer.msg("获取数据异常");
        });

        var line = echarts.init(document.getElementById("line"));
        line.setOption({
            title: {

                left: 'center'
            },
            tooltip: {},
            legend: {
                data: ['人数'],
                left: 'right'
            },
            xAxis: {
                type: 'category',
                data: []
            },
            yAxis: {},
            series: {
                name: "月份",
                type: 'line',
                data: []
            }
        });

        bar.setOption(option);

        $.get("/charts/list/newCustomer/count").done(function (resp) {
            if(resp.state == "success") {

                var nameArray = [];
                var valueArray = [];

                var dataArray = resp.data;
                for(var i = 0;i < dataArray.length;i++) {
                    var obj = dataArray[i];
                    nameArray.push(obj.count);
                    valueArray.push(obj.month);
                }

                line.setOption({
                    xAxis:{
                        data:valueArray
                    },
                    series:{
                        data: nameArray
                    }
                });


            } else {
                layer.msg(resp.message);
            }
        }).error(function () {
            layer.msg("加载数据异常");
        });

    });
</script>

</body>
</html>
