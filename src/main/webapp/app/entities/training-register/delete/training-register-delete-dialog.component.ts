import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrainingRegister } from '../training-register.model';
import { TrainingRegisterService } from '../service/training-register.service';

@Component({
  templateUrl: './training-register-delete-dialog.component.html',
})
export class TrainingRegisterDeleteDialogComponent {
  trainingRegister?: ITrainingRegister;

  constructor(protected trainingRegisterService: TrainingRegisterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trainingRegisterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
