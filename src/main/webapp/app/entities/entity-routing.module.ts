import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-details',
        data: { pageTitle: 'UserDetails' },
        loadChildren: () => import('./user-details/user-details.module').then(m => m.UserDetailsModule),
      },
      {
        path: 'training',
        data: { pageTitle: 'Trainings' },
        loadChildren: () => import('./training/training.module').then(m => m.TrainingModule),
      },
      {
        path: 'training-register',
        data: { pageTitle: 'TrainingRegisters' },
        loadChildren: () => import('./training-register/training-register.module').then(m => m.TrainingRegisterModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
