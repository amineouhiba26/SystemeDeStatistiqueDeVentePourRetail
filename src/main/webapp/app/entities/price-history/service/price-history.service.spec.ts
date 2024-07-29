import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPriceHistory } from '../price-history.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../price-history.test-samples';

import { PriceHistoryService } from './price-history.service';

const requireRestSample: IPriceHistory = {
  ...sampleWithRequiredData,
};

describe('PriceHistory Service', () => {
  let service: PriceHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IPriceHistory | IPriceHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PriceHistoryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PriceHistory', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const priceHistory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(priceHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PriceHistory', () => {
      const priceHistory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(priceHistory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PriceHistory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PriceHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PriceHistory', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPriceHistoryToCollectionIfMissing', () => {
      it('should add a PriceHistory to an empty array', () => {
        const priceHistory: IPriceHistory = sampleWithRequiredData;
        expectedResult = service.addPriceHistoryToCollectionIfMissing([], priceHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(priceHistory);
      });

      it('should not add a PriceHistory to an array that contains it', () => {
        const priceHistory: IPriceHistory = sampleWithRequiredData;
        const priceHistoryCollection: IPriceHistory[] = [
          {
            ...priceHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPriceHistoryToCollectionIfMissing(priceHistoryCollection, priceHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PriceHistory to an array that doesn't contain it", () => {
        const priceHistory: IPriceHistory = sampleWithRequiredData;
        const priceHistoryCollection: IPriceHistory[] = [sampleWithPartialData];
        expectedResult = service.addPriceHistoryToCollectionIfMissing(priceHistoryCollection, priceHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(priceHistory);
      });

      it('should add only unique PriceHistory to an array', () => {
        const priceHistoryArray: IPriceHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const priceHistoryCollection: IPriceHistory[] = [sampleWithRequiredData];
        expectedResult = service.addPriceHistoryToCollectionIfMissing(priceHistoryCollection, ...priceHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const priceHistory: IPriceHistory = sampleWithRequiredData;
        const priceHistory2: IPriceHistory = sampleWithPartialData;
        expectedResult = service.addPriceHistoryToCollectionIfMissing([], priceHistory, priceHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(priceHistory);
        expect(expectedResult).toContain(priceHistory2);
      });

      it('should accept null and undefined values', () => {
        const priceHistory: IPriceHistory = sampleWithRequiredData;
        expectedResult = service.addPriceHistoryToCollectionIfMissing([], null, priceHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(priceHistory);
      });

      it('should return initial array if no PriceHistory is added', () => {
        const priceHistoryCollection: IPriceHistory[] = [sampleWithRequiredData];
        expectedResult = service.addPriceHistoryToCollectionIfMissing(priceHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(priceHistoryCollection);
      });
    });

    describe('comparePriceHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePriceHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.comparePriceHistory(entity1, entity2);
        const compareResult2 = service.comparePriceHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.comparePriceHistory(entity1, entity2);
        const compareResult2 = service.comparePriceHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.comparePriceHistory(entity1, entity2);
        const compareResult2 = service.comparePriceHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
