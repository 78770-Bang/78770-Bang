import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './features/user/components/home/home.component';
import { SignupComponent } from './features/user/components/signup/signup.component';
import { LoginComponent } from './features/user/components/login/login.component';
import { ForgotPasswordComponent } from './features/user/components/forgot-password/forgot-password.component';
import { ProfileComponent } from './features/user/components/profile/profile.component';
import { DashboardComponent } from './customer/pages/dashboard/dashboard.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'user/signup', component: SignupComponent },
  { path: 'user/login', component: LoginComponent },
  { path: 'user/forgot-password', component: ForgotPasswordComponent },
  { path: 'user/profile', component: ProfileComponent },
  {path: 'customer/dashboard', component: DashboardComponent},
  { path: 'customer/dashboard', loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule) },
  { path: '**', redirectTo: '', pathMatch: 'full' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
