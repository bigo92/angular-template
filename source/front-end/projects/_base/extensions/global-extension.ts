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

// prototype
String.prototype.convertToISOTime = function (this: string) {
  return (new Date(parseInt(this.substring(0, 4)), parseInt(this.substring(4, 6)) - 1, parseInt(this.substring(6, 8)))).toISOString();
};

String.prototype.convertToDate = function (this: string) {
  return new Date(parseInt(this.substring(0, 4)), parseInt(this.substring(4, 6)) - 1, parseInt(this.substring(6, 8)));
};

String.prototype.convertYYYYMMDDToDate = function (this: string) {

  return new Date(parseInt(this.substring(0, 4)), parseInt(this.substring(4, 6)) - 1, parseInt(this.substring(6, 8)));
};

String.prototype.toDateYYYYMMDD = function (this: string): string {
  const dateTime = new Date(this);
  let monthValue = (dateTime.getMonth() + 1).toString();
  if (monthValue.length == 1) {
    monthValue = `0${monthValue}`;
  }
  let dateValue = dateTime.getDate().toString();
  if (dateValue.length == 1) {
    dateValue = `0${dateValue}`;
  }
  return `${dateTime.getFullYear()}${monthValue}${dateValue}`;
};

String.prototype.stringDateVItoStringYYYYMMDD = function (this: string): string {
  let arr = this.split('/');
  if (arr[0].length < 2) arr[0] = '0' + arr[0];
  if (arr[1].length < 2) arr[1] = '0' + arr[1];
  return arr[2] + arr[1] + arr[0];
};

String.prototype.toJsonArray = function (this: string, defaultValue: any = []): any {
  if (this !== null && this !== '') {
    return JSON.parse(this);
  }
  return [];
};

String.prototype.toUnSign = function (this: string, toUper: boolean = true): string {
  let str = this;
  const AccentsMap = [
    "aàảãáạăằẳẵắặâầẩẫấậ",
    "AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬ",
    "dđ", "DĐ",
    "eèẻẽéẹêềểễếệ",
    "EÈẺẼÉẸÊỀỂỄẾỆ",
    "iìỉĩíị",
    "IÌỈĨÍỊ",
    "oòỏõóọôồổỗốộơờởỡớợ",
    "OÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢ",
    "uùủũúụưừửữứự",
    "UÙỦŨÚỤƯỪỬỮỨỰ",
    "yỳỷỹýỵ",
    "YỲỶỸÝỴ"
  ];
  for (var i = 0; i < AccentsMap.length; i++) {
    var re = new RegExp('[' + AccentsMap[i].substr(1) + ']', 'g');
    var char = AccentsMap[i][0];
    str = str.replace(re, char);
  }
  if (toUper) {
    str = str.toUpperCase();
  }
  return str;
}

String.prototype.isNotEmpty = function (this: string): boolean {
  return this !== '' && this !== null;
}

Date.prototype.toDateYYYYMMDD = function (this: Date): string {
  let monthValue = (this.getMonth() + 1).toString();
  if (monthValue.length == 1) {
    monthValue = `0${monthValue}`;
  }
  let dateValue = this.getDate().toString();
  if (dateValue.length == 1) {
    dateValue = `0${dateValue}`;
  }
  return `${this.getFullYear()}${monthValue}${dateValue}`;
};

Date.prototype.toStringShortDate = function (this: Date): string {
  let monthValue = (this.getMonth() + 1).toString();
  if (monthValue.length == 1) {
    monthValue = `0${monthValue}`;
  }
  let dateValue = this.getDate().toString();
  if (dateValue.length == 1) {
    dateValue = `0${dateValue}`;
  }
  return `${dateValue}/${monthValue}/${this.getFullYear()}`;
};

