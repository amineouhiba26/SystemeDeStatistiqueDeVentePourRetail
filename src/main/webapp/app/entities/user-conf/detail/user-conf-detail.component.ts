import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserConf } from '../user-conf.model';

@Component({
  selector: 'jhi-user-conf-detail',
  templateUrl: './user-conf-detail.component.html',
})
export class UserConfDetailComponent implements OnInit {
  userConf: IUserConf | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userConf }) => {
      this.userConf = userConf;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
