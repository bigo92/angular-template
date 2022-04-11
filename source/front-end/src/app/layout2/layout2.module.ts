import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Layout2Component } from './layout2.component';
import { Layout2Routes } from './layout2.routing';

@NgModule({
  imports: [
    CommonModule,
    Layout2Routes
  ],
  declarations: [Layout2Component]
})
export class Layout2Module { }
