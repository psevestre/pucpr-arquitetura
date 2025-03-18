import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDeliveryItem } from '../delivery-item.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../delivery-item.test-samples';

import { DeliveryItemService } from './delivery-item.service';

const requireRestSample: IDeliveryItem = {
  ...sampleWithRequiredData,
};

describe('DeliveryItem Service', () => {
  let service: DeliveryItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeliveryItem | IDeliveryItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DeliveryItemService);
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

    it('should create a DeliveryItem', () => {
      const deliveryItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(deliveryItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeliveryItem', () => {
      const deliveryItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(deliveryItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeliveryItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeliveryItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DeliveryItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeliveryItemToCollectionIfMissing', () => {
      it('should add a DeliveryItem to an empty array', () => {
        const deliveryItem: IDeliveryItem = sampleWithRequiredData;
        expectedResult = service.addDeliveryItemToCollectionIfMissing([], deliveryItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryItem);
      });

      it('should not add a DeliveryItem to an array that contains it', () => {
        const deliveryItem: IDeliveryItem = sampleWithRequiredData;
        const deliveryItemCollection: IDeliveryItem[] = [
          {
            ...deliveryItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeliveryItemToCollectionIfMissing(deliveryItemCollection, deliveryItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeliveryItem to an array that doesn't contain it", () => {
        const deliveryItem: IDeliveryItem = sampleWithRequiredData;
        const deliveryItemCollection: IDeliveryItem[] = [sampleWithPartialData];
        expectedResult = service.addDeliveryItemToCollectionIfMissing(deliveryItemCollection, deliveryItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryItem);
      });

      it('should add only unique DeliveryItem to an array', () => {
        const deliveryItemArray: IDeliveryItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deliveryItemCollection: IDeliveryItem[] = [sampleWithRequiredData];
        expectedResult = service.addDeliveryItemToCollectionIfMissing(deliveryItemCollection, ...deliveryItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deliveryItem: IDeliveryItem = sampleWithRequiredData;
        const deliveryItem2: IDeliveryItem = sampleWithPartialData;
        expectedResult = service.addDeliveryItemToCollectionIfMissing([], deliveryItem, deliveryItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryItem);
        expect(expectedResult).toContain(deliveryItem2);
      });

      it('should accept null and undefined values', () => {
        const deliveryItem: IDeliveryItem = sampleWithRequiredData;
        expectedResult = service.addDeliveryItemToCollectionIfMissing([], null, deliveryItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryItem);
      });

      it('should return initial array if no DeliveryItem is added', () => {
        const deliveryItemCollection: IDeliveryItem[] = [sampleWithRequiredData];
        expectedResult = service.addDeliveryItemToCollectionIfMissing(deliveryItemCollection, undefined, null);
        expect(expectedResult).toEqual(deliveryItemCollection);
      });
    });

    describe('compareDeliveryItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeliveryItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 16562 };
        const entity2 = null;

        const compareResult1 = service.compareDeliveryItem(entity1, entity2);
        const compareResult2 = service.compareDeliveryItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 16562 };
        const entity2 = { id: 12510 };

        const compareResult1 = service.compareDeliveryItem(entity1, entity2);
        const compareResult2 = service.compareDeliveryItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 16562 };
        const entity2 = { id: 16562 };

        const compareResult1 = service.compareDeliveryItem(entity1, entity2);
        const compareResult2 = service.compareDeliveryItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
