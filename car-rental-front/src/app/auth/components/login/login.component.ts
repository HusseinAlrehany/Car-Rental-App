import { Component } from '@angular/core';
import { ZorroImportsModule } from '../../../Angular.Zorro';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';
import { AuthService } from '../../services/auth/auth.service';
import { StorageService } from '../../services/storage/storage.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ZorroImportsModule, CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  signInForm!: FormGroup;
  showPassword = true;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private nzMessage: NzMessageService,
              private authService: AuthService,
    
  ){

    this.signInForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]]
    })

  }

  onSubmit(){
    this.authService.signIn(this.signInForm.value).subscribe(
      (res)=> {
       // window.location.reload();
        if(res.userId != null){
          const user = {
            id: res.userId,
            role: res.userRole
          }
          StorageService.saveUser(user);
          console.log("the logged in user is ADMIN-> " + StorageService.isAdminLoggedIn());
          console.log("the logged in user is CUSTOMER-> " + StorageService.isCustomerLoggedIn());

          
          if(StorageService.isCustomerLoggedIn()){
              this.router.navigateByUrl("/customer/dashboard");
          }
           else if(StorageService.isAdminLoggedIn()){
              this.router.navigateByUrl("/admin/dashboard");
          }

          this.nzMessage.success("Log in Successfull!", {nzDuration: 5000});
        }
          
      },
      (error)=> {
          const errorMessage = error.error?.message || error.message || 'An unexpected error occured!';
          this.nzMessage.error(errorMessage, {nzDuration: 5000});
      }
    )

  }
  togglePasswordVisibility(){
    this.showPassword = !this.showPassword;
  }

}
