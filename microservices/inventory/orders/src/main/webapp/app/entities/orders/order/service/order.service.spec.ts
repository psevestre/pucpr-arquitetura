import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOrder } from '../order.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../order.test-samples';

import { OrderService, RestOrder } from './order.service';

const requireRestSample: RestOrder = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
};

describe('Order Service', () => {
  let service: OrderService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrder | IOrder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderService);
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

    it('should create a Order', () => {
      const order = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(order).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Order', () => {
      const order = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(order).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Order', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Order', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Order', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrderToCollectionIfMissing', () => {
      it('should add a Order to an empty array', () => {
        const order: IOrder = sampleWithRequiredData;
        expectedResult = service.addOrderToCollectionIfMissing([], order);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(order);
      });

      it('should not add a Order to an array that contains it', () => {
        const order: IOrder = sampleWithRequiredData;
        const orderCollection: IOrder[] = [
          {
            ...order,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderToCollectionIfMissing(orderCollection, order);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Order to an array that doesn't contain it", () => {
        const order: IOrder = sampleWithRequiredData;
        const orderCollection: IOrder[] = [sampleWithPartialData];
        expectedResult = service.addOrderToCollectionIfMissing(orderCollection, order);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(order);
      });

      it('should add only unique Order to an array', () => {
        const orderArray: IOrder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderCollection: IOrder[] = [sampleWithRequiredData];
        expectedResult = service.addOrderToCollectionIfMissing(orderCollection, ...orderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const order: IOrder = sampleWithRequiredData;
        const order2: IOrder = sampleWithPartialData;
        expectedResult = service.addOrderToCollectionIfMissing([], order, order2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(order);
        expect(expectedResult).toContain(order2);
      });

      it('should accept null and undefined values', () => {
        const order: IOrder = sampleWithRequiredData;
        expectedResult = service.addOrderToCollectionIfMissing([], null, order, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(order);
      });

      it('should return initial array if no Order is added', () => {
        const orderCollection: IOrder[] = [sampleWithRequiredData];
        expectedResult = service.addOrderToCollectionIfMissing(orderCollection, undefined, null);
        expect(expectedResult).toEqual(orderCollection);
      });
    });

    describe('compareOrder', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 14644 };
        const entity2 = null;

        const compareResult1 = service.compareOrder(entity1, entity2);
        const compareResult2 = service.compareOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 14644 };
        const entity2 = { id: 4253 };

        const compareResult1 = service.compareOrder(entity1, entity2);
        const compareResult2 = service.compareOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 14644 };
        const entity2 = { id: 14644 };

        const compareResult1 = service.compareOrder(entity1, entity2);
        const compareResult2 = service.compareOrder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
