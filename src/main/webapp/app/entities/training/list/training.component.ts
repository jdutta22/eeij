import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITraining } from '../training.model';
import { TrainingService } from '../service/training.service';
import { TrainingDeleteDialogComponent } from '../delete/training-delete-dialog.component';

@Component({
  selector: 'jhi-training',
  templateUrl: './training.component.html',
})
export class TrainingComponent implements OnInit {
  trainings?: ITraining[];
  isLoading = false;

  constructor(protected trainingService: TrainingService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.trainingService.query().subscribe(
      (res: HttpResponse<ITraining[]>) => {
        this.isLoading = false;
        this.trainings = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITraining): number {
    return item.id!;
  }

  delete(training: ITraining): void {
    const modalRef = this.modalService.open(TrainingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.training = training;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
