import { BasicDialogComponent } from './basic-dialog/basic-dialog.component';
import { DialogSize, DialogMode } from './../../../_base/servicers/dialog.service';
import { DialogService } from 'src/app/_base/servicers/dialog.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-basic',
  templateUrl: './basic.component.html',
  styleUrls: ['./basic.component.scss']
})
export class BasicComponent implements OnInit {

  checked = false;
  loading = false;
  indeterminate = false;
  listOfData: readonly Data[] = [];
  listOfCurrentPageData: readonly Data[] = [];
  setOfCheckedId = new Set<number>();

  constructor(private dialogService: DialogService){

  }

  updateCheckedSet(id: number, checked: boolean): void {
    if (checked) {
      this.setOfCheckedId.add(id);
    } else {
      this.setOfCheckedId.delete(id);
    }
  }

  onCurrentPageDataChange(listOfCurrentPageData: readonly Data[]): void {
    this.listOfCurrentPageData = listOfCurrentPageData;
    this.refreshCheckedStatus();
  }

  refreshCheckedStatus(): void {
    const listOfEnabledData = this.listOfCurrentPageData.filter(({ disabled }) => !disabled);
    this.checked = listOfEnabledData.every(({ id }) => this.setOfCheckedId.has(id));
    this.indeterminate = listOfEnabledData.some(({ id }) => this.setOfCheckedId.has(id)) && !this.checked;
  }

  onItemChecked(id: number, checked: boolean): void {
    this.updateCheckedSet(id, checked);
    this.refreshCheckedStatus();
  }

  onAllChecked(checked: boolean): void {
    this.listOfCurrentPageData
      .filter(({ disabled }) => !disabled)
      .forEach(({ id }) => this.updateCheckedSet(id, checked));
    this.refreshCheckedStatus();
  }

  sendRequest(): void {
    this.loading = true;
    const requestData = this.listOfData.filter(data => this.setOfCheckedId.has(data.id));
    console.log(requestData);
    setTimeout(() => {
      this.setOfCheckedId.clear();
      this.refreshCheckedStatus();
      this.loading = false;
    }, 1000);
  }

  ngOnInit(): void {
    this.listOfData = new Array(100).fill(0).map((_, index) => ({
      id: index,
      name: `Edward King ${index}`,
      age: 32,
      address: `London, Park Lane no. ${index}`,
      disabled: index % 2 === 0
    }));
  }

  getData(){

  }

  createData(){
    const dialog = this.dialogService.openDialog(option => {
      option.title = 'Thêm mới dữ liệu';
      option.size = DialogSize.xlarge;
      option.component = BasicDialogComponent;
      option.inputs = { //tham số cần truyền vào cho dialog @Input
        id: null, //Thêm ko cần id, dành cho sửa và xem
        mode: DialogMode.add
      }; 
    },(eventNames: string, eventValue: any) => {
      if(eventNames === 'onClose'){ //sự kiện ấn đóng của dialog báo về @Output
        this.dialogService.closeDialogById(dialog.id); //đong dialog
        if (eventValue) {
          this.getData(); // refesh lại table
        }
      }
    });
  }

}

export interface Data {
  id: number;
  name: string;
  age: number;
  address: string;
  disabled: boolean;
}
