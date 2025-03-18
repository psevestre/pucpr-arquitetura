import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStockItem } from '../stock-item.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../stock-item.test-samples';

import { StockItemService } from './stock-item.service';

const requireRestSample: IStockItem = {
  ...sampleWithRequiredData,
};

describe('StockItem Service', () => {
  let service: StockItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IStockItem | IStockItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StockItemService);
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

    it('should create a StockItem', () => {
      const stockItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stockItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StockItem', () => {
      const stockItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stockItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StockItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StockItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StockItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStockItemToCollectionIfMissing', () => {
      it('should add a StockItem to an empty array', () => {
        const stockItem: IStockItem = sampleWithRequiredData;
        expectedResult = service.addStockItemToCollectionIfMissing([], stockItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockItem);
      });

      it('should not add a StockItem to an array that contains it', () => {
        const stockItem: IStockItem = sampleWithRequiredData;
        const stockItemCollection: IStockItem[] = [
          {
            ...stockItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStockItemToCollectionIfMissing(stockItemCollection, stockItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StockItem to an array that doesn't contain it", () => {
        const stockItem: IStockItem = sampleWithRequiredData;
        const stockItemCollection: IStockItem[] = [sampleWithPartialData];
        expectedResult = service.addStockItemToCollectionIfMissing(stockItemCollection, stockItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockItem);
      });

      it('should add only unique StockItem to an array', () => {
        const stockItemArray: IStockItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stockItemCollection: IStockItem[] = [sampleWithRequiredData];
        expectedResult = service.addStockItemToCollectionIfMissing(stockItemCollection, ...stockItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stockItem: IStockItem = sampleWithRequiredData;
        const stockItem2: IStockItem = sampleWithPartialData;
        expectedResult = service.addStockItemToCollectionIfMissing([], stockItem, stockItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockItem);
        expect(expectedResult).toContain(stockItem2);
      });

      it('should accept null and undefined values', () => {
        const stockItem: IStockItem = sampleWithRequiredData;
        expectedResult = service.addStockItemToCollectionIfMissing([], null, stockItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockItem);
      });

      it('should return initial array if no StockItem is added', () => {
        const stockItemCollection: IStockItem[] = [sampleWithRequiredData];
        expectedResult = service.addStockItemToCollectionIfMissing(stockItemCollection, undefined, null);
        expect(expectedResult).toEqual(stockItemCollection);
      });
    });

    describe('compareStockItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStockItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 23342 };
        const entity2 = null;

        const compareResult1 = service.compareStockItem(entity1, entity2);
        const compareResult2 = service.compareStockItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 23342 };
        const entity2 = { id: 21429 };

        const compareResult1 = service.compareStockItem(entity1, entity2);
        const compareResult2 = service.compareStockItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 23342 };
        const entity2 = { id: 23342 };

        const compareResult1 = service.compareStockItem(entity1, entity2);
        const compareResult2 = service.compareStockItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
