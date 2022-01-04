import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrainingRegisterDetailComponent } from './training-register-detail.component';

describe('TrainingRegister Management Detail Component', () => {
  let comp: TrainingRegisterDetailComponent;
  let fixture: ComponentFixture<TrainingRegisterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrainingRegisterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ trainingRegister: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TrainingRegisterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TrainingRegisterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trainingRegister on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.trainingRegister).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
