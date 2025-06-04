import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from '../../../auth/services/storage/storage.service';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

   BaseUrl = "http://localhost:9000/api/customer";

  constructor(private httpClient: HttpClient) { }

  getAllCars(): Observable<any> {
    return this.httpClient.get(this.BaseUrl + `/cars`, {
      withCredentials: true
    })
  }

  getCarsPage(page: number, size: number): Observable<any> {
      return this.httpClient.get(this.BaseUrl + `/cars-page?page=${page-1}&size=${size}`, {
        withCredentials: true
      })
  }

  getCarById(carId: number): Observable<any> {
    return this.httpClient.get(this.BaseUrl + `/car/${carId}`, {
      withCredentials: true,
    })
  }

  bookCar(bookCarDTO: any): Observable<any> {
    return this.httpClient.post(this.BaseUrl + `/book-car`, bookCarDTO, {
      withCredentials: true,
    })
  }

  getPageOfBookingHistory(page: number, size: number): Observable<any> {
    const userId = StorageService.getUserId();
    return this.httpClient.get(this.BaseUrl + `/booking-history-page/${userId}?page=${page-1}&size=${size}`, {
      withCredentials: true
    })
  }
}
