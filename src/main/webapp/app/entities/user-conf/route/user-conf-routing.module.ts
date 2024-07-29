import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserConfComponent } from '../list/user-conf.component';
import { UserConfDetailComponent } from '../detail/user-conf-detail.component';
import { UserConfUpdateComponent } from '../update/user-conf-update.component';
import { UserConfRoutingResolveService } from './user-conf-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const userConfRoute: Routes = [
  {
    path: '',
    component: UserConfComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserConfDetailComponent,
    resolve: {
      userConf: UserConfRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserConfUpdateComponent,
    resolve: {
      userConf: UserConfRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserConfUpdateComponent,
    resolve: {
      userConf: UserConfRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userConfRoute)],
  exports: [RouterModule],
})
export class UserConfRoutingModule {}
