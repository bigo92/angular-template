import { NzModalRef } from "ng-zorro-antd/modal";

export interface DialogModal<T> {
  id: number;
  dialog: NzModalRef<T>;
}

export interface DialogConfigModal {
  title: string,
  size: string;
  component: any;
  inputs: any;
  escClose: boolean;
  closeOnNavigation: boolean;
}
