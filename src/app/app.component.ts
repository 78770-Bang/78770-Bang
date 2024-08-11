import { Component } from '@angular/core';
import { UserStorageService } from './features/user/services/storage/user-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Dine_Direct';
  
  
  isUserLoggedIn: boolean = UserStorageService.isUserLoggedIn();
  
  constructor(private router: Router) {}
  
  
  ngOnInit() {
    this.router.events.subscribe(event => {
      this.isUserLoggedIn = UserStorageService.isUserLoggedIn();
    })
  }
  
  logout() {
    UserStorageService.signOut();
    this.router.navigate(['login'])
  }
 
}
