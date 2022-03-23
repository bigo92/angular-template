import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { InputSelectMultipleApiComponent } from './input-select-multiple-api.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NzIconModule,
    NzSpinModule,
    NzSelectModule
  ],
  exports: [
    InputSelectMultipleApiComponent
  ],
  declarations: [InputSelectMultipleApiComponent]
})
export class InputSelectMultipleApiModule { }
