import { BasicDialogComponent } from './basic-dialog/basic-dialog.component';
import { FormModule } from './../../../_base/controls/form/form.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicComponent } from './basic.component';
import { BasicRoutes } from './basic.routing';

@NgModule({
  imports: [
    CommonModule,
    FormModule, // thư viện chung
    BasicRoutes // thêm route vào module,
  ],
  declarations: [
    BasicComponent,
    BasicDialogComponent // thêm dialog cần sử dụng vào đây
  ]
})
export class BasicModule { }
