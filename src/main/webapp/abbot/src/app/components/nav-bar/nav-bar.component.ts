import { AuthService } from '../../services/auth-service';
import { UserService } from '../../services/user.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  constructor(private authservice: AuthService)
  {
  }

  isAuthenticated = false;

  ngOnInit() {
  }

  authenticated()
  {
    return this.authservice.isAuthenticated();
  }

  userIsAdmin()
  {
    return true;
  }

  logout()
  {
    this.authservice.logout();
  }
}
