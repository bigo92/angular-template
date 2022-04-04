<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    #highchart-paging {
        width: 100%;
        position: relative;
    }

    #highchart-paging .back {
        position: absolute;
        top: -40px;
        left: 10px;
        border: solid 1px #ccc;
        border-radius: 3px;
        padding: 1px 7px;
        cursor: pointer;
    }

    #highchart-paging .next {
        position: absolute;
        top: -40px;
        right: 10px;
        border: solid 1px #ccc;
        border-radius: 3px;
        padding: 1px 7px;
        cursor: pointer;
    }
</style>
<div id="highchart-paging">
    <div class="back" ng-click="changePageByHighChart(curentPage + 1)">Trở lại</div>
    <div class="next" ng-click="changePageByHighChart(curentPage - 1)">Tiếp</div>
</div>