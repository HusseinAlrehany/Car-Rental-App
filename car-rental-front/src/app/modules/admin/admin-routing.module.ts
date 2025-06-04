import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { PostCarComponent } from './components/post-car/post-car.component';
import { UpdateCarComponent } from './components/update-car/update-car.component';
import { BookedRidesComponent } from './components/booked-rides/booked-rides.component';

const routes: Routes = [
  {path: 'dashboard', component: AdminDashboardComponent},
  {path: 'car', component: PostCarComponent},
  {path: 'update-car/:carId', component: UpdateCarComponent},
  {path: 'booked-cars', component: BookedRidesComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
