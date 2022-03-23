import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { SettingService } from './setting.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    // check quyền
    return true;

    // if (!this.settingService.data.login || this.userService.isAuthenticated()) {
    //   let linkCurrent: string = `${location.origin}${location.pathname}`;
    //   let domain = this.settingService.data.urlApi;
    //   linkCurrent = linkCurrent.replace(domain,'');
    //   linkCurrent = linkCurrent.replace('//','/');
    //   // check quyền menu

    //   if (linkCurrent === '/' || linkCurrent.indexOf('/public/') !== -1) {
    //     return true;
    //   }
    //   if (this.userService.userInfo.linkMenu?.some((x:any) => x.link && linkCurrent.startsWith(x.link))) {
    //     return true;
    //   }
    //   this.router.navigate(['/public/account/logout'], { queryParams: { redirect: state.url }, replaceUrl: true });
    //   return false;
    // }

    // this.router.navigate(['/public/account/login'], { queryParams: { redirect: state.url }, replaceUrl: true });
    // return false;
  }

}
