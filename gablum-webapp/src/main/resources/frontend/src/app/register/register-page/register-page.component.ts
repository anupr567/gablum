import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';


@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterPageComponent implements OnInit {

  registrationForm = new FormGroup({
    name : new FormControl(''),
    email : new FormControl(''),
    address : new FormControl(''),
    phone : new FormControl(''),
    companyName : new FormControl(''),
    userName : new FormControl(''),
    businessLicense : new FormControl(''),
    BCryptPassword : new FormControl(''),
    role : new FormControl(''),
    domainName : new FormControl(''),
    subDomain : new FormControl('')
  });
  constructor() { }

  ngOnInit() {
  }

}
