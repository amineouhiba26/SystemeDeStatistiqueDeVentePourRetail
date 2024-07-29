import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserConf } from '../user-conf.model';
import { UserConfService } from '../service/user-conf.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './user-conf-delete-dialog.component.html',
})
export class UserConfDeleteDialogComponent {
  userConf?: IUserConf;

  constructor(protected userConfService: UserConfService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.userConfService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
