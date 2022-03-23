import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputSelectApiComponent } from './input-select-api.component';
import { FormsModule } from '@angular/forms';
import { NzSelectModule} from 'ng-zorro-antd/select';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzSpinModule } from 'ng-zorro-antd/spin';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NzIconModule,
    NzSpinModule,
    NzSelectModule
  ],
  exports: [
    InputSelectApiComponent
  ],
  declarations: [InputSelectApiComponent]
})
export class InputSelectApiModule { }
