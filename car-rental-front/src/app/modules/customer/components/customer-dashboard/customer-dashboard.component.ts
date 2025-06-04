import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../service/customer.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NzMessageService } from 'ng-zorro-antd/message';
import { CommonModule } from '@angular/common';
import { ZorroImportsModule } from '../../../../Angular.Zorro';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [CommonModule, ZorroImportsModule, RouterLink],
  templateUrl: './customer-dashboard.component.html',
  styleUrl: './customer-dashboard.component.scss'
})
export class CustomerDashboardComponent implements OnInit{

  cars: any = [];
  page = 1;
  size = 8;
  totalCars = 0;

  constructor(private customerService: CustomerService,
              private message: NzMessageService
  ){}

  ngOnInit(): void {
    this.getAllCars();
  }
  getAllCars() { 
    this.customerService.getCarsPage(this.page, this.size).subscribe({
      next: (res) => {
        console.log(res);
        this.totalCars = res.data.totalElements;
        this.cars = res.data.content.map((element: { processedImg: string; image: string }) => ({
         ...element,
         processedImg: 'data:image/jpeg;base64,' + element.image
       }));
      },
      error: (error: HttpErrorResponse) => {
        if(error.status === 400  && error.error){
          this.message.error(error.message);
        }
      }
    });
}

onPageChange(newPage: number) {
    this.page = newPage;
    this.getAllCars(); 
  }

}