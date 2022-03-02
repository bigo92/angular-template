import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzAutocompleteModule } from 'ng-zorro-antd/auto-complete'
import { InputAutocomplateApiComponent } from './input-autocomplate-api.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NzIconModule,
    NzInputModule,
    NzAutocompleteModule
  ],
  exports: [
    InputAutocomplateApiComponent
  ],
  declarations: [InputAutocomplateApiComponent]
})
export class InputAutocomplateApiModule { }
