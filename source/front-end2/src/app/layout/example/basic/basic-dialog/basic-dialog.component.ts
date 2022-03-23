import { FormBuilder, FormGroup } from '@angular/forms';
import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { DialogMode } from 'src/app/_base/servicers/dialog.service';

@Component({
  selector: 'app-basic-dialog',
  templateUrl: './basic-dialog.component.html',
  styleUrls: ['./basic-dialog.component.scss']
})
export class BasicDialogComponent implements OnInit {

  @Input() id: number | undefined;
  @Input() mode: string = DialogMode.view;

  @Output() onClose = new EventEmitter<any>();

  isLoading: boolean = true;
  isSubmit: boolean = false;
  myForm!: FormGroup;
  constructor(private fb: FormBuilder) { }

  async ngOnInit() {
    this.isLoading = true;
    this.myForm = this.fb.group({
      colText: [null],
      colNumber: [null],
      colFloat: [null],
      colDateString: [null],
      colSelectSingle: [null],
      colDateTime: [null],
    });

    this.isLoading = false;
  }

  async submitForm(){

  }

  closeDialog(value: any = null) {
    this.onClose.emit(value);
  }
}

