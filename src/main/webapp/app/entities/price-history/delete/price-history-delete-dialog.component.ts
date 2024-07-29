import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPriceHistory } from '../price-history.model';
import { PriceHistoryService } from '../service/price-history.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './price-history-delete-dialog.component.html',
})
export class PriceHistoryDeleteDialogComponent {
  priceHistory?: IPriceHistory;

  constructor(protected priceHistoryService: PriceHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.priceHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
