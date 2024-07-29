import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product-cancellations',
        data: { pageTitle: 'ssvrApp.productCancellations.home.title' },
        loadChildren: () => import('./product-cancellations/product-cancellations.module').then(m => m.ProductCancellationsModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'ssvrApp.payment.home.title' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'ssvrApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'price-history',
        data: { pageTitle: 'ssvrApp.priceHistory.home.title' },
        loadChildren: () => import('./price-history/price-history.module').then(m => m.PriceHistoryModule),
      },
      {
        path: 'order-item',
        data: { pageTitle: 'ssvrApp.orderItem.home.title' },
        loadChildren: () => import('./order-item/order-item.module').then(m => m.OrderItemModule),
      },
      {
        path: 'order',
        data: { pageTitle: 'ssvrApp.order.home.title' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      {
        path: 'permission',
        data: { pageTitle: 'ssvrApp.permission.home.title' },
        loadChildren: () => import('./permission/permission.module').then(m => m.PermissionModule),
      },
      {
        path: 'role',
        data: { pageTitle: 'ssvrApp.role.home.title' },
        loadChildren: () => import('./role/role.module').then(m => m.RoleModule),
      },
      {
        path: 'user-conf',
        data: { pageTitle: 'ssvrApp.userConf.home.title' },
        loadChildren: () => import('./user-conf/user-conf.module').then(m => m.UserConfModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
