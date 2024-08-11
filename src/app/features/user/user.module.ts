import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { CoreModule } from '../../core/core.module';
import { DemoNgZorroAntdModule } from '../../DemoNgZorroAntdModule';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { HomeComponent } from './components/home/home.component';
import { BookingComponent } from './components/booking/booking.component';



@NgModule({
  declarations: [
    SignupComponent,
    LoginComponent,
    ForgotPasswordComponent,
    HomeComponent,
    BookingComponent
  

  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    FormsModule,
    DemoNgZorroAntdModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    NzInputModule,
    NzFormModule,
    NzSpinModule,
    NzInputModule,
    NzButtonModule,
    NzIconModule,
    CoreModule,
    NzLayoutModule
  ]
})
export class UserModule { }
