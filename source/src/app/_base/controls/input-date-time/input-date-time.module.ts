import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { InputDateTimeComponent } from './input-date-time.component';
import { FormsModule } from '@angular/forms';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NzDatePickerModule,
    NzIconModule,
    NzInputModule,
  ],
  providers: [
    DatePipe
  ],
  exports:[
    InputDateTimeComponent
  ],
  declarations: [InputDateTimeComponent]
})
export class InputDateTimeModule { }
