import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrainingRegister } from '../training-register.model';
import { TrainingRegisterService } from '../service/training-register.service';
import { TrainingRegisterDeleteDialogComponent } from '../delete/training-register-delete-dialog.component';

@Component({
  selector: 'jhi-training-register',
  templateUrl: './training-register.component.html',
})
export class TrainingRegisterComponent implements OnInit {
  trainingRegisters?: ITrainingRegister[];
  isLoading = false;

  constructor(protected trainingRegisterService: TrainingRegisterService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.trainingRegisterService.query().subscribe(
      (res: HttpResponse<ITrainingRegister[]>) => {
        this.isLoading = false;
        this.trainingRegisters = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITrainingRegister): number {
    return item.id!;
  }

  delete(trainingRegister: ITrainingRegister): void {
    const modalRef = this.modalService.open(TrainingRegisterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.trainingRegister = trainingRegister;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
