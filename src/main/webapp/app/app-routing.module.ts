import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
  {
    path: 'admin',
    loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
  },
  {
    path: 'dashboard',
    component: DashboardComponent, // Use the DashboardComponent directly
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity-routing.module`).then(m => m.EntityRoutingModule),
  },
  navbarRoute,
  ...errorRoute,
  { path: '**', redirectTo: 'error' }, // Handle 404 and other unspecified routes
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: DEBUG_INFO_ENABLED })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
