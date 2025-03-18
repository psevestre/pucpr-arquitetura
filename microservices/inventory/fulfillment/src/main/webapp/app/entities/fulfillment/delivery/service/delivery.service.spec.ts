import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDelivery } from '../delivery.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../delivery.test-samples';

import { DeliveryService, RestDelivery } from './delivery.service';

const requireRestSample: RestDelivery = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('Delivery Service', () => {
  let service: DeliveryService;
  let httpMock: HttpTestingController;
  let expectedResult: IDelivery | IDelivery[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DeliveryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Delivery', () => {
      const delivery = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(delivery).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Delivery', () => {
      const delivery = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(delivery).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Delivery', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Delivery', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Delivery', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeliveryToCollectionIfMissing', () => {
      it('should add a Delivery to an empty array', () => {
        const delivery: IDelivery = sampleWithRequiredData;
        expectedResult = service.addDeliveryToCollectionIfMissing([], delivery);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(delivery);
      });

      it('should not add a Delivery to an array that contains it', () => {
        const delivery: IDelivery = sampleWithRequiredData;
        const deliveryCollection: IDelivery[] = [
          {
            ...delivery,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeliveryToCollectionIfMissing(deliveryCollection, delivery);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Delivery to an array that doesn't contain it", () => {
        const delivery: IDelivery = sampleWithRequiredData;
        const deliveryCollection: IDelivery[] = [sampleWithPartialData];
        expectedResult = service.addDeliveryToCollectionIfMissing(deliveryCollection, delivery);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(delivery);
      });

      it('should add only unique Delivery to an array', () => {
        const deliveryArray: IDelivery[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deliveryCollection: IDelivery[] = [sampleWithRequiredData];
        expectedResult = service.addDeliveryToCollectionIfMissing(deliveryCollection, ...deliveryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const delivery: IDelivery = sampleWithRequiredData;
        const delivery2: IDelivery = sampleWithPartialData;
        expectedResult = service.addDeliveryToCollectionIfMissing([], delivery, delivery2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(delivery);
        expect(expectedResult).toContain(delivery2);
      });

      it('should accept null and undefined values', () => {
        const delivery: IDelivery = sampleWithRequiredData;
        expectedResult = service.addDeliveryToCollectionIfMissing([], null, delivery, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(delivery);
      });

      it('should return initial array if no Delivery is added', () => {
        const deliveryCollection: IDelivery[] = [sampleWithRequiredData];
        expectedResult = service.addDeliveryToCollectionIfMissing(deliveryCollection, undefined, null);
        expect(expectedResult).toEqual(deliveryCollection);
      });
    });

    describe('compareDelivery', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDelivery(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 16325 };
        const entity2 = null;

        const compareResult1 = service.compareDelivery(entity1, entity2);
        const compareResult2 = service.compareDelivery(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 16325 };
        const entity2 = { id: 9797 };

        const compareResult1 = service.compareDelivery(entity1, entity2);
        const compareResult2 = service.compareDelivery(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 16325 };
        const entity2 = { id: 16325 };

        const compareResult1 = service.compareDelivery(entity1, entity2);
        const compareResult2 = service.compareDelivery(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
