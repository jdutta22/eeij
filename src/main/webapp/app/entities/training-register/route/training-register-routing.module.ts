import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrainingRegisterComponent } from '../list/training-register.component';
import { TrainingRegisterDetailComponent } from '../detail/training-register-detail.component';
import { TrainingRegisterUpdateComponent } from '../update/training-register-update.component';
import { TrainingRegisterRoutingResolveService } from './training-register-routing-resolve.service';

const trainingRegisterRoute: Routes = [
  {
    path: '',
    component: TrainingRegisterComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrainingRegisterDetailComponent,
    resolve: {
      trainingRegister: TrainingRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrainingRegisterUpdateComponent,
    resolve: {
      trainingRegister: TrainingRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrainingRegisterUpdateComponent,
    resolve: {
      trainingRegister: TrainingRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trainingRegisterRoute)],
  exports: [RouterModule],
})
export class TrainingRegisterRoutingModule {}
