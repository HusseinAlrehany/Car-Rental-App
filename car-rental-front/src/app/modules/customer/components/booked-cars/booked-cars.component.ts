import { Component, OnInit } from '@angular/core';
import { ZorroImportsModule } from '../../../../Angular.Zorro';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd/message';
import { CustomerService } from '../../service/customer.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-booked-cars',
  standalone: true,
  imports: [ZorroImportsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './booked-cars.component.html',
  styleUrl: './booked-cars.component.scss'
})
export class BookedCarsComponent implements OnInit{

  bookings: any = [];
  pageSize: number = 5;
  pageIndex: number = 1;
  totalCars: number = 0;

  constructor(private message: NzMessageService,
              private customerService: CustomerService
              
  ){}
  ngOnInit(): void {
    this.getBookingHistory(this.pageIndex, this.pageSize);

    
  }

  onPageChange(pageNumber: number): void{
     this.pageIndex = pageNumber;
     this.getBookingHistory(pageNumber, this.pageSize);

  }
  getBookingHistory(page: number, size: number) {
    this.customerService.getPageOfBookingHistory(page, size).subscribe(
      (response)=> {
        
         this.bookings = response.data.content;
         this.totalCars = response.data.totalElements;
         
      },
      (error: HttpErrorResponse)=>{
        if(error.status === 400 && error.error){
         this.message.error(error.error.message);
       }
      }
    )
  }

  getStatusColor(status: string): string {
  switch (status) {
    case 'PENDING':
      return 'orange';
    case 'APPROVED':
      return 'green';
    case 'REJECTED':
      return 'red';
    case 'COMPLETED':
      return 'blue';  
    default:
      return 'default';
  }
}

}
