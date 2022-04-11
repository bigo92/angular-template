import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SettingService {

  public data!: SettingModel;
  constructor(private http: HttpClient) {
  }

  public init(): Promise<void> {
    return new Promise<void>(async (resolve, reject) => {
      if (this.data) {
        resolve();
      } else {
        this.http.get(environment.fileSetting).subscribe(res => {
          this.data = Object.setPrototypeOf(res, SettingModel.prototype)
          resolve();
        });
      }
    });
  }
}

export class SettingModel {
  api!: SettingApiModel;
  path!: string;
}

export class SettingApiModel {
  example!: string;
  sso!: string;
  qtud!: string;
  dashboard!: string;
}
