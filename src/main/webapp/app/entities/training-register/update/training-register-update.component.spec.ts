jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TrainingRegisterService } from '../service/training-register.service';
import { ITrainingRegister, TrainingRegister } from '../training-register.model';
import { ITraining } from 'app/entities/training/training.model';
import { TrainingService } from 'app/entities/training/service/training.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TrainingRegisterUpdateComponent } from './training-register-update.component';

describe('TrainingRegister Management Update Component', () => {
  let comp: TrainingRegisterUpdateComponent;
  let fixture: ComponentFixture<TrainingRegisterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trainingRegisterService: TrainingRegisterService;
  let trainingService: TrainingService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TrainingRegisterUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TrainingRegisterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrainingRegisterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trainingRegisterService = TestBed.inject(TrainingRegisterService);
    trainingService = TestBed.inject(TrainingService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Training query and add missing value', () => {
      const trainingRegister: ITrainingRegister = { id: 456 };
      const training: ITraining = { id: 39076 };
      trainingRegister.training = training;

      const trainingCollection: ITraining[] = [{ id: 10770 }];
      jest.spyOn(trainingService, 'query').mockReturnValue(of(new HttpResponse({ body: trainingCollection })));
      const additionalTrainings = [training];
      const expectedCollection: ITraining[] = [...additionalTrainings, ...trainingCollection];
      jest.spyOn(trainingService, 'addTrainingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trainingRegister });
      comp.ngOnInit();

      expect(trainingService.query).toHaveBeenCalled();
      expect(trainingService.addTrainingToCollectionIfMissing).toHaveBeenCalledWith(trainingCollection, ...additionalTrainings);
      expect(comp.trainingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const trainingRegister: ITrainingRegister = { id: 456 };
      const user: IUser = { id: 47678 };
      trainingRegister.user = user;

      const userCollection: IUser[] = [{ id: 96460 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trainingRegister });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const trainingRegister: ITrainingRegister = { id: 456 };
      const training: ITraining = { id: 53195 };
      trainingRegister.training = training;
      const user: IUser = { id: 72051 };
      trainingRegister.user = user;

      activatedRoute.data = of({ trainingRegister });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(trainingRegister));
      expect(comp.trainingsSharedCollection).toContain(training);
      expect(comp.usersSharedCollection).toContain(user);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrainingRegister>>();
      const trainingRegister = { id: 123 };
      jest.spyOn(trainingRegisterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainingRegister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trainingRegister }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(trainingRegisterService.update).toHaveBeenCalledWith(trainingRegister);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrainingRegister>>();
      const trainingRegister = new TrainingRegister();
      jest.spyOn(trainingRegisterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainingRegister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trainingRegister }));
      saveSubject.complete();

      // THEN
      expect(trainingRegisterService.create).toHaveBeenCalledWith(trainingRegister);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrainingRegister>>();
      const trainingRegister = { id: 123 };
      jest.spyOn(trainingRegisterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainingRegister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trainingRegisterService.update).toHaveBeenCalledWith(trainingRegister);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTrainingById', () => {
      it('Should return tracked Training primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTrainingById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
