import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'textMore'
})
export class TextMorePipe implements PipeTransform {
  // cắt text
  transform(value: any, len?: number): any {
    const lenStr = len || 20;
    if ((value + '').length <= lenStr) {
      return value;
    } else {
      return (value + '').substring(0, lenStr) + '...';
    }
  }
}