Array.prototype.getMapingCombobox =
  // tslint:disable-next-line: only-arrow-functions
  async function <T>(this: Array<T>, keys: string, keyMap: string, apiService: any, apiParams?: any, apiActionName?: string): Promise<Array<T>> {
    return new Promise(async (resove) => {

      if (!this || this.length === 0) {
        return resove(this);
      }

      const getApiCombobox = (param: any): Promise<any> => {
        if (!apiActionName) {
          apiActionName = 'getCombobox';
        }
        return apiService[apiActionName](param);
      };

      const getValueFromObjectByKeyMultipleLevel = (obj: any, multipleKey: string): any => {
        const arr = multipleKey.split('.');
        // clone object để không dính preference
        let temp = {
          ...obj
        };
        // lấy dữ liệu từ các cấp key
        try {
          arr.map((key) => {
            temp = temp[key];
          });
        } catch (e) { // nếu không get được value thì return null
          return null;
        }

        return temp;
      };

      let isArray = false;
      let valueSearchParam: any[] = [];
      valueSearchParam = this.filter(x => getValueFromObjectByKeyMultipleLevel(x, keys)).map(x => {
        const valueByKey = getValueFromObjectByKeyMultipleLevel(x, keys);
        if (valueByKey instanceof Array) {
          isArray = true;
        }
        return valueByKey;
      });

      valueSearchParam = [...new Set(valueSearchParam)]

      if (isArray) {
        const valueSearchParamNew = [];
        for (const item of valueSearchParam) {
          for (const v of item) {
            valueSearchParamNew.push(v);
          }
        }
        valueSearchParam = valueSearchParamNew;
      }

      if (valueSearchParam.length === 0) {
        return resove(this);
      }

      let params = { page: 1, size: valueSearchParam.length, valueSearch: valueSearchParam.join(',') };
      if (apiParams) {
        params = { ...params, ...apiParams };
      }
      const rs = await getApiCombobox(params);
      if (rs.success) {
        for (let item of this) {
          const objectItem = getValueFromObjectByKeyMultipleLevel(item, keys);
          if (objectItem) {
            if (isArray) {
              // const listValueMap = [];
              // for (const objectValue of objectItem) {
              //   const mapData = rs.result.data.find(x => JSON.stringify(x.value) === JSON.stringify(objectValue));
              //   if (mapData) {
              //     listValueMap.push(mapData.text);
              //   }
              // }
              // item[keyMap] = listValueMap;

            } else {
              if (Object.prototype.hasOwnProperty.call(rs.result, objectItem)) {
                let objRef: any = item;
                objRef[keyMap] = rs.result[objectItem];
              }
            }
          }
        }
      }

      return resove(this);
    });
  };


Array.prototype.getMapingComboboxFromArrayNotObject =
  // tslint:disable-next-line: only-arrow-functions
  async function <T>(this: Array<T>, apiService: any, apiParams?: any, apiActionName?: string): Promise<Array<T>> {
    let dataConvert: any[] = this.map(x => { return { id: x, name: '' } });
    dataConvert = await dataConvert.getMapingCombobox('id', 'name', apiService, apiParams);
    return dataConvert.map(x => x.name);
  };

Array.prototype.getMapingComboboxJson =
  // tslint:disable-next-line: only-arrow-functions
  async function <T>(this: Array<T>, path: string, keys: string, keyMap: string, apiService: any, apiParams?: any, apiActionName?: string): Promise<Array<T>> {
    return new Promise(async (resove) => {

      if (!this || this.length === 0) {
        return resove(this);
      }

      const getApiCombobox = (param: any): Promise<any> => {
        if (!apiActionName) {
          apiActionName = 'getCombobox';
        }
        return apiService[apiActionName](param);
      };

      const getValueFromObjectByKeyMultipleLevel = (obj: any, multipleKey: string): any => {
        const arr = multipleKey.split('.');
        // clone object để không dính preference
        let temp = {
          ...obj
        };
        // lấy dữ liệu từ các cấp key
        try {
          arr.map((key) => {
            temp = temp[key];
          });
        } catch (e) { // nếu không get được value thì return null
          return null;
        }

        return temp;
      };

      let isArray = false;
      let valueSearchParam: any[] = [];
      valueSearchParam = this.filter((x: any) => getValueFromObjectByKeyMultipleLevel(JSON.parse(x[path]), keys)).map((x: any) => {
        const valueByKey = getValueFromObjectByKeyMultipleLevel(JSON.parse(x[path]), keys);
        if (valueByKey instanceof Array) {
          isArray = true;
        }
        return valueByKey;
      });

      valueSearchParam = [...new Set(valueSearchParam)]

      if (isArray) {
        const valueSearchParamNew = [];
        for (const item of valueSearchParam) {
          for (const v of item) {
            valueSearchParamNew.push(v);
          }
        }
        valueSearchParam = valueSearchParamNew;
      }

      let params = { page: 1, size: valueSearchParam.length, valueSearch: valueSearchParam.join(',') };
      if (apiParams) {
        params = { ...params, ...apiParams };
      }
      const rs = await getApiCombobox(params);
      if (rs.success) {
        for (const item of this) {
          let itemRef: any = item;
          const objectItem = getValueFromObjectByKeyMultipleLevel(JSON.parse(itemRef[path]), keys);
          if (objectItem) {
            if (isArray) {
              // const listValueMap = [];
              // for (const objectValue of objectItem) {
              //   const mapData = rs.result.data.find(x => JSON.stringify(x.value) === JSON.stringify(objectValue));
              //   if (mapData) {
              //     listValueMap.push(mapData.text);
              //   }
              // }
              // item[keyMap] = listValueMap;

            } else {
              if (Object.prototype.hasOwnProperty.call(rs.result, objectItem)) {
                itemRef[keyMap] = rs.result[objectItem];
              }
            }
          }
        }
      }

      return resove(this);
    });
  };

