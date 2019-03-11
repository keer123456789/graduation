$(function(){




    /**
     * 点击链接按钮事件
     */
    $("#btn_conn").click(function(){
        $.get({
            url: "/farmreception",
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            }
        },function (allData){

        });
    });



    /**
     * 点击运行按钮事件
     */
    $("#btn_run").click(function(){
        $.get({
            url: "/farmreception",
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            }
        },function (allData){

        });
    });





});