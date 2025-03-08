import { Component, OnInit } from '@angular/core';
import { ZorroImportsModule } from '../../../../Angular.Zorro';
import { NzFormModule } from 'ng-zorro-antd/form';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';
import { AdminService } from '../../service/admin.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-update-car',
  standalone: true,
  imports: [ZorroImportsModule, NzFormModule, ReactiveFormsModule, CommonModule ],
  templateUrl: './update-car.component.html',
  styleUrl: './update-car.component.scss'
})
export class UpdateCarComponent implements OnInit{

carId: number = this.activatedRoute.snapshot.params["carId"];  

updateCarForm!: FormGroup;

existingImage! : string | null;
selectedFile!: File | null;
imagePreview!: string | ArrayBuffer | null;
imgChanged = false;

listOfOptions: Array<{ label: string; value: string }> = [];
listOfBrands = ["BMW", "Audi", "Mercedes", "Ferrari", "AlphaRomio", "Toyota", "Suparo", "Jeep", "Lexus", "RAM", "LandRover", "Jajuar"];
listOfTypes = ["Petrol", "Hyprid", "Electric"];
listOfColor = ["Red", "White", "Blue", "Black", "Silver", "Grey"];
listOfTransmission = ["Manual", "Automatic"];

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private message: NzMessageService,
              private adminService: AdminService,
              private activatedRoute: ActivatedRoute
              
  ){}

  ngOnInit(): void {
     
    this.updateCarForm = this.formBuilder.group({
            brand: [null, [Validators.required]],
            name: [null, [Validators.required]],
            type: [null, [Validators.required]],
            transmission: [null, [Validators.required]],
            color: [null, [Validators.required]],
            modelYear: [null, [Validators.required]],
            price: [null, [Validators.required]],
            description: [null, [Validators.required]]

    })

    this.getCarById();
    
  }

  onFileSelected(event: any){
   const file = event.target.files[0];

   if(file){
    this.selectedFile = file;
    this.previewImage();
    this.imgChanged = true;
    this.existingImage = null;
   } else {
    this.selectedFile = null;
    this.imgChanged = false;
    this.imagePreview = this.existingImage;
   }
  }

  previewImage(){
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    }

    reader.readAsDataURL(this.selectedFile!);
  }
  getCarById() {
     this.adminService.getCarById(this.carId).subscribe(
      (res)=> {
        if(res.id != null){
          this.updateCarForm.patchValue(res);
          this.existingImage = 'data:image/jpeg;base64,' + res.image;
        }
      }
     )
  }

  updateCar(){
    
    if(this.updateCarForm.valid){
       const formData: FormData = new FormData();
       if(this.imgChanged && this.selectedFile){
        formData.append('returnedImage', this.selectedFile);
       }

       Object.keys(this.updateCarForm.controls).forEach(key=> {
         formData.append(key, this.updateCarForm.get(key)?.value);
       })

       this.adminService.updateCar(this.carId, formData).subscribe(
        (res)=> {
          if(res.id != null){
            this.message.success("Car Updated Successfully!", {nzDuration: 3000});
            this.router.navigateByUrl('/admin/dashboard');
          }
        },
        (error: HttpErrorResponse)=> {
          if(error.status === 400 && error.error){
            this.showValidationErrors(error.error);
          }

        }
       )
    }
  }

  showValidationErrors(errors: any){
      Object.values(errors).forEach(
        (message: any) => {
          this.message.error(message);
        }
      )
  }


}
