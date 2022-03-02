import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputFloatComponent } from './input-float.component';
import { FormsModule } from '@angular/forms';
import { NzInputNumberModule } from 'ng-zorro-antd/input-number';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';
import { TextMaskModule } from 'angular2-text-mask';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NzInputNumberModule,
    TextMaskModule,
    NzIconModule,
    NzInputModule
  ],
  exports:[
    InputFloatComponent
  ],
  declarations: [InputFloatComponent]
})
export class InputFloatModule { }
