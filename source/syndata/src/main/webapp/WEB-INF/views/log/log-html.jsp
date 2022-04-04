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
<div class="content" ng-controller="listLog">
    <div class="card" id="list-log">
        <div class="card-header header-elements-inline">
            <h5 class="card-title">Danh sách lịch sử</h5>

            <div class="form-search">
                <form ng-submit="getData()">
                    <div class="input-group">
                        <input type="text" placeholder="Nhập tên service" class="form-control" ng-model="searchKey">
                        <span class="input-group-append" ng-click="getData()">
                            <span class="input-group-text">
                                <i class="icon-search4"></i>
                            </span>
                        </span>
                    </div>
                    <button type="submit" class="hidden"></button>
                </form>
            </div>

            <button type="button" class="btn btn-danger opacity-0">Xóa log</button>
        </div>

        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Mã log</th>
                        <th>Trạng thái</th>
                        <th>Ip máy chủ</th>
                        <th>Thời gian thực hiện</th>
                        <th>Tệp tin Hoặc kết quả</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="item in data">
                        <td ng-if="item.ISSERVICE"></td>
                        <td ng-if="item.ISSERVICE" colspan="6" class="font-weight-bold">{{item.SERVICENAME}}</td>
                        <td ng-if="!item.ISSERVICE">{{item.STT}}</td>
                        <td ng-if="!item.ISSERVICE">{{item.SCHEDULE_LOG_ID}}</td>
                        <td ng-if="!item.ISSERVICE">{{item.SCHEDULE_LOG_RETURN.STATUS === 'SUCCESS'? 'Thành Công': 'Thất Bại'}}</td>
                        <td ng-if="!item.ISSERVICE">{{item.IP_SERVER}}</td>
                        <td ng-if="!item.ISSERVICE">{{item.CREATED_TIME_TEXT}}</td>
                        <td ng-if="!item.ISSERVICE">
                            <div ng-if="item.SCHEDULE_LOG_RETURN.STATUS === 'SUCCESS' && 
                                        item.SCHEDULE_LOG_RETURN.FILES">
                                <div ng-repeat="f in item.SCHEDULE_LOG_RETURN.FILES">
                                    <a href="${pageContext.request.contextPath}/open-file?fileName={{f.FILENAME}}"
                                        target="_blank">{{f.FILENAME}}</a>
                                </div>
                            </div>
                            <div ng-if="item.SCHEDULE_LOG_RETURN.STATUS === 'SUCCESS' && item.SCHEDULE_LOG_FILE">
                                {{item.SCHEDULE_LOG_FILE}}
                            </div>
                        </td>
                        <td ng-if="!item.ISSERVICE" class="text-center">
                            <a href="javascript:;" ng-click="viewDetail(item.SCHEDULE_LOG_ID)">
                                <i class="icon-pencil7"></i>
                            </a>

                            <a href="javascript:;" ng-if="user && user.USER_LEVEL >= 4" ng-click="delete(item)" title="Xóa tác vụ chạy nền"> 
                                &nbsp; <i class="icon-trash-alt text-danger"></i> </a>
                        </td>
                    </tr>
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
					<div>
						Ip: {{detail.IP_SERVER}}
					</div>
					<div class="form-group">
						<label for="recipient-name" class="col-form-label">Param:</label>
						<div id="jsonParam" style="width: 100%; height: 200px;"></div>
					</div>
					<div class="form-group">
						<label for="recipient-name" class="col-form-label">Result:</label>
						<div id="jsonResult" style="width: 100%; height: 400px;"></div>
					</div>
					<div class="form-group">
						<label for="message-text" class="col-form-label">Message:</label>
						<textarea class="form-control" id="message-text">{{detail.SCHEDULE_LOG_MESSAGE}}</textarea>
					</div>
					<div class="form-group">
						<label for="message-text" class="col-form-label">Trace:</label>
						<textarea class="form-control" id="message-text">{{detail.SCHEDULE_LOG_TRACE}}</textarea>
					</div>
					<div class="form-group">
						<label for="message-text" class="col-form-label">Retry:</label>
						<div id="jsonRetry" style="width: 100%; height: 400px;"></div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" ng-click="closeViewDetail()" class="btn btn-default">Đóng</button>
				</div>
			</div>
		</div>
	</div>
</div>