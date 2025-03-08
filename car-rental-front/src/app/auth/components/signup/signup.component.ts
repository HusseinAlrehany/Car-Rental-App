import { Component } from '@angular/core';
import { ZorroImportsModule } from '../../../Angular.Zorro';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ZorroImportsModule, CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {

  signUpForm!: FormGroup;

  showPassword = true;
  
  constructor(private formBuilder: FormBuilder, 
              private authService: AuthService,
              private nzMessage: NzMessageService,
              private router: Router
  ){

  
  }

  ngOnInit(){
    this.signUpForm = this.formBuilder.group({
      name: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required, Validators.minLength(6)]],
      confirmPassword: [null, [Validators.required, this.passwordMatchValidator]]
    })
  }

  //pasword and confirm password must match validation
  passwordMatchValidator = (control: FormControl): { [s: string]: boolean } => {
    if(!control.value){
        return {required: true};
    } else if (control.value !== this.signUpForm.controls['password'].value) {
      return { mustMatch: true, error: true};
    }
    return {};
  }; 

  togglePasswordVisibility(): void{
     this.showPassword = !this.showPassword;
  }
  onSubmit(){
    console.log("Sign up -> " , this.signUpForm.value);
    this.authService.signUp(this.signUpForm.value).subscribe(
      (res)=> {
        if(res.id != null){
          this.nzMessage.success("sign up successfull!", {nzDuration: 3000, nzPauseOnHover: true});
          this.router.navigateByUrl('/login')
        }
      },
      (error)=> {
         const errorMessage = error.error?.message ||
                              error.message || 
                              'An un expected error occured!';
         this.nzMessage.error(errorMessage, {nzDuration: 5000, nzPauseOnHover: true, nzAnimate: true});                     
      }
    )

  }

}
