import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductCancellations } from '../product-cancellations.model';
import { ProductCancellationsService } from '../service/product-cancellations.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './product-cancellations-delete-dialog.component.html',
})
export class ProductCancellationsDeleteDialogComponent {
  productCancellations?: IProductCancellations;

  constructor(protected productCancellationsService: ProductCancellationsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.productCancellationsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
