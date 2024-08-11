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
  signup(UserDto: any): Observable<any> {
    return this.http.post(this.Basic_URL + "/user/signup", UserDto,{
      headers:new HttpHeaders().set('Content-Type','application/json')
    })  
  }

   // Method to handle user login
   login(username: string, password: string): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post(this.Basic_URL + "/user/login", { username, password }, { headers, observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => {
        console.log(res.body);
        this.userStorageService.saveUser(res.body);
        const tokenLength = res.headers.get(AUTH_HEADER)?.length;
        const bearerToken = res.headers.get(AUTH_HEADER)?.substring(7, tokenLength);
        console.log(bearerToken);
        this.userStorageService.saveToken(bearerToken);
        return res;
      }));
  }

  forgotPassword(forgotPasswordRequest: any): Observable<any> {
    return this.http.post(this.Basic_URL + "/user/forgotpassword", forgotPasswordRequest);
  }
}
