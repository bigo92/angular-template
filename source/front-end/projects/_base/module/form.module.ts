import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { PagingModule } from "./paging/paging.module";
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { NzTableModule } from 'ng-zorro-antd/table';
import { IconsProviderModule } from "./icons-provider.module";
import { RenderErrorsModule } from "./render-errors/render-errors.module";
import { InputTextModule } from "../controls/input-text/input-text.module";
import { InputTextSearchModule } from "../controls/input-text-search/input-text-search.module";
import { InputTextareaModule } from "../controls/input-textarea/input-textarea.module";

@NgModule({
  imports: [
    FormsModule,
    ReactiveFormsModule,
    PagingModule,
    // PipeModule,
    NzFormModule,
    NzTableModule,
    IconsProviderModule,
    NzSpinModule,
    NzButtonModule,
    NzTabsModule,
    RenderErrorsModule,
    InputTextModule,
    InputTextSearchModule,
    InputTextareaModule,
    // InputRadioModule,
    // InputCheckBoxModule,
    // InputDateModule,
    // InputSelectModule,
    // InputSelectApiModule,
    // InputSelectMultipleModule,
    // InputSelectMultipleApiModule,
    // InputNumberModule,
    // InputFileModule,
    // InputDateDynamicModule,
    // NzToolTipModule
  ],
  exports: [
    FormsModule,
    ReactiveFormsModule,
    PagingModule,
    // PipeModule,
    NzFormModule,
    NzTableModule,
    IconsProviderModule,
    NzSpinModule,
    NzButtonModule,
    NzTabsModule,
    RenderErrorsModule,
    InputTextModule,
    InputTextSearchModule,
    InputTextareaModule,
    // InputRadioModule,
    // InputCheckBoxModule,
    // InputDateModule,
    // InputSelectModule,
    // InputSelectApiModule,
    // InputSelectMultipleModule,
    // InputSelectMultipleApiModule,
    // InputNumberModule,
    // InputFileModule,
    // InputDateDynamicModule,
    // NzToolTipModule
  ]
})
export class FormModule {}
