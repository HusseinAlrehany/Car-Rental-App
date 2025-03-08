import { Component, OnInit } from '@angular/core';
import { ZorroImportsModule } from '../../../../Angular.Zorro';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NzFormModule } from 'ng-zorro-antd/form';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';
import { AdminService } from '../../service/admin.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-post-car',
  standalone: true,
  imports: [ZorroImportsModule, CommonModule, ReactiveFormsModule,NzFormModule],
  templateUrl: './post-car.component.html',
  styleUrl: './post-car.component.scss'
})
export class PostCarComponent implements OnInit{
listOfOptions: Array<{ label: string; value: string }> = [];
listOfBrands = ["BMW", "Audi", "Mercedes", "Ferrari", "AlphaRomio", "Toyota", "Suparo", "Jeep", "Lexus", "RAM", "LandRover", "Jajuar"];
listOfTypes = ["Petrol", "Hyprid", "Electric"];
listOfColor = ["Red", "White", "Blue", "Black", "Silver", "Grey"];
listOfTransmission = ["Manual", "Automatic"];

carForm!: FormGroup;
imgPreview!: string | ArrayBuffer | null;
selectedFile!: File | null;

constructor(private formBuilder: FormBuilder,
            private router: Router,
            private message: NzMessageService,
            private adminService: AdminService
){}

onFileSelected(event: any){
  this.selectedFile = event.target.files[0];
  this.previewImage();
}

previewImage(){
  const reader = new FileReader();
  reader.onload= ()=> {
    //update the imgPreview
    this.imgPreview = reader.result;
  }

  reader.readAsDataURL(this.selectedFile!);
}



  ngOnInit(): void {
    this.carForm = this.formBuilder.group({
      brand: [null, [Validators.required]],
      name: [null, [Validators.required]],
      type: [null, [Validators.required]],
      transmission: [null, [Validators.required]],
      color: [null, [Validators.required]],
      modelYear: [null, [Validators.required]],
      price: [null, [Validators.required]],
      description: [null, [Validators.required]]
    })

  }
  addCar(){
    if(this.carForm.valid){
      const formData: FormData = new FormData();
      formData.append('returnedImage', this.selectedFile!);

      Object.keys(this.carForm.controls).forEach(key => {
        formData.append(key, this.carForm.get(key)?.value);
      } );
    
      console.log("Car is => " , this.carForm.value);

      this.adminService.addCar(formData).subscribe(
        (res)=> {
          if(res.id != null){
            this.message.success("Car Added Successfully!", {nzDuration: 5000});
            this.router.navigateByUrl('/admin/dashboard');
          }

        },
        (error: HttpErrorResponse)=> {
         //to show error messages from the backend 
         if(error.status === 400 && error.error){
          this.showValidationErrors(error.error);
         }
        }
      )
    }

  }
  showValidationErrors(errors: any) {
    Object.values(errors).forEach(
      (message: any) => {
        this.message.error(message);
      }
    )
  }

}
