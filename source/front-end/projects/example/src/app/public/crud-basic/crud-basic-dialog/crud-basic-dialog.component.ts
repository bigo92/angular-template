import { Component, Input, OnInit } from '@angular/core';
import { DialogMode } from 'projects/_base/service/dialog.service';

@Component({
  selector: 'app-crud-basic-dialog',
  templateUrl: './crud-basic-dialog.component.html',
  styleUrls: ['./crud-basic-dialog.component.scss']
})
export class CrudBasicDialogComponent implements OnInit {

  @Input() id: any;
  @Input() mode: string = DialogMode.view;
  constructor() { }

  ngOnInit() {
  }

}
