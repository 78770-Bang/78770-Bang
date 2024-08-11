import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { UserStorageService } from '../../services/storage/user-storage.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  isSpinning = false;
  

  constructor(private authService: AuthService , 
            private fb: FormBuilder,
            private notification: NzNotificationService,
            private router: Router) { }

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }


  submitForm() { 
    if (this.loginForm.invalid) {
      this.notification.error('Error', 'Please fill in the form correctly');
      return;
    }
    
    this.isSpinning = true;
    this.authService.login(this.loginForm.get('email')!.value, 
    this.loginForm.get('password')!.value)
      .subscribe({
        next: (res) => {
          console.log('Login response:', res);
          this.isSpinning = false;
          if (UserStorageService.isUserLoggedIn()) {
            this.router.navigate(['/user/dashboard']);
          } else {
            this.notification.error('Error', 'Login failed. Please try again.', { nzDuration: 5000 });
          }
        },
        error: (error) => {
          this.isSpinning = false;
          console.error('Login error:', error);
          this.notification.error('Error', 'Invalid credentials', { nzDuration: 5000 });
        }
      });
  }
}
  


