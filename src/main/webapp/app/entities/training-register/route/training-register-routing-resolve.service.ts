import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrainingRegister, TrainingRegister } from '../training-register.model';
import { TrainingRegisterService } from '../service/training-register.service';

@Injectable({ providedIn: 'root' })
export class TrainingRegisterRoutingResolveService implements Resolve<ITrainingRegister> {
  constructor(protected service: TrainingRegisterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrainingRegister> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trainingRegister: HttpResponse<TrainingRegister>) => {
          if (trainingRegister.body) {
            return of(trainingRegister.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TrainingRegister());
  }
}
