import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { DialogService, DialogSize } from 'projects/_base/service/dialog.service';
import { MessageService } from 'projects/_base/service/message.service';
import { UserService } from 'projects/_shared/services/user.service';
import { Observable } from 'rxjs';

declare let $: any;
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  encapsulation: ViewEncapsulation.None //Mo khoa scss cho phep scss se anh huong tat ca component
})
export class NavbarComponent implements OnInit {

  public userName: Observable<string> | undefined;
  public listOffice: any[] = [];
  public curentOffice: any;
  public dropdownOpen: boolean = false;
  listOfNotification = [];
  hasNewNotification = true;
  whereParamPos = {};

  constructor(
    private dialogService: DialogService,
    private fb: FormBuilder,
    private router: Router,
    private messageService: MessageService,
    public userService: UserService,
  ) { }

  async ngOnInit() {

  }

  resizeMenu() {
    if ($('body').hasClass('sidebar-xs')) {
      $('body').removeClass('sidebar-xs');
    } else {
      $('body').addClass('sidebar-xs');
    }
  }

  showMenuMobile() {
    if ($('body').hasClass('sidebar-mobile-main')) {
      $('body').removeClass('sidebar-mobile-main');
    } else {
      $('body').addClass('sidebar-mobile-main');
    }
  }

}
