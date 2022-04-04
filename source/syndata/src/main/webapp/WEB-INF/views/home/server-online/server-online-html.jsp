<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<div class="card" ng-controller="serverOnline">
    <div class="card-header header-elements-sm-inline">
        <h6 class="card-title">Danh sách máy chủ kết nối với hệ thống</h6>
        <div class="header-elements">
            <span class="badge bg-success badge-pill">{{data.length}} máy chủ</span>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table text-nowrap">
            <thead>
                <tr>
                    <th>IP máy chủ</th>
                    <th>Số job đang sử lý</th>
                    <th>Online gần nhất</th>
                    <th>Trạng thái</th>
                    <!-- <th class="text-center" style="width: 20px;"><i class="icon-arrow-down12"></i></th> -->
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="item in data">
                    <td>
                        <div class="d-flex align-items-center">
                            <div>
                                <div class="text-muted font-size-sm">
                                    <span class="badge badge-mark border-blue mr-1"></span>
                                    {{item.IP_SERVER}}
                                </div>
                            </div>
                        </div>
                    </td>
                    <td><span class="text-muted">{{item.QUANTITY_JOB}}</span></td>
                    <td><span>{{item.LASTTIME}}</span></td>
                    <td><span class="badge" 
                        ng-class="{'bg-blue': item.STATUS === 'Online','bg-danger': item.STATUS === 'Offline'}">{{item.STATUS}}</span></td>
                    <!-- <td class="text-center">
                        <div class="list-icons">
                            <div class="list-icons-item dropdown">
                                <a href="#" class="list-icons-item dropdown-toggle caret-0" data-toggle="dropdown"><i class="icon-menu7"></i></a>
                                <div class="dropdown-menu dropdown-menu-right">
                                    <a href="#" class="dropdown-item"><i class="icon-file-stats"></i> View statement</a>
                                    <a href="#" class="dropdown-item"><i class="icon-file-text2"></i> Edit campaign</a>
                                    <a href="#" class="dropdown-item"><i class="icon-file-locked"></i> Disable campaign</a>
                                    <div class="dropdown-divider"></div>
                                    <a href="#" class="dropdown-item"><i class="icon-gear"></i> Settings</a>
                                </div>
                            </div>
                        </div>
                    </td> -->
                </tr>
            </tbody>
        </table>
    </div>
</div>