<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    .action {
        width: 100px;
    }

    .form-search {
        display: inline-block;
        width: 350px;
    }

    .hidden {
        display: none;
    }

    .input-group-append {
        cursor: pointer;
    }
</style>
<div class="content" ng-controller="listService">
    <div class="card" id="list-service">
        <div class="card-header header-elements-inline">
            <h5 class="card-title">Danh sách cấu hình service</h5>
            <div class="form-search">
                <form ng-submit="getData()">
                    <div class="input-group">
                        <input type="text" placeholder="Nhập tên nhóm hoặc tên service" class="form-control"
                            ng-model="searchKey">
                        <span class="input-group-append" ng-click="getData()">
                            <span class="input-group-text">
                                <i class="icon-search4"></i>
                            </span>
                        </span>
                    </div>
                    <button type="submit" class="hidden"></button>
                </form>
            </div>
            <button type="button" class="btn btn-success" ng-click="addService()">Thêm service</button>
        </div>

        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Tên service</th>
                        <th>Lịch</th>
                        <th>Trạng thái</th>
                        <th>Khóa</th>
                        <th class="action">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="item in data">
                        <td ng-if="item.ISGROUP"></td>
                        <td ng-if="item.ISGROUP" colspan="5" class="font-weight-bold">{{item.GROUPNAME}}</td>
                        <td ng-if="!item.ISGROUP">{{item.STT}}</td>
                        <td ng-if="!item.ISGROUP">{{item.SCHEDULE_SETTING_NAME}}</td>
                        <td ng-if="!item.ISGROUP">{{item.SCHEDULE_SETTING_CRON}}</td>
                        <td ng-if="!item.ISGROUP">
                            <span ng-if="item.STATUS === 1" class="badge badge-success">Đang chạy</span>
                            <span ng-if="item.STATUS === 0" class="badge badge-danger">Đang dừng</span>
                        </td>
                        <td ng-if="!item.ISGROUP">{{item.IP_LOCK}}</td>
                        <td ng-if="!item.ISGROUP" class="text-center">
                            <a href="javascript:;" ng-click="viewDetail(item)" title="Sửa service"> <i
                                    class="icon-pencil7"></i> </a>

                            <a href="javascript:;" ng-click="changeStatusService(item)">
                                <i class="icon-play4" title="Chạy service" ng-if="item.STATUS === 0"></i>
                                <i class="icon-stop2" title="Dừng service" ng-if="item.STATUS === 1"></i>
                            </a>

                            <a href="javascript:;" ng-click="runJob(item)" title="Chạy 1 lần ngay bây giờ"> <i
                                    class="icon-database-insert"></i> </a>

                            <a target="_black" href="${pageContext.request.contextPath}/log?serviceName={{item.SCHEDULE_SETTING_NAME}}" title="Xem lịch sử"> 
                                &nbsp; <i class="icon-database-time2"> </i> </a>

                            <a href="javascript:;" ng-if="user && user.USER_LEVEL >= 4" ng-click="delete(item.SCHEDULE_SETTING_ID)" title="Xóa tác vụ chạy nền"> 
                                &nbsp; <i class="icon-trash-alt text-danger"></i> </a>
                        </td>
                    </tr>
                    </ng-container>
                </tbody>
            </table>
        </div>

        <vnpt-paging ng-model="paging" ng-change="changePage($event)"></vnpt-paging>
    </div>

    <div id="detail_modal" class="modal fade" role="dialog" style="display: none;">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Thông tin mở rộng</h5>
                    <button type="button" class="close" ng-click="closeViewDetail()">×</button>
                </div>
                <div class="modal-body">
                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Tên nhóm:</label>
                        <div class="col-lg-10">
                            <input type="text" required class="form-control" ng-model="detail.SCHEDULE_SETTING_GROUP">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Tên service:</label>
                        <div class="col-lg-10">
                            <input type="text" required class="form-control" ng-model="detail.SCHEDULE_SETTING_NAME">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Chế độ:</label>
                        <div class="col-lg-10">
                            <select class="custom-select" ng-model="detail.SCHEDULE_SETTING_LOOP">
                                <option ng-value="0">Lặp lại theo số lần cấu hình</option>
                                <option ng-value="1">Lặp lại vô tận</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row" ng-if="detail.SCHEDULE_SETTING_LOOP === 0">
                        <label class="col-form-label col-lg-2">Số lần thực hiện:</label>
                        <div class="col-lg-10">
                            <input type="number" class="form-control" ng-model="detail.SCHEDULE_SETTING_TURN">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Số lần thực hiện lại khi gặp lỗi:</label>
                        <div class="col-lg-10">
                            <input type="number" class="form-control" ng-model="detail.SCHEDULE_SETTING_RETRY">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Lịch:</label>
                        <div class="col-lg-10">
                            <input type="text" required class="form-control" ng-model="detail.SCHEDULE_SETTING_CRON">
                            <i>Tham khảo cấu hình tại website: <a target="_blank"
                                    href="https://www.freeformatter.com/cron-expression-generator-quartz.html">click
                                    here</a></i>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Job thực hiện:</label>
                        <div class="col-lg-10">
                            <select class="custom-select" ng-model="detail.SCHEDULE_SETTING_FUNCTION"
                                ng-change="changeFunction()">
                                <option value="createFileJob">CreateFileJob</option>
                                <option value="receiveFileJob">ReceiveFileJob</option>
                                <option value="synDataJob">SynDataJob</option>
                                <option value="executeApiJob">ExecuteApiJob</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Trạng thái:</label>
                        <div class="col-lg-10">
                            <select class="custom-select" ng-model="detail.STATUS">
                                <option ng-value="0">Dừng</option>
                                <option ng-value="1">Chạy</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Param:</label>
                        <div class="col-lg-10">
                            <div id="jsonParam" style="width: 100%; height: 350px;"></div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary mr-2" ng-click="submitForm()"
                        class="btn btn-default">Lưu</button>
                    <button type="button" class="btn btn-default" ng-click="closeViewDetail()"
                        class="btn btn-default">Đóng</button>
                </div>
            </div>
        </div>
    </div>
</div>