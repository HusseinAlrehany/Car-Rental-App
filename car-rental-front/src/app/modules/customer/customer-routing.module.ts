import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerDashboardComponent } from './components/customer-dashboard/customer-dashboard.component';
import { CarDetailsComponent } from './components/car-details/car-details.component';
import { BookingCarComponent } from './components/booking-car/booking-car.component';
import { BookedCarsComponent } from './components/booked-cars/booked-cars.component';

const routes: Routes = [
  {path: 'dashboard', component: CustomerDashboardComponent},
  {path: 'car-details/:id', component: CarDetailsComponent},
  {path: 'book/:id', component: BookingCarComponent},
  {path: 'booked-cars', component: BookedCarsComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
