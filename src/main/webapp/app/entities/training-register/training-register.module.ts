import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrainingRegisterComponent } from './list/training-register.component';
import { TrainingRegisterDetailComponent } from './detail/training-register-detail.component';
import { TrainingRegisterUpdateComponent } from './update/training-register-update.component';
import { TrainingRegisterDeleteDialogComponent } from './delete/training-register-delete-dialog.component';
import { TrainingRegisterRoutingModule } from './route/training-register-routing.module';

@NgModule({
  imports: [SharedModule, TrainingRegisterRoutingModule],
  declarations: [
    TrainingRegisterComponent,
    TrainingRegisterDetailComponent,
    TrainingRegisterUpdateComponent,
    TrainingRegisterDeleteDialogComponent,
  ],
  entryComponents: [TrainingRegisterDeleteDialogComponent],
})
export class TrainingRegisterModule {}
