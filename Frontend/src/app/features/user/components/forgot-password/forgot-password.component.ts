import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Data } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';


@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  forgotPasswordForm!: FormGroup;
  isSpinning = false;

  constructor(private service: AuthService, private fb: FormBuilder) { }

  ngOnInit() {
    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  submitForgotPassword() {
    if (this.forgotPasswordForm.invalid) {
      return;
    }

    this.isSpinning = true;
    this.service.forgotPassword(this.forgotPasswordForm.value).subscribe({
      next: (Data) => {
        console.log('Password reset link sent', Data);
        this.isSpinning = false;
      },
      error: (error) => {
        console.error('Forgot password error', error);
        this.isSpinning = false;
      }
    });
  };
  
}