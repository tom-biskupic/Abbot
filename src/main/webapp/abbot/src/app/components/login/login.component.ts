import { AuthService } from '../../services/auth-service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  credentials = {username: '', password: ''};

  error = false;

  constructor(private authservice: AuthService, private router: Router ) { }

  ngOnInit() {
  }

  login()
  {
    this.authservice.login(
      this.credentials.username, this.credentials.password).subscribe(
        principal => { this.router.navigate(['']); },
        error => { this.error = true; }
      );
  }

  getError(): boolean
  {
    return this.error;
  }
}
