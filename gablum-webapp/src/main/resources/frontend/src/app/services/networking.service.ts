import { Injectable } from '@angular/core';
import { CommunicatorService } from './communicator.service';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { pipe, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NetworkingService {

  constructor(private comms: CommunicatorService, private http: HttpClient) { }

  getData<T>(url: string, dest: string, key = 'inventory') {
    this.http.get<T>(url)
      .pipe(
        retry(3),
        catchError((err) => {
          return throwError(err);
        })
      )
      .subscribe(res => {
        this.comms.postMessage(this, dest, {[key]: res});
      });
  }

  postData<T>(url: string, dest: string, data, key = 'inventory') {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })};
    console.log( ' datatatatat :::', data);
    this.http.post<T>(url, data, httpOptions)
      .pipe(
        retry(3),
        catchError(err => {
          return throwError(err);
        })
      )
      .subscribe(res => {
        this.comms.postMessage(this, dest, {[key]: res});
      });
  }
}
