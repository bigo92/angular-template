import { NgModule } from '@angular/core';
import { DateFormatPipe } from './date-format.pipe';
import { DatetimeFormatPipe } from './datetime-format.pipe';
import { StatusPipe } from './status.pipe';
import { TextMorePipe } from './text-more.pipe';
import { FirstTxtPipe } from './first-txt.pipe';

@NgModule({
  declarations: [
    DateFormatPipe,
    DatetimeFormatPipe,
    StatusPipe,
    TextMorePipe,
    FirstTxtPipe
   ],
  exports: [
    DateFormatPipe,
    DatetimeFormatPipe,
    StatusPipe,
    TextMorePipe,
    FirstTxtPipe
  ]
})
export class PipeModule { }
