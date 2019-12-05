import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NetworkingService } from '../services/networking.service';
import { LoginToken } from '../interfaces/login-token';
import { LoginDataService } from '../services/login-data.service';
import { CommunicatorService } from '../services/communicator.service';
import { AuthenticationService } from '../services/authentication.service';
import { LoggerService } from '../services/logger.service';
import { ProfileDataService } from '../services/profile-data.service';
import { checkServerIdentity } from 'tls';
// import { MatError } from '@angular/material';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  get password() {
    return this.loginForm.get('password');
  }
  get userName() {
    return this.loginForm.get('username');
  }
  constructor(
    private router: Router,
    private loginService: LoginDataService,
    private comms: CommunicatorService,
    private logger: LoggerService,
    private auth: AuthenticationService,
    private profile: ProfileDataService) {
      this.comms.getMessages().subscribe(message => {
        if (message.dest === '@all' || message.dest === LoginComponent.messageKey) {
          const data = message.data;
          if ('loginResult' in data) {
            const loginToken: LoginToken = data.loginResult.accessToken;
            if (loginToken === undefined || loginToken === null) {

            } else {
              auth.setAuthenticated(true);
              this.profile.getUserProfileByEmail('@all', 'profile');
              this.router.navigate(['/dashboard']);
            }
          }
        }
      });
  }
  public static messageKey = 'login-component';

  check = true;

  loginForm = new FormGroup({
    username : new FormControl('', Validators.compose([Validators.required,
      Validators.minLength(3)])),
    password : new FormControl('', Validators.compose([Validators.required, Validators.pattern('^[&@$_.#!a-zA-Z0-9]{0,20}$'),
    Validators.minLength(3)]))
  });

  ngOnInit() {
  }

  getErrorMessage1() {
    return this.userName.hasError('required') ? '*You must enter a Username' :
        // this.userName.hasError('pattern') ? '*Not a valid Username' :
        this.userName.hasError('minlength') ? '*Minimum 3 characters' :
            '';
  }

  getErrorMessage2() {
    return this.password.hasError('required') ? '*You must enter a Password' :
        this.password.hasError('pattern') ? '*Not a valid Password' :
        this.password.hasError('minlength') ? '*Minimum 3 characters' :
            '';
  }

  onSubmit() {
    this.loginService.login(this.loginForm.value);
  }
  OnSignUp() {
    this.router.navigate(['/register']);
  }

}
