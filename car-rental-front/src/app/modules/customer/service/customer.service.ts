import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

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
      return this.httpClient.get(this.BaseUrl + `/cars-page?page=${page}&size=${size}`, {
        withCredentials: true
      })
  }

  getCarById(carId: number): Observable<any> {
    return this.httpClient.get(this.BaseUrl + `/car/${carId}`, {
      withCredentials: true,
    })
  }
}
