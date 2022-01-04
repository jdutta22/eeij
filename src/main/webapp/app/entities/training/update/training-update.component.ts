import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITraining, Training } from '../training.model';
import { TrainingService } from '../service/training.service';

@Component({
  selector: 'jhi-training-update',
  templateUrl: './training-update.component.html',
})
export class TrainingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    trainingName: [],
    trainingMode: [],
    trainingType: [],
    trainingStartDate: [],
    traingEndDate: [],
    trainingYear: [],
    trainingRegistration: [],
  });

  constructor(protected trainingService: TrainingService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ training }) => {
      this.updateForm(training);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const training = this.createFromForm();
    if (training.id !== undefined) {
      this.subscribeToSaveResponse(this.trainingService.update(training));
    } else {
      this.subscribeToSaveResponse(this.trainingService.create(training));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITraining>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(training: ITraining): void {
    this.editForm.patchValue({
      id: training.id,
      trainingName: training.trainingName,
      trainingMode: training.trainingMode,
      trainingType: training.trainingType,
      trainingStartDate: training.trainingStartDate,
      traingEndDate: training.traingEndDate,
      trainingYear: training.trainingYear,
      trainingRegistration: training.trainingRegistration,
    });
  }

  protected createFromForm(): ITraining {
    return {
      ...new Training(),
      id: this.editForm.get(['id'])!.value,
      trainingName: this.editForm.get(['trainingName'])!.value,
      trainingMode: this.editForm.get(['trainingMode'])!.value,
      trainingType: this.editForm.get(['trainingType'])!.value,
      trainingStartDate: this.editForm.get(['trainingStartDate'])!.value,
      traingEndDate: this.editForm.get(['traingEndDate'])!.value,
      trainingYear: this.editForm.get(['trainingYear'])!.value,
      trainingRegistration: this.editForm.get(['trainingRegistration'])!.value,
    };
  }
}
