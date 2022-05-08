import { Component, Input, OnInit } from '@angular/core';
import { DialogMode } from 'projects/_base/service/dialog.service';

@Component({
  selector: 'app-customer-data',
  templateUrl: './customer-data.component.html',
  styleUrls: ['./customer-data.component.scss']
})
export class CustomerDataComponent implements OnInit {

  @Input() id: any;
  @Input() mode: string = DialogMode.view;
  constructor() { }

  ngOnInit() {
  }

}
