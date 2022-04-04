<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
    #groupScheduleDetail #highchart-paging .next {
        right: 430px !important;
    }

    #db-group-schedule-detail {
        min-height: 400px;
    }
</style>
<figure id="groupScheduleDetail" class="highcharts-figure" ng-controller="groupScheduleDetail">
    <div ng-if="groupServiceName">
        <div id="db-group-schedule-detail"></div>
        <high-chart-paging ng-model="page" ng-change="changeTime($event)"></high-chart-paging>
    </div>
</figure>