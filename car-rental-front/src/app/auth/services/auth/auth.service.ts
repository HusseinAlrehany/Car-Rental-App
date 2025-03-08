import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseURL = "http://localhost:9000/";
  constructor(private httpClient: HttpClient) { }

  signIn(signinRequest: any): Observable<any> {
     return this.httpClient.post(this.baseURL + `signin`, signinRequest, {
      withCredentials : true
     })
  }

  signUp(signupRequest: any): Observable<any> {
    return this.httpClient.post(this.baseURL + `signup`, signupRequest);
  }

  logOut(): Observable<any> {
    return this.httpClient.post(this.baseURL + `logout`, null, {withCredentials: true})
    
    
  }

 /* private clearJwtCookie(): void {
    document.cookie = 'jwt=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 UTC;';
  }*/

}
