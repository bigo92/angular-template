import { LocationStrategy } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { SettingService } from 'projects/_shared/services/setting.service';
import { BaseApi } from '../_base/base.api';

@Injectable({
  providedIn: 'root'
})
export class CrudApi extends BaseApi {

  constructor(
    injector: Injector,
    setting: SettingService
  ) {
    super(injector);

    this.domain = setting.data.api.example;
    this.baseUrl = '/public/category/bloodgroup';
  }
}
