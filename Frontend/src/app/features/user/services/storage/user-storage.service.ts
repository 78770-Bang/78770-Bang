import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth-token';
const USER = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class UserStorageService {

  constructor() { }

  public saveToken(token: string): void {
    window.localStorage.removeItem(TOKEN_KEY);
    window.localStorage.setItem(TOKEN_KEY, token);
  }

  static getToken(): string  {
    return localStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user): void {
    window.localStorage.removeItem(USER);
    window.localStorage.setItem(USER, JSON.stringify(user));
  }

  static getUser(): any {
    return JSON.parse(localStorage.getItem(USER));
  }

  static getUserId(): string {
    const user = this.getUser();
    if (user === null) {return '';}
    return user.userId;
  }
  
  static getUserRole(): string {
    const user = this.getUser();
    if (user === null) {return '';}
    return user.role;
  }
  static isUserLoggedIn(): boolean {
    if(this.getToken() === null){
      return false;
    }
    const role: string =this.getUserRole();
    return role == 'CUSTOMER';
  }
  
  static signOut(): void {
    window.localStorage.removeItem(TOKEN_KEY);
    window.localStorage.removeItem(USER);
  }
}



