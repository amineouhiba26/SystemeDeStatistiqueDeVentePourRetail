import { Route, Routes } from '@angular/router';

import { HomeComponent } from './home.component';

const routes: Routes = [];
export const HOME_ROUTE: Route = {
  path: '',
  component: HomeComponent,
  data: {
    pageTitle: 'home.title',
  },
};
