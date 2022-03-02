import { InputFilePreviewComponent } from './input-file-preview/input-file-preview.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputFileComponent } from './input-file.component';
import { NzUploadModule } from 'ng-zorro-antd/upload';
import { NzIconModule } from 'ng-zorro-antd/icon';

@NgModule({
  imports: [
    CommonModule,
    NzUploadModule ,
    NzIconModule
  ],
  exports:[
    InputFileComponent
  ],
  declarations: [InputFileComponent, InputFilePreviewComponent]
})
export class InputFileModule { }
