<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="content" ng-controller="setting">
    <!-- Form inputs -->
    <div class="card">
        <div class="card-header header-elements-inline">
            <h5 class="card-title">Cấu hình hệ thống</h5>
        </div>

        <div class="card-body">
            <p class="mb-4">Cấu hình các chế độ làm tươi của từng dashbroad cho phù hợp. khuyễn nghị tối thiểu là 5s</p>

            <form ng-submit="submitForm()">
                <fieldset class="mb-3">
                    <legend class="text-uppercase font-size-sm font-weight-bold">Thời gian làm tươi dashbroad</legend>

                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Danh sách máy chủ kết nối với hệ thống
                        </label>
                        <div class="col-lg-10">
                            <div class="input-group">
                                <input type="number" class="form-control" ng-model="setting.general">
                                <span class="input-group-append">
                                    <span class="input-group-text">giây</span>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Thống kê các task trong hệ thống
                        </label>
                        <div class="col-lg-10">
                            <div class="input-group">
                                <input type="number" class="form-control" ng-model="setting.totalTask">
                                <span class="input-group-append">
                                    <span class="input-group-text">giây</span>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Danh sách nhóm service
                        </label>
                        <div class="col-lg-10">
                            <div class="input-group">
                                <input type="number" class="form-control" ng-model="setting.listGroup">
                                <span class="input-group-append">
                                    <span class="input-group-text">giây</span>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Chi tiết một nhóm service
                        </label>
                        <div class="col-lg-10">
                            <div class="input-group">
                                <input type="number" class="form-control" ng-model="setting.listService">
                                <span class="input-group-append">
                                    <span class="input-group-text">giây</span>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-form-label col-lg-2">Chi tiết một service
                        </label>
                        <div class="col-lg-10">
                            <div class="input-group">
                                <input type="number" class="form-control" ng-model="setting.detailService">
                                <span class="input-group-append">
                                    <span class="input-group-text">giây</span>
                                </span>
                            </div>
                        </div>
                    </div>
                </fieldset>

                <div class="text-right">
                    <button type="submit" class="btn btn-primary">Lưu cấu hình <i class="icon-floppy-disk ml-2"></i></button>
                </div>
            </form>
        </div>
    </div>
    <!-- /form inputs -->
</div>