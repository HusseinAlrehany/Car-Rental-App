import { Component, OnInit } from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterLinkActive, RouterModule, RouterOutlet } from '@angular/router';
import { ZorroImportsModule } from './Angular.Zorro';
import { CommonModule } from '@angular/common';
import { StorageService } from './auth/services/storage/storage.service';
import { AuthService } from './auth/services/auth/auth.service';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, ZorroImportsModule, CommonModule, RouterLink, RouterLinkActive], 
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit{
  title = 'car-rental-system';
  isHomePage = true;
  isAdminLoggedIn: boolean = StorageService.isAdminLoggedIn();
  isCustomerLoggedIn: boolean = StorageService.isCustomerLoggedIn();

  constructor(private router: Router, 
              private authService: AuthService,
              private message: NzMessageService){

     this.router.events.subscribe(event => {
      if(event instanceof NavigationEnd){
        //only show the hero section when on home page
        this.isHomePage = event.url === '/';
      }
     });
  }
  ngOnInit(): void {
    this.router.events.subscribe(event=> {
      this.isAdminLoggedIn = StorageService.isAdminLoggedIn();
      this.isCustomerLoggedIn = StorageService.isCustomerLoggedIn();
    })
    
  }

  logout(){
      this.authService.logOut().subscribe(
        (res)=> {
          console.log("Log out Success", res);
          this.message.success("Log out Success!", {nzDuration: 5000});
          StorageService.clearUserData();
          this.router.navigateByUrl('/login');
        },
        (error)=> {
        
          const errorMessage = error.error?.message || error.message || 'An unexpcted error occured';
          this.message.error(errorMessage, {nzDuration: 5000});
          StorageService.clearUserData();
          this.router.navigateByUrl('/login');
        }
      )
     
      
  }
            //res.clearJwtCookie('jwt');

}
