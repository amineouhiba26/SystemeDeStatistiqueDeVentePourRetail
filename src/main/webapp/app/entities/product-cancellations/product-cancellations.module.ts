import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductCancellationsComponent } from './list/product-cancellations.component';
import { ProductCancellationsDetailComponent } from './detail/product-cancellations-detail.component';
import { ProductCancellationsUpdateComponent } from './update/product-cancellations-update.component';
import { ProductCancellationsDeleteDialogComponent } from './delete/product-cancellations-delete-dialog.component';
import { ProductCancellationsRoutingModule } from './route/product-cancellations-routing.module';

@NgModule({
  imports: [SharedModule, ProductCancellationsRoutingModule],
  declarations: [
    ProductCancellationsComponent,
    ProductCancellationsDetailComponent,
    ProductCancellationsUpdateComponent,
    ProductCancellationsDeleteDialogComponent,
  ],
})
export class ProductCancellationsModule {}
