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

getCarPage(page: number, size: number): Observable<any> {

  return this.httpClient.get(this.BaseUrl + `/car-page?page=${page-1}&size=${size}`, {
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

 getAllBookedRides(): Observable<any> {
  //get has 2 parts(URL, options)
  return this.httpClient.get(this.BaseUrl + `/booked-cars`, {
    withCredentials: true,
  })
 }

 getPagedBookedRides(page: number, size: number): Observable<any>{

  return this.httpClient.get(this.BaseUrl + `/paged-booked-cars?page=${page-1}&size=${size}`, {
    withCredentials: true,
  })

 }

 changeBookingStatus(id: number, status: string): Observable<any> {

  //put and post has 3 parts must provided (URL, body(can be null or empty), options(contain withCredentials))
  return this.httpClient.put(this.BaseUrl + `/booked-cars/${id}/${status}`,null, {
    withCredentials: true,
  })
 }


}
