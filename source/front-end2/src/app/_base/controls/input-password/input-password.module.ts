import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputPasswordComponent } from './input-password.component';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NzIconModule,
    NzInputModule
  ],
  exports: [
    InputPasswordComponent
  ],
  declarations: [InputPasswordComponent]
})
export class InputPasswordModule { }
