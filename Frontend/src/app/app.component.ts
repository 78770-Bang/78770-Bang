import { Component, OnInit } from '@angular/core';
import { UserStorageService } from './features/user/services/storage/user-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'Dine_Direct';

  isUserLoggedIn: boolean = false
  isDropdownVisible = false;
  
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

  toggleDropdown() {
    this.isDropdownVisible = !this.isDropdownVisible;
  }
 
}
