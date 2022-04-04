<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    #db-group-task{
        min-height: 400px;
    }

    .total-task #highchart-paging .next {
        right: 10px !important;
    }
</style>
<figure class="highcharts-figure total-task" ng-controller="totalTask">
    <div id="db-total-task"></div>
    <high-chart-paging ng-model="page" ng-change="changeTime($event)"></high-chart-paging>
</figure>

