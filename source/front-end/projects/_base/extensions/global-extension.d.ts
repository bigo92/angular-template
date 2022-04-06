import { AbstractControl, FormArray, FormControl, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { ErrorsModel, ResponseModel } from '../model/response-model';

// dealare

declare global {
  interface String {
    convertToISOTime(this: string): string;
    convertToDate(this: string): Date;
    convertYYYYMMDDToDate(this: string): Date;
    toDateYYYYMMDD(this: string): string;
    toUnSign(this: string, toUper: boolean): string;
    isNotEmpty(this: string): boolean;
    stringDateVItoStringYYYYMMDD(this: string): string;
    toJsonArray(this: string, defaultValue: any): any;
  }


  interface Date {
    toDateYYYYMMDD(this: Date): string;
    toStringShortDate(this: Date): string;
  }

  interface Array<T> {
    getMapingCombobox(this: Array<T>, keys: string, keyMap: string, apiService: any, apiParams?: any, apiActionName?: string): Promise<Array<T>>
    getMapingComboboxFromArrayNotObject(this: Array<T>, apiService: any, apiParams?: any, apiActionName?: string): Promise<Array<T>>
    getMapingComboboxJson(this: Array<T>, path: string, keys: string, keyMap: string, apiService: any, apiParams?: any, apiActionName?: string): Promise<Array<T>>
    clone(this: Array<T>): Array<T>
    trim(this: Array<string>): Array<string>
  }
}

declare module '@angular/forms' {
  interface FormGroup {
    bindError(this: FormGroup, errors: ErrorsModel): string | null;
    textTrim(this: FormGroup): void;
    resetMulti(this: FormGroup, listControl: string[]): void;
    disableMulti(this: FormGroup, listControl: string[]): void;
    enableMulti(this: FormGroup, listControl: string[]): void;
  }
  interface AbstractControl {
    markAllAsDirty(this: AbstractControl): void;
  }
}

declare module 'rxjs' {
  interface Observable<T> {
    toApiPromise(this: Observable<T>): Promise<ResponseModel<T>>;
  }
}

export { };

