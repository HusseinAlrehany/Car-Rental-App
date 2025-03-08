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

    constructor(private adminService: AdminService, 
                private message: NzMessageService
    ){}

  ngOnInit(): void {
    this.getAllCars();
  }
  getAllCars() {
    this.listOfCars = [];
    this.adminService.getAllCars().subscribe(
      (res)=> {
        console.log("cars are " , res);
        res.forEach((element: { processedImg: string; image: string; }) => {
          element.processedImg = 'data:image/jpeg;base64, ' + element.image;
          this.listOfCars.push(element);
        }); 
      }
    )
  }

  deleteCar(carId: number) {
     this.adminService.deleteCar(carId).subscribe(
      (res)=> {
        this.message.success("Car Deleted Successfully!", {nzDuration: 5000});
        this.getAllCars();
      },
      (error: HttpErrorResponse)=> {
        if(error.status === 400 && error.error){
          this.message.error(error.message);
        }
      }
     )

  }


}
