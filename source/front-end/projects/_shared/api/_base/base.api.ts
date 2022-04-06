import { Observable } from 'rxjs';
import { LocationStrategy } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { environment } from 'src/environments/environment';
import { SettingService } from '../../services/setting.service';
import { PagedListModel, PagingModel } from 'projects/_base/model/response-model';

@Injectable({
  providedIn: 'root'
})
export class BaseApi {

  protected _domain: string = '';
  protected _baseUrl: string = '';
  protected http: HttpClient;

  constructor(injector: Injector) {
    this.http = injector.get(HttpClient);
  }

  public get domain() { return this._domain }

  public set domain(value: string) {
    this._domain = value;
  }

  public get baseUrl() { return this._baseUrl }

  public set baseUrl(value: string) {
    this._baseUrl = value;
  }

  protected stringifyParams(params: any): any {
    if (!params) { return undefined; }
    const paramsCopy = JSON.parse(JSON.stringify(params, (key, value) => {
      if (value !== null) { return value; }
      else { return ''; }
    }));
    Object.keys(paramsCopy).forEach(key => {
      if (typeof paramsCopy[key] === 'object') {
        paramsCopy[key] = JSON.stringify(paramsCopy[key], (k, value) => {
          if (value !== null) { return value; }
          else { return ''; }
        });
      }
    });
    return paramsCopy;
  }

  public serializeQueryParam(param: any): string {
    param = this.stringifyParams(param);
    var str = [];
    for (var p in param)
      if (param.hasOwnProperty(p)) {
        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(param[p]));
      }
    return str.join("&");
  }

  public getPaging<T = PagedListModel<any>>(params: any = null, path: string = 'get-paging'): Observable<T> {
    return this.http.get<T>(`${this._baseUrl}/${path}`, { params: this.stringifyParams(params) });
  }

  public getAll<T = any[]>(params: any = null, path: string = 'get-all'): Observable<T> {
    return this.http.get<T>(`${this._baseUrl}/${path}`, { params: this.stringifyParams(params) });
  }

  public getCombobox<T = PagedListModel<any>>(params: any = null, path: string = 'get-combobox'): Observable<T> {
    return this.http.get<T>(`${this._baseUrl}/${path}`, { params: this.stringifyParams(params) });
  }

  public findOne<T = any>(params: any = null, path: string = 'find-one'): Observable<T> {
    return this.http.get<T>(`${this._baseUrl}/${path}`, { params: this.stringifyParams(params) });
  }

  public add<T = any>(body: any = null, path: string = 'add'): Observable<T> {
    return this.http.post<T>(`${this._baseUrl}/${path}`, body);
  }

  public edit<T = any>(body: any = null, path: string = 'edit'): Observable<T> {
    return this.http.post<T>(`${this._baseUrl}/${path}`, body);
  }

  public delete<T = any>(params: any = null, path: string = 'delete'): Observable<T> {
    return this.http.delete<T>(`${this._baseUrl}/${path}`, { params: params })
  }

}
