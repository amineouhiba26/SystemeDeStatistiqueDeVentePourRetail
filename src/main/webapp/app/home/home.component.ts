import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  account: Account | null = null;

  constructor(private accountService: AccountService, private loginService: LoginService, private router: Router) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
        if (this.accountService.hasAnyAuthority('ROLE_ADMIN')) {
          console.log(this.account.authorities);
          this.router.navigate(['/user-conf']); // Navigate to user-conf route
        } else {
          console.log(this.account.authorities);
          this.router.navigate(['/product']);
        }
      } else {
        this.login();
      }
    });
  }

  login(): void {
    this.loginService.login();
  }
}
