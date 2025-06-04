import { Component, OnInit } from '@angular/core';
import { ZorroImportsModule } from '../../../../Angular.Zorro';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { AdminService } from '../../service/admin.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-booked-rides',
  standalone: true,
  imports: [ZorroImportsModule, CommonModule, ReactiveFormsModule, MatButtonModule,
    MatMenuModule],
  templateUrl: './booked-rides.component.html',
  styleUrl: './booked-rides.component.scss'
})
export class BookedRidesComponent implements OnInit{

 pageIndex = 1;
 pageSize = 5;
 total =0;

  bookingDetails : any = [];


  constructor(private adminService: AdminService,
              private message: NzMessageService
              
  ){

  }
  ngOnInit(): void {
   
    this.loadBookedCarsPage(this.pageIndex, this.pageSize);
  }
 

  onPageChange(page: number): void{

    this.pageIndex = page;
    this.loadBookedCarsPage(page, this.pageSize);
  }

  loadBookedCarsPage(page: number, pageSize: number) {
    
     this.adminService.getPagedBookedRides(page, pageSize).subscribe(
      (response)=> {
        this.bookingDetails = response.data.content;
        this.total = response.data.totalElements;

      },
      (error: HttpErrorResponse)=> {
        if(error.status === 400 && error.error){
         this.message.error(error.error.message);
       }

      }
     )

  }


  changeOrderStatus(id: number, status: string){

    this.adminService.changeBookingStatus(id, status).subscribe(
      (response)=> {
        this.message.success(response.message, {nzDuration: 5000});
        this.loadBookedCarsPage(this.pageIndex, this.pageSize);
        
      },
      (error: HttpErrorResponse)=> {
        if(error.status === 400 && error.error){
         this.message.error(error.message);
       }
      }
    )
  }

}
