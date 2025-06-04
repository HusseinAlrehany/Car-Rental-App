import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../service/customer.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { NzMessageService } from 'ng-zorro-antd/message';
import { CommonModule } from '@angular/common';
import { ZorroImportsModule } from '../../../../Angular.Zorro';
import { StorageService } from '../../../../auth/services/storage/storage.service';

@Component({
  selector: 'app-booking-car',
  standalone: true,
  imports: [ZorroImportsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './booking-car.component.html',
  styleUrl: './booking-car.component.scss'
})
export class BookingCarComponent implements OnInit{
car: any;
carId: number = this.activatedRoute.snapshot.params['id'];
bookingForm!:FormGroup;
processedImg: any;
dateFormat!: "MM-DD-YYYY";

constructor(private activatedRoute: ActivatedRoute,
            private customerService: CustomerService,
            private router: Router,
            private formBuilder: FormBuilder,
            private message: NzMessageService             
            
){

  this.bookingForm = this.formBuilder.group({
    fromDate: [null, [Validators.required]],
    toDate: [null, [Validators.required]]
  })
  

}
  ngOnInit(): void {
   
    this.getCarById();

  }
  getCarById() {
    this.customerService.getCarById(this.carId).subscribe(
      (response)=> {
        this.processedImg = 'data:image/jpeg;base64,' + response.data.image;
        this.car = response.data;
      },
      (error: HttpErrorResponse)=> {
         if(error.status === 400 && error.error){
         this.message.error(error.message);
       }
      }
    )
  }

  bookCar(data: any){

   let bookCarDTO = {
      fromDate: data.fromDate,
      toDate: data.toDate,
      userId: StorageService.getUserId(),
      carId: this.carId
    }

    this.customerService.bookCar(bookCarDTO).subscribe(
      (response)=> {
        console.log(response);
        this.message.success(response.message, {nzDuration: 5000});
        this.router.navigateByUrl('/customer/booked-cars');
      },
      (error: HttpErrorResponse)=> {
        if(error.status === 404 && error.error){
         this.message.error(error.error.message);
       }
      }
    )

     }

  }




