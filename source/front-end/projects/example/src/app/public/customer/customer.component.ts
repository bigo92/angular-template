import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PagingModel } from 'projects/_base/model/response-model';
import { TableConfigModel } from 'projects/_base/model/table-config-model';
import { DialogMode, DialogService, DialogSize } from 'projects/_base/service/dialog.service';
import { MessageService } from 'projects/_base/service/message.service';
import { CrudApi } from 'projects/_shared/api/example/crud.api';
import { CrudBasicDialogComponent } from '../crud-basic/crud-basic-dialog/crud-basic-dialog.component';
import { CustomerDataComponent } from './customer-data/customer-data.component';

declare let $: any;

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {

  public formSearch: FormGroup | undefined;

  public listOfData: any[] = [];
  public isLoading = false;
  public paging: PagingModel | undefined;

  public kv: number = 1;

  public tableConfig: TableConfigModel = new TableConfigModel({
    keyId: 'id',
    isAllChecked: false,
    indeterminate: false,
    itemSelected: new Set<any>()
  });

  public loading = false;

  public lstStatus: any[] = [
    { value: 1, text: 'Đang sử dụng' },
    { value: 2, text: 'Đã xóa' },
  ];

  public lisData: any[] = [
    { id: 1, name: 'avbas', edit: false },
    { id: 2, name: 'sss', edit: false },
    { id: 3, name: 'fff', edit: false }
  ]

  constructor(
    private dialogService: DialogService,
    private fb: FormBuilder,
    // private messageService: MessageService,
    // private crudApi: CrudApi
  ) { }

  async ngOnInit(): Promise<void> {
    this.formSearch = this.fb.group({
      name: ['']
    });
    await this.getData();
  }

  async getData(paging: PagingModel = { order: { id: false } }): Promise<void> {
    const formData = this.formSearch!.getRawValue();

    const where = { and: [] };
    let params: any = {
      ...paging
    };

    if (formData.name) {
      params.name = formData.name;
    }

    // loading animation
    this.isLoading = true;
    let a = 'ass'.toDateYYYYMMDD();
    // const rs = await this.crudApi.getPaging(params).toApiPromise();
    // if (rs.success) {
    //   this.tableConfig.reset();
    //   this.listOfData = rs.result.data;
    //   this.paging = rs.result.paging;
    // } else {
    //   // this.messageService.notiMessageError(rs.error);
    // }

    this.isLoading = false;
  }

  async addDataDialog(): Promise<void> {
    const dialog = this.dialogService.openDialog(option => {
      option.title = 'Thêm mới';
      option.size = DialogSize.full;
      option.component = CustomerDataComponent;
      option.inputs = {
        id: null,
        mode: DialogMode.add
      };
    }, (eventName, eventValue) => {
      if (eventName === 'onClose') {
        this.dialogService.closeDialogById(dialog.id);
        if (eventValue) {
          this.getData({ ...this.paging, page: 1 });
        }
      }
    });
  }

  async showDataDialog(id: number): Promise<void> {
    const dialog = this.dialogService.openDialog(option => {
      option.title = 'Xem thông tin dữ liệu';
      option.size = DialogSize.large;
      option.component = CrudBasicDialogComponent;
      option.inputs = {
        id,
        mode: DialogMode.view
      };
    }, (eventName) => {
      if (eventName === 'onClose') {
        this.dialogService.closeDialogById(dialog.id);
      }
    });
  }

  async editDataDialog(id: number): Promise<void> {
    const dialog = this.dialogService.openDialog(option => {
      option.title = 'Sửa dữ liệu';
      option.size = DialogSize.large;
      option.component = CrudBasicDialogComponent;
      option.inputs = {
        id,
        mode: DialogMode.edit
      };
    }, (eventName) => {
      if (eventName === 'onClose') {
        this.dialogService.closeDialogById(dialog.id);
        this.getData(this.paging);
      }
    });
  }

  async deleteData(listId: number[]): Promise<void> {
    //   const result = await this.dialogService.confirm('Bạn có muốn xóa những dữ liệu này không?', ' ');
    //   if (!result) { return; }

    //   let params: any = {
    //     id: listId
    //   };
    //   const rs = await this.crudApi.delete(params).toApiPromise();
    //   if (rs.success) {
    //     this.messageService.notiMessageSuccess('Xóa dữ liệu thành công');
    //     this.getData(this.paging);
    //   } else {
    //     this.messageService.notiMessageError(rs.error);
    //   }
  }

  them1Dong() {
    this.lisData.push({ id: 4, name: 'ggggg' });
  }

  showData() {
    console.log(this.lisData);
  }
}
