import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITrainingRegister, TrainingRegister } from '../training-register.model';

import { TrainingRegisterService } from './training-register.service';

describe('TrainingRegister Service', () => {
  let service: TrainingRegisterService;
  let httpMock: HttpTestingController;
  let elemDefault: ITrainingRegister;
  let expectedResult: ITrainingRegister | ITrainingRegister[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrainingRegisterService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      userAttendance: 0,
      cerificate: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TrainingRegister', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TrainingRegister()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrainingRegister', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          userAttendance: 1,
          cerificate: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrainingRegister', () => {
      const patchObject = Object.assign(
        {
          userAttendance: 1,
        },
        new TrainingRegister()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrainingRegister', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          userAttendance: 1,
          cerificate: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TrainingRegister', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTrainingRegisterToCollectionIfMissing', () => {
      it('should add a TrainingRegister to an empty array', () => {
        const trainingRegister: ITrainingRegister = { id: 123 };
        expectedResult = service.addTrainingRegisterToCollectionIfMissing([], trainingRegister);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trainingRegister);
      });

      it('should not add a TrainingRegister to an array that contains it', () => {
        const trainingRegister: ITrainingRegister = { id: 123 };
        const trainingRegisterCollection: ITrainingRegister[] = [
          {
            ...trainingRegister,
          },
          { id: 456 },
        ];
        expectedResult = service.addTrainingRegisterToCollectionIfMissing(trainingRegisterCollection, trainingRegister);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrainingRegister to an array that doesn't contain it", () => {
        const trainingRegister: ITrainingRegister = { id: 123 };
        const trainingRegisterCollection: ITrainingRegister[] = [{ id: 456 }];
        expectedResult = service.addTrainingRegisterToCollectionIfMissing(trainingRegisterCollection, trainingRegister);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trainingRegister);
      });

      it('should add only unique TrainingRegister to an array', () => {
        const trainingRegisterArray: ITrainingRegister[] = [{ id: 123 }, { id: 456 }, { id: 57908 }];
        const trainingRegisterCollection: ITrainingRegister[] = [{ id: 123 }];
        expectedResult = service.addTrainingRegisterToCollectionIfMissing(trainingRegisterCollection, ...trainingRegisterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trainingRegister: ITrainingRegister = { id: 123 };
        const trainingRegister2: ITrainingRegister = { id: 456 };
        expectedResult = service.addTrainingRegisterToCollectionIfMissing([], trainingRegister, trainingRegister2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trainingRegister);
        expect(expectedResult).toContain(trainingRegister2);
      });

      it('should accept null and undefined values', () => {
        const trainingRegister: ITrainingRegister = { id: 123 };
        expectedResult = service.addTrainingRegisterToCollectionIfMissing([], null, trainingRegister, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trainingRegister);
      });

      it('should return initial array if no TrainingRegister is added', () => {
        const trainingRegisterCollection: ITrainingRegister[] = [{ id: 123 }];
        expectedResult = service.addTrainingRegisterToCollectionIfMissing(trainingRegisterCollection, undefined, null);
        expect(expectedResult).toEqual(trainingRegisterCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
