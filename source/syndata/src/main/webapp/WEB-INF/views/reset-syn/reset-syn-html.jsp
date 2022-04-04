<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    .action {
        width: 100px;
    }

    .hidden {
        display: none;
    }

    .input-group-append {
        cursor: pointer;
    }
</style>
<div class="content" ng-controller="resetSyn">
    <div class="card" id="list-service">
        <div class="card-header header-elements-inline">
            <h5 class="card-title">Reset đồng bộ hệ thống</h5>
            <button type="button" class="btn btn-danger" ng-click="resetAll()">Reset tất cả</button>
        </div>

        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Tên hệ thống</th>
                        <th class="action">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="item in data">
                        <td>{{item.STT}}</td>
                        <td>{{item.NAME}}</td>
                        <td class="text-center">
                            <a href="javascript:;" ng-click="resetItem(item)" title="Reset đồng bộ">
                                <i class="icon-database-arrow"> </i>
                            </a>
                        </td>
                    </tr>
                    </ng-container>
                </tbody>
            </table>
        </div>
    </div>
</div>