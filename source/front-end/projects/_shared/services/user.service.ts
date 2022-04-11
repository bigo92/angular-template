import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { SettingService } from './setting.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  public userInfo: any = { userName: 'Admin', fullName: 'Quản trị viên' };

  // Observable navItem source
  private _authNavStatusSource = new BehaviorSubject<boolean>(false);
  // Observable navItem stream
  authNavStatus$ = this._authNavStatusSource.asObservable();
  private user: any | null;

  constructor(
    private settingService: SettingService) { }

  public init(): Promise<void> {
    return new Promise<void>(async (resolve, reject) => {
      this.user = { userName: 'admin', fullName: 'Admin' };
      this._authNavStatusSource.next(this.isAuthenticated());
      await this.initUserInfo();
      resolve();
    });
  }

  private async initUserInfo(): Promise<void> {
    this.userInfo = this.user;
    // if (this.settingService.data.login) {
    //   // check request ignor
    //   if (this.isAuthenticated()) {
    //     const rs = await this.userApi.getUserInfo().toPromise();
    //     if (rs.success) {
    //       this.userInfo = rs.result;
    //     }
    //   }
    // } else {
    //   const rs = await this.userApi.getUserInfo().toPromise();
    //   if (rs.success) {
    //     this.userInfo = rs.result;
    //   }
    // }
  }

  private getCookie(name: string) {
    const value = `; ${document.cookie}`;
    const parts: any = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
  }

  isAuthenticated(): boolean {
    return this.user != null && !this.user.expired;
  }

  login() {
    //return this.manager.signinRedirect();
  }

  async logout() {
    // await this.manager.signoutRedirect();
  }

  async completeAuthentication() {

    // this.user = await this.manager.signinRedirectCallback();
    // this._authNavStatusSource.next(this.isAuthenticated());
  }

  get authorizationHeaderValue(): string {
    return `${this.user.token_type} ${this.user.access_token}`;
  }

}
