import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PagingComponent } from './paging.component';
import { IconsProviderModule } from '../icons-provider.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IconsProviderModule
  ],
  providers: [PagingComponent],
  exports: [PagingComponent],
  declarations: [PagingComponent]
})
export class PagingModule { }
