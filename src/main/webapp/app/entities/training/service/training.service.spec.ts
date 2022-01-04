import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITraining, Training } from '../training.model';

import { TrainingService } from './training.service';

describe('Training Service', () => {
  let service: TrainingService;
  let httpMock: HttpTestingController;
  let elemDefault: ITraining;
  let expectedResult: ITraining | ITraining[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrainingService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      trainingName: 'AAAAAAA',
      trainingMode: 'AAAAAAA',
      trainingType: 'AAAAAAA',
      trainingStartDate: currentDate,
      traingEndDate: currentDate,
      trainingYear: 'AAAAAAA',
      trainingRegistration: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          trainingStartDate: currentDate.format(DATE_FORMAT),
          traingEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Training', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          trainingStartDate: currentDate.format(DATE_FORMAT),
          traingEndDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          trainingStartDate: currentDate,
          traingEndDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Training()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Training', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          trainingName: 'BBBBBB',
          trainingMode: 'BBBBBB',
          trainingType: 'BBBBBB',
          trainingStartDate: currentDate.format(DATE_FORMAT),
          traingEndDate: currentDate.format(DATE_FORMAT),
          trainingYear: 'BBBBBB',
          trainingRegistration: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          trainingStartDate: currentDate,
          traingEndDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Training', () => {
      const patchObject = Object.assign(
        {
          trainingType: 'BBBBBB',
          trainingYear: 'BBBBBB',
          trainingRegistration: true,
        },
        new Training()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          trainingStartDate: currentDate,
          traingEndDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Training', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          trainingName: 'BBBBBB',
          trainingMode: 'BBBBBB',
          trainingType: 'BBBBBB',
          trainingStartDate: currentDate.format(DATE_FORMAT),
          traingEndDate: currentDate.format(DATE_FORMAT),
          trainingYear: 'BBBBBB',
          trainingRegistration: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          trainingStartDate: currentDate,
          traingEndDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Training', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTrainingToCollectionIfMissing', () => {
      it('should add a Training to an empty array', () => {
        const training: ITraining = { id: 123 };
        expectedResult = service.addTrainingToCollectionIfMissing([], training);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(training);
      });

      it('should not add a Training to an array that contains it', () => {
        const training: ITraining = { id: 123 };
        const trainingCollection: ITraining[] = [
          {
            ...training,
          },
          { id: 456 },
        ];
        expectedResult = service.addTrainingToCollectionIfMissing(trainingCollection, training);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Training to an array that doesn't contain it", () => {
        const training: ITraining = { id: 123 };
        const trainingCollection: ITraining[] = [{ id: 456 }];
        expectedResult = service.addTrainingToCollectionIfMissing(trainingCollection, training);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(training);
      });

      it('should add only unique Training to an array', () => {
        const trainingArray: ITraining[] = [{ id: 123 }, { id: 456 }, { id: 7634 }];
        const trainingCollection: ITraining[] = [{ id: 123 }];
        expectedResult = service.addTrainingToCollectionIfMissing(trainingCollection, ...trainingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const training: ITraining = { id: 123 };
        const training2: ITraining = { id: 456 };
        expectedResult = service.addTrainingToCollectionIfMissing([], training, training2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(training);
        expect(expectedResult).toContain(training2);
      });

      it('should accept null and undefined values', () => {
        const training: ITraining = { id: 123 };
        expectedResult = service.addTrainingToCollectionIfMissing([], null, training, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(training);
      });

      it('should return initial array if no Training is added', () => {
        const trainingCollection: ITraining[] = [{ id: 123 }];
        expectedResult = service.addTrainingToCollectionIfMissing(trainingCollection, undefined, null);
        expect(expectedResult).toEqual(trainingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
