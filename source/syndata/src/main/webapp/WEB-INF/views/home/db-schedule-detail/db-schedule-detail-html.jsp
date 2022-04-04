<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
	#scheduleDetail #highchart-paging .next {
		top: 10px !important;
	}

	#scheduleDetail #highchart-paging .back {
		top: 10px !important;
	}

	#scheduleDetail .modal-header .close {
		padding: 7px 12px;
	}
	#scheduleDetail .stt {
        width: 55px;
    }
	#scheduleDetail .date {
        width: 150px;
    }
	#scheduleDetail .status {
        width: 120px;
    }
	#scheduleDetail .action {
        width: 100px;
    }
	#scheduleDetail .text-bold{
		font-weight: bold;
	}
</style>

<section ng-controller="scheduleDetail">
	<div class="card" id="scheduleDetail">
		<div class="card-header header-elements-inline" ng-if="serviceName">
			<h5 class="card-title">Chi tiết Service {{serviceName}}</h5>
		</div>

		<div class="table-responsive" ng-if="serviceName">
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th class="stt text-center">STT</th>
						<th class="date">Thời gian</th>
						<th class="status text-center">Trạng thái</th>
						<th class="text-center">Files hoặc kết quả</th>
						<th class="action text-center">Thao tác</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="item in data">
						<td ng-if="item.ISGROUP" class="stt text-center"></td>
						<td ng-if="item.ISGROUP" colspan="4" class="text-bold">Ngày {{item.GROUPNAME}}</td>
						<td ng-if="!item.ISGROUP" class="stt text-center">{{item.STT}}</td>
						<td ng-if="!item.ISGROUP" class="date">{{item.DATE_DETAIL_TEXT}}</td>
						<td ng-if="!item.ISGROUP" class="status text-center">{{item.STATUS === 'SUCCESS'? 'Thành Công': 'Thất Bại'}}</td>
						<td ng-if="!item.ISGROUP">
							<div ng-repeat="f in item.FILES">
								<a href="${pageContext.request.contextPath}/open-file?fileName={{f.FILENAME}}"
									target="_blank">{{f.FILENAME}}</a>
							</div>
							<div ng-if="item.CONTENT">
								{{item.CONTENT}}
							</div>
						</td>
						<td ng-if="!item.ISGROUP" class="text-center action">
							<a href="javascript:;" ng-click="viewDetail(item.ID)" title="Xem chi tiết">
								<i class="icon-eye"></i>
							</a>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

		<high-chart-paging ng-model="page" ng-change="changeTime($event)" ng-if="serviceName"></high-chart-paging>
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

</section>