import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrainingRegister, getTrainingRegisterIdentifier } from '../training-register.model';

export type EntityResponseType = HttpResponse<ITrainingRegister>;
export type EntityArrayResponseType = HttpResponse<ITrainingRegister[]>;

@Injectable({ providedIn: 'root' })
export class TrainingRegisterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/training-registers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trainingRegister: ITrainingRegister): Observable<EntityResponseType> {
    return this.http.post<ITrainingRegister>(this.resourceUrl, trainingRegister, { observe: 'response' });
  }

  update(trainingRegister: ITrainingRegister): Observable<EntityResponseType> {
    return this.http.put<ITrainingRegister>(
      `${this.resourceUrl}/${getTrainingRegisterIdentifier(trainingRegister) as number}`,
      trainingRegister,
      { observe: 'response' }
    );
  }

  partialUpdate(trainingRegister: ITrainingRegister): Observable<EntityResponseType> {
    return this.http.patch<ITrainingRegister>(
      `${this.resourceUrl}/${getTrainingRegisterIdentifier(trainingRegister) as number}`,
      trainingRegister,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrainingRegister>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrainingRegister[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTrainingRegisterToCollectionIfMissing(
    trainingRegisterCollection: ITrainingRegister[],
    ...trainingRegistersToCheck: (ITrainingRegister | null | undefined)[]
  ): ITrainingRegister[] {
    const trainingRegisters: ITrainingRegister[] = trainingRegistersToCheck.filter(isPresent);
    if (trainingRegisters.length > 0) {
      const trainingRegisterCollectionIdentifiers = trainingRegisterCollection.map(
        trainingRegisterItem => getTrainingRegisterIdentifier(trainingRegisterItem)!
      );
      const trainingRegistersToAdd = trainingRegisters.filter(trainingRegisterItem => {
        const trainingRegisterIdentifier = getTrainingRegisterIdentifier(trainingRegisterItem);
        if (trainingRegisterIdentifier == null || trainingRegisterCollectionIdentifiers.includes(trainingRegisterIdentifier)) {
          return false;
        }
        trainingRegisterCollectionIdentifiers.push(trainingRegisterIdentifier);
        return true;
      });
      return [...trainingRegistersToAdd, ...trainingRegisterCollection];
    }
    return trainingRegisterCollection;
  }
}
