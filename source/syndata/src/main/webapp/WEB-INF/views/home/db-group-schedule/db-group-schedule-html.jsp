<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    #group-schedule #highchart-paging .next {
        right: 265px !important;
    }

    #db-group-schedule{
        min-height: 400px;
    }
</style>
<figure id="group-schedule" class="highcharts-figure" ng-controller="groupSchedule">
    <div id="db-group-schedule"></div>
    <high-chart-paging ng-model="page" ng-change="changeTime($event)"></high-chart-paging>
</figure>

