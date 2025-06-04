import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../service/customer.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { NzMessageService } from 'ng-zorro-antd/message';
import { ZorroImportsModule } from '../../../../Angular.Zorro';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-car-details',
  standalone: true,
  imports: [ZorroImportsModule, CommonModule, RouterLink],
  templateUrl: './car-details.component.html',
  styleUrl: './car-details.component.scss'
})
export class CarDetailsComponent implements OnInit{

  carId: number = this.activatedRoute.snapshot.params['id'];
  car: any;
  processedImg: any;

  constructor(private customerService: CustomerService,
              private activatedRoute: ActivatedRoute,
              private message: NzMessageService,
              private router: Router
  ){}

  ngOnInit(): void {
    this.getCarDetails();
  }
  getCarDetails() {
    this.customerService.getCarById(this.carId).subscribe(
      (res)=> {
         console.log(res);
         this.car = res.data;
         this.processedImg = 'data:image/jpeg;base64,' + res.data.image;
      },
    (error: HttpErrorResponse)=> {
       if(error.status === 400 && error.error){
         this.message.error(error.error.message);
       }
     }
    )
  }

  onCancel(){
    this.router.navigateByUrl('/customer/dashboard');
  }




}
