import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductCancellationsComponent } from '../list/product-cancellations.component';
import { ProductCancellationsDetailComponent } from '../detail/product-cancellations-detail.component';
import { ProductCancellationsUpdateComponent } from '../update/product-cancellations-update.component';
import { ProductCancellationsRoutingResolveService } from './product-cancellations-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productCancellationsRoute: Routes = [
  {
    path: '',
    component: ProductCancellationsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductCancellationsDetailComponent,
    resolve: {
      productCancellations: ProductCancellationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductCancellationsUpdateComponent,
    resolve: {
      productCancellations: ProductCancellationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductCancellationsUpdateComponent,
    resolve: {
      productCancellations: ProductCancellationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productCancellationsRoute)],
  exports: [RouterModule],
})
export class ProductCancellationsRoutingModule {}
