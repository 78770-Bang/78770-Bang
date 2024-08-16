import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { UserStorageService } from '../storage/user-storage.service';
import { environment } from '../../../../../environments/environment';

const AUTH_HEADER = 'Authorization'; 

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  Basic_URL = environment.apiUrl;


  constructor(private http: HttpClient,
    private userStorageService: UserStorageService
  ) { }
  
  // Method to handle user signup
  signupCustomer(signupRequestDto: any): Observable<any> {
    return this.http.post(this.Basic_URL + "/user/signup", signupRequestDto,{
      headers:new HttpHeaders().set('Content-Type','application/json')
    })  
  }

   // Method to handle user login
   login(username: string, password: string): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post<any>(`${this.Basic_URL}/authenticate`, { username, password }, { headers, observe: 'response' })
        .pipe(map((res: HttpResponse<any>) => {
            const token = res.headers.get('Authorization')?.substring(7); // Extract token from header
            const user = res.body; // Assuming user details are returned in the body

            if (token && user) {
                this.userStorageService.saveToken(token);
                this.userStorageService.saveUser(user);
                return res;
            } else {
                throw new Error('Login failed');
            }
        }));
}

  forgotPassword(forgotPasswordRequest: any): Observable<any> {
    return this.http.post(this.Basic_URL + "/customer/forgotpassword", forgotPasswordRequest);
  }
}
