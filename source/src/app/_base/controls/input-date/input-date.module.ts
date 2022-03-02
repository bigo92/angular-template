import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputDateComponent } from './input-date.component';
import { FormsModule } from '@angular/forms';
import { NzDatePickerModule} from 'ng-zorro-antd/date-picker';
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
  exports:[
    InputDateComponent
  ],
  declarations: [InputDateComponent]
})
export class InputDateModule { }
