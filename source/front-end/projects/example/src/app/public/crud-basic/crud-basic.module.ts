import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CrudBasicComponent } from './crud-basic.component';
import { CrudBasicRoutes } from './crud-basic.routing';
import { FormModule } from 'projects/_base/module/form.module';

@NgModule({
  imports: [
    CommonModule,
    FormModule,
    CrudBasicRoutes
  ],
  declarations: [CrudBasicComponent]
})
export class CrudBasicModule { }
