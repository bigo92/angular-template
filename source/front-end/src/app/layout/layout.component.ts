import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { DialogService } from 'projects/_base/service/dialog.service';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
  encapsulation: ViewEncapsulation.None //Mo khoa scss cho phep scss se anh huong tat ca component
})
export class LayoutComponent implements OnInit {

  isInit = false;

  constructor(private dialogService: DialogService) { }

  ngOnInit(): void {
    this.dialogService.error('a','aaa');

  }

}
