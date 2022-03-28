import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CrudBasicComponent } from './crud-basic.component';
import { CrudBasicRoutes } from './crud-basic.routing';

@NgModule({
  imports: [
    CommonModule,
    CrudBasicRoutes
  ],
  declarations: [CrudBasicComponent]
})
export class CrudBasicModule { }
