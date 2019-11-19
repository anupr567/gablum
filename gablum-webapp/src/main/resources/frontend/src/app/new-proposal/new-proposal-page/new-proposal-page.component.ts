import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
@Component({
  selector: 'app-new-proposal-page',
  templateUrl: './new-proposal-page.component.html',
  styleUrls: ['./new-proposal-page.component.css']
})
export class NewProposalPageComponent implements OnInit {

  productSpecsForm = new FormGroup({
    subDomain: new FormControl(''),
    productName: new FormControl(''),
    quantity: new FormControl('')
  });
  constructor() { }

  ngOnInit() {
  }

}
