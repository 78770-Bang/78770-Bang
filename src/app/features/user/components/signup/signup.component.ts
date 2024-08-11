import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  isSpinning = false;
  signupForm!: FormGroup;
  passwordVisible = true;
  confirmPasswordVisible = true;

  constructor(private fb: FormBuilder,
              private authservice: AuthService,
              private router: Router,
              private notification: NzNotificationService
            ) { }

  ngOnInit() {
    this.signupForm = this.fb.group({
      name: ['', [Validators.required]],
      contactNumber: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]], // assuming a 10 digit contact number
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required, this.confirmValidator]]
    });
  }

  confirmValidator = (control: FormGroup): { [key: string]: boolean } | null => {
    const password = this.signupForm?.get('password')?.value;
    const confirmPassword = control.value;
    if (password && confirmPassword && password !== confirmPassword) {
      return { mismatch: true };
    }
    return null;
  }

  submitForm() {
    if (this.signupForm.valid) {
      this.isSpinning = true;
      this.authservice.signup(this.signupForm.value).subscribe({
        next: (res) => {
          console.log(res);
          this.notification.success('Success', 'Signup successful', { nzDuration: 2000 });
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          console.error('Full Error Response:', error);

          // Handle specific error messages based on the backend response
          if (error.status === 409) {
            this.notification.error('Error', 'User already exists', { nzDuration: 5000 });
          } else if (error.error && error.error.message) {
            this.notification.error('Error', error.error.message, { nzDuration: 5000 });
          } else {
            this.notification.error('Error', 'Signup failed', { nzDuration: 5000 });
          }
          
          this.isSpinning = false;
        },
        complete: () => {
          this.isSpinning = false;
        }
      });
    } else {
      this.notification.error('Error', 'Please fill in all required fields correctly', { nzDuration: 5000 });
      this.signupForm.markAllAsTouched();  // Trigger validation messages
    }
  }
}
