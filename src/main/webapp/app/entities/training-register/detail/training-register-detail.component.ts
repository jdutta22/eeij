import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrainingRegister } from '../training-register.model';

@Component({
  selector: 'jhi-training-register-detail',
  templateUrl: './training-register-detail.component.html',
})
export class TrainingRegisterDetailComponent implements OnInit {
  trainingRegister: ITrainingRegister | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trainingRegister }) => {
      this.trainingRegister = trainingRegister;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
