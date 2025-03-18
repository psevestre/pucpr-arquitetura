import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOrderCustomer } from '../order-customer.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../order-customer.test-samples';

import { OrderCustomerService } from './order-customer.service';

const requireRestSample: IOrderCustomer = {
  ...sampleWithRequiredData,
};

describe('OrderCustomer Service', () => {
  let service: OrderCustomerService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderCustomer | IOrderCustomer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderCustomerService);
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

    it('should create a OrderCustomer', () => {
      const orderCustomer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderCustomer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderCustomer', () => {
      const orderCustomer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderCustomer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderCustomer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderCustomer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderCustomer', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrderCustomerToCollectionIfMissing', () => {
      it('should add a OrderCustomer to an empty array', () => {
        const orderCustomer: IOrderCustomer = sampleWithRequiredData;
        expectedResult = service.addOrderCustomerToCollectionIfMissing([], orderCustomer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderCustomer);
      });

      it('should not add a OrderCustomer to an array that contains it', () => {
        const orderCustomer: IOrderCustomer = sampleWithRequiredData;
        const orderCustomerCollection: IOrderCustomer[] = [
          {
            ...orderCustomer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderCustomerToCollectionIfMissing(orderCustomerCollection, orderCustomer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderCustomer to an array that doesn't contain it", () => {
        const orderCustomer: IOrderCustomer = sampleWithRequiredData;
        const orderCustomerCollection: IOrderCustomer[] = [sampleWithPartialData];
        expectedResult = service.addOrderCustomerToCollectionIfMissing(orderCustomerCollection, orderCustomer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderCustomer);
      });

      it('should add only unique OrderCustomer to an array', () => {
        const orderCustomerArray: IOrderCustomer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderCustomerCollection: IOrderCustomer[] = [sampleWithRequiredData];
        expectedResult = service.addOrderCustomerToCollectionIfMissing(orderCustomerCollection, ...orderCustomerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderCustomer: IOrderCustomer = sampleWithRequiredData;
        const orderCustomer2: IOrderCustomer = sampleWithPartialData;
        expectedResult = service.addOrderCustomerToCollectionIfMissing([], orderCustomer, orderCustomer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderCustomer);
        expect(expectedResult).toContain(orderCustomer2);
      });

      it('should accept null and undefined values', () => {
        const orderCustomer: IOrderCustomer = sampleWithRequiredData;
        expectedResult = service.addOrderCustomerToCollectionIfMissing([], null, orderCustomer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderCustomer);
      });

      it('should return initial array if no OrderCustomer is added', () => {
        const orderCustomerCollection: IOrderCustomer[] = [sampleWithRequiredData];
        expectedResult = service.addOrderCustomerToCollectionIfMissing(orderCustomerCollection, undefined, null);
        expect(expectedResult).toEqual(orderCustomerCollection);
      });
    });

    describe('compareOrderCustomer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderCustomer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 18682 };
        const entity2 = null;

        const compareResult1 = service.compareOrderCustomer(entity1, entity2);
        const compareResult2 = service.compareOrderCustomer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 18682 };
        const entity2 = { id: 4148 };

        const compareResult1 = service.compareOrderCustomer(entity1, entity2);
        const compareResult2 = service.compareOrderCustomer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 18682 };
        const entity2 = { id: 18682 };

        const compareResult1 = service.compareOrderCustomer(entity1, entity2);
        const compareResult2 = service.compareOrderCustomer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
