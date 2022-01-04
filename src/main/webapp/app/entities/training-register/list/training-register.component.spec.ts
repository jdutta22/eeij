import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TrainingRegisterService } from '../service/training-register.service';

import { TrainingRegisterComponent } from './training-register.component';

describe('TrainingRegister Management Component', () => {
  let comp: TrainingRegisterComponent;
  let fixture: ComponentFixture<TrainingRegisterComponent>;
  let service: TrainingRegisterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TrainingRegisterComponent],
    })
      .overrideTemplate(TrainingRegisterComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrainingRegisterComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TrainingRegisterService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.trainingRegisters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