Array.prototype.clone = function <T>(this: Array<T>): Array<T> {
  return JSON.parse(JSON.stringify(this));
}

Array.prototype.trim = function <T>(this: Array<string>): Array<string> {
  return this.map(x => {
    return x.trim();
  });
};

AbstractControl.prototype.markAllAsDirty = function (this: AbstractControl) {
  // tslint:disable-next-line: forin
  if (this instanceof FormGroup) {
    const formGroupValue = (this as FormGroup);
    for (const item in formGroupValue.controls) {
      formGroupValue.get(item)!.markAllAsDirty();
    }
    this.updateValueAndValidity();
  } else if (this instanceof FormArray) {
    const formArrayValue = (this as FormArray);
    for (let i = 0; i < formArrayValue.length; i++) {
      const formGroupValue = formArrayValue.at(i);
      (formGroupValue as AbstractControl).markAllAsDirty();
    }
  } else if (this instanceof FormControl) {
    this.markAsDirty();
    this.updateValueAndValidity();
  }
};

FormGroup.prototype.bindError = function (this: FormGroup, errors: ErrorsModel): string | null {
  const getKeyName = function (keyName: string, form: FormGroup) {
    for (const control in form.controls) {
      if (keyName.toLocaleLowerCase() === control.toLocaleLowerCase()) {
        return control;
      }
    }
    return null;
  };
  const lstMessageAlert: string[] = [];
  for (const key in errors) {
    let controlName = getKeyName(key, this);
    if (controlName != null) {
      if (errors[key].length > 0) {
        const errorValue = { error: errors[key][0] };
        this.get(controlName)!.setErrors(errorValue);
      }
    } else {
      // tslint:disable-next-line: prefer-for-of
      for (let index = 0; index < errors[key].length; index++) {
        const value = errors[key][index];
        lstMessageAlert.push(value);
      }
    }
  }
  return lstMessageAlert.length > 0 ? lstMessageAlert.join('/n') : null;
};

FormGroup.prototype.textTrim = function (this: FormGroup) {
  for (const i in this.controls) {
    if (typeof this.controls[i].value === 'string') {
      this.controls[i].setValue(this.controls[i].value.trim());
    }
  }
};

// tslint:disable-next-line: typedef
FormGroup.prototype.resetMulti = function (this: FormGroup, listControl: string[]) {
  for (const key of listControl) {
    this.get(key)!.reset();
  }
};

// tslint:disable-next-line: typedef
FormGroup.prototype.disableMulti = function (this: FormGroup, listControl: string[]) {
  for (const key of listControl) {
    this.get(key)!.disable();
  }
};

// tslint:disable-next-line: typedef
FormGroup.prototype.enableMulti = function (this: FormGroup, listControl: string[]) {
  for (const key of listControl) {
    this.get(key)!.enable();
  }
};

Observable.prototype.toApiPromise = function <T>(this: Observable<T>): Promise<ResponseModel<T>> {
  return new Promise(async (resove) => {
    this.subscribe(result => {
      if (result) {
        let result = new ResponseModel<T>();
        result.result = result?.result;
        resove(result);
      } else {
        resove(new ResponseModel<T>());
      }
    }, error => {
      let result = new ResponseModel<T>();
      result.error = error.response ? JSON.parse(error.response) : null;
      resove(result);
    });
  });
};

export { };

