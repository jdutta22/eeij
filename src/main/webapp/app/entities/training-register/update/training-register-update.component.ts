import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITrainingRegister, TrainingRegister } from '../training-register.model';
import { TrainingRegisterService } from '../service/training-register.service';
import { ITraining } from 'app/entities/training/training.model';
import { TrainingService } from 'app/entities/training/service/training.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-training-register-update',
  templateUrl: './training-register-update.component.html',
})
export class TrainingRegisterUpdateComponent implements OnInit {
  isSaving = false;

  trainingsSharedCollection: ITraining[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    userAttendance: [],
    cerificate: [],
    training: [],
    user: [],
  });

  constructor(
    protected trainingRegisterService: TrainingRegisterService,
    protected trainingService: TrainingService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trainingRegister }) => {
      this.updateForm(trainingRegister);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trainingRegister = this.createFromForm();
    if (trainingRegister.id !== undefined) {
      this.subscribeToSaveResponse(this.trainingRegisterService.update(trainingRegister));
    } else {
      this.subscribeToSaveResponse(this.trainingRegisterService.create(trainingRegister));
    }
  }

  trackTrainingById(index: number, item: ITraining): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrainingRegister>>): void {
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

  protected updateForm(trainingRegister: ITrainingRegister): void {
    this.editForm.patchValue({
      id: trainingRegister.id,
      userAttendance: trainingRegister.userAttendance,
      cerificate: trainingRegister.cerificate,
      training: trainingRegister.training,
      user: trainingRegister.user,
    });

    this.trainingsSharedCollection = this.trainingService.addTrainingToCollectionIfMissing(
      this.trainingsSharedCollection,
      trainingRegister.training
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, trainingRegister.user);
  }

  protected loadRelationshipsOptions(): void {
    this.trainingService
      .query()
      .pipe(map((res: HttpResponse<ITraining[]>) => res.body ?? []))
      .pipe(
        map((trainings: ITraining[]) =>
          this.trainingService.addTrainingToCollectionIfMissing(trainings, this.editForm.get('training')!.value)
        )
      )
      .subscribe((trainings: ITraining[]) => (this.trainingsSharedCollection = trainings));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ITrainingRegister {
    return {
      ...new TrainingRegister(),
      id: this.editForm.get(['id'])!.value,
      userAttendance: this.editForm.get(['userAttendance'])!.value,
      cerificate: this.editForm.get(['cerificate'])!.value,
      training: this.editForm.get(['training'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
