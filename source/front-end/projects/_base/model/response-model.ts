
export class ResponseModel<T = PagedListModel<any> | any> {
  result!: T;
  error?: ErrorsModel;

  public get success(): boolean {
    return !this.error;
  }

}

export interface PagingModel {
  page?: number;
  size?: number;
  count?: number;
  order?: any;
  loadMore?: boolean;
}

export interface PagedListModel<T> {
  data: T[] | [];
  paging: PagingModel;
}

export interface ErrorModel {
  key: string;
  value: any;
}

export interface ErrorsModel {
  [key: string]: string[];
}
