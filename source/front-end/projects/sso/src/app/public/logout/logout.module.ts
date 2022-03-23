import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LogoutComponent } from './logout.component';
import { LogoutRoutes } from './logout.routing';

@NgModule({
  imports: [
    CommonModule,
    LogoutRoutes
  ],
  declarations: [LogoutComponent]
})
export class LogoutModule { }
