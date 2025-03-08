import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { error } from 'node:console';
import { catchError, Observable, throwError } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AdminService {

  BaseUrl = "http://localhost:9000/api/admin"


  constructor(private httpClient: HttpClient, private message: NzMessageService) { }

  addCar(carDTO: any) : Observable<any> {
    return this.httpClient.post(this.BaseUrl + `/add-car`, carDTO, {
      withCredentials: true
    })
  }

  getAllCars(): Observable<any> {
    return this.httpClient.get(this.BaseUrl + `/cars`, {
      withCredentials: true
    })
  }
 deleteCar(carId: number): Observable<any> {
  return this.httpClient.delete(this.BaseUrl + `/car/${carId}`,{
    withCredentials: true

  })
 }

 getCarById(carId: number): Observable<any> {
   return this.httpClient.get(this.BaseUrl + `/car/${carId}`, {
    withCredentials: true
   })
 }
 updateCar(carId: number,carDTO: any): Observable<any> {
  return this.httpClient.put(this.BaseUrl + `/car/${carId}`, carDTO, {
    withCredentials: true
  })
 }
}
