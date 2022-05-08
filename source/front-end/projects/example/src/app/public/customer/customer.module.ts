import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerComponent } from './customer.component';
import { CustomerRoutes } from './customer.routing';
import { FormModule } from 'projects/_base/module/form.module';
import { CustomerDataComponent } from './customer-data/customer-data.component';

@NgModule({
  imports: [
    CommonModule,
    FormModule,
    CustomerRoutes
  ],
  declarations: [
    CustomerComponent,
    CustomerDataComponent]
})
export class CustomerModule { }
