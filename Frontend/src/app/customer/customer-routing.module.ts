import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './customer.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { HomeComponent } from '../features/user/components/home/home.component';

const routes: Routes = [
  { path: '', component: CustomerComponent },
  { path: '', component: HomeComponent },

  { path: 'dashboard', component: DashboardComponent }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
