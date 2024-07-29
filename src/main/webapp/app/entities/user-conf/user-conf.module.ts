import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserConfComponent } from './list/user-conf.component';
import { UserConfDetailComponent } from './detail/user-conf-detail.component';
import { UserConfUpdateComponent } from './update/user-conf-update.component';
import { UserConfDeleteDialogComponent } from './delete/user-conf-delete-dialog.component';
import { UserConfRoutingModule } from './route/user-conf-routing.module';

@NgModule({
  imports: [SharedModule, UserConfRoutingModule],
  declarations: [UserConfComponent, UserConfDetailComponent, UserConfUpdateComponent, UserConfDeleteDialogComponent],
})
export class UserConfModule {}
