import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { ZorroImportsModule } from '../../../../Angular.Zorro';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [ZorroImportsModule, CommonModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss'
})
export class AdminDashboardComponent implements OnInit{
    listOfCars: any = [];

    pageIndex = 1;
    pageSize = 5;
    total = 0;
    constructor(private adminService: AdminService, 
                private message: NzMessageService
    ){}

  ngOnInit(): void {
    //this.getAllCars();
    this.loadCarPage(this.pageIndex, this.pageSize);
  }

 onPageChange(pageNumber: number): void {
  this.pageIndex = pageNumber;
  this.loadCarPage(pageNumber, this.pageSize);
}


 loadCarPage(pageIndex: number, pageSize: number){
 
  this.listOfCars = [];
  
  this.adminService.getCarPage(pageIndex, pageSize).subscribe(
    (response)=> {

      //this.listOfCars = response.data.content;
       console.log("cars are " , response);
        this.total = response.data.totalElements;
        this.listOfCars = response.data.content.map((element: { processedImg: string; image: string }) => ({
         ...element,
         processedImg: 'data:image/jpeg;base64,' + element.image
       }));
       
    },
    (error: HttpErrorResponse)=> {
       if(error.status === 400 && error.error){
         this.message.error(error.message);
       }
    }
  )
 }

  deleteCar(carId: number) {
     this.adminService.deleteCar(carId).subscribe(
      (res)=> {
        this.message.success("Car Deleted Successfully!", {nzDuration: 5000});
        this.loadCarPage(this.pageIndex, this.pageSize);
      },
      (error: HttpErrorResponse)=> {
        if(error.status === 400 && error.error){
          this.message.error(error.error.message);
        }
      }
     )

  }


}
