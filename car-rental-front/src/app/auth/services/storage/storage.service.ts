import { Injectable } from '@angular/core';

const USER = 'user';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

 static isSessionStorageAvailable(): boolean {
     return (
       typeof window !== 'undefined' &&
       typeof window.sessionStorage !== 'undefined'
     );
  }

 static saveUser(user: any) : void {
    if(this.isSessionStorageAvailable()){
      sessionStorage.removeItem(USER);
      sessionStorage.setItem(USER, JSON.stringify(user));
    }
  }

  static getUser(): any {
    if(this.isSessionStorageAvailable()){
      return JSON.parse(sessionStorage.getItem(USER)!);
    }
    return null;
            
  }

  static getUserRole(): string {
    const user = this.getUser();
    return user?.role || '';
  }

  static getUserId(): string | null{
    const user = this.getUser();
    return user? user.id : null;
  }

  static isAdminLoggedIn(): boolean {
    if(!this.isSessionStorageAvailable()){
      return false;
    }
    const role: string = this.getUserRole();
    return role === "ADMIN";
  }

  static isCustomerLoggedIn(): boolean {
    if(!this.isSessionStorageAvailable()){
      return false;
    }
    const role: string = this.getUserRole();
    return role === "CUSTOMER";
  }

  static clearUserData(): void {
     if(this.isSessionStorageAvailable()){
      sessionStorage.removeItem(USER);
     }
  }


}
