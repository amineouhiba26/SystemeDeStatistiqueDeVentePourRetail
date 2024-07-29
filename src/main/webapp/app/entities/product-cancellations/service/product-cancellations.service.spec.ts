import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductCancellations } from '../product-cancellations.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../product-cancellations.test-samples';

import { ProductCancellationsService } from './product-cancellations.service';

const requireRestSample: IProductCancellations = {
  ...sampleWithRequiredData,
};

describe('ProductCancellations Service', () => {
  let service: ProductCancellationsService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductCancellations | IProductCancellations[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductCancellationsService);
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

    it('should create a ProductCancellations', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const productCancellations = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productCancellations).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductCancellations', () => {
      const productCancellations = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productCancellations).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductCancellations', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductCancellations', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductCancellations', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductCancellationsToCollectionIfMissing', () => {
      it('should add a ProductCancellations to an empty array', () => {
        const productCancellations: IProductCancellations = sampleWithRequiredData;
        expectedResult = service.addProductCancellationsToCollectionIfMissing([], productCancellations);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productCancellations);
      });

      it('should not add a ProductCancellations to an array that contains it', () => {
        const productCancellations: IProductCancellations = sampleWithRequiredData;
        const productCancellationsCollection: IProductCancellations[] = [
          {
            ...productCancellations,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductCancellationsToCollectionIfMissing(productCancellationsCollection, productCancellations);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductCancellations to an array that doesn't contain it", () => {
        const productCancellations: IProductCancellations = sampleWithRequiredData;
        const productCancellationsCollection: IProductCancellations[] = [sampleWithPartialData];
        expectedResult = service.addProductCancellationsToCollectionIfMissing(productCancellationsCollection, productCancellations);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productCancellations);
      });

      it('should add only unique ProductCancellations to an array', () => {
        const productCancellationsArray: IProductCancellations[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productCancellationsCollection: IProductCancellations[] = [sampleWithRequiredData];
        expectedResult = service.addProductCancellationsToCollectionIfMissing(productCancellationsCollection, ...productCancellationsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productCancellations: IProductCancellations = sampleWithRequiredData;
        const productCancellations2: IProductCancellations = sampleWithPartialData;
        expectedResult = service.addProductCancellationsToCollectionIfMissing([], productCancellations, productCancellations2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productCancellations);
        expect(expectedResult).toContain(productCancellations2);
      });

      it('should accept null and undefined values', () => {
        const productCancellations: IProductCancellations = sampleWithRequiredData;
        expectedResult = service.addProductCancellationsToCollectionIfMissing([], null, productCancellations, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productCancellations);
      });

      it('should return initial array if no ProductCancellations is added', () => {
        const productCancellationsCollection: IProductCancellations[] = [sampleWithRequiredData];
        expectedResult = service.addProductCancellationsToCollectionIfMissing(productCancellationsCollection, undefined, null);
        expect(expectedResult).toEqual(productCancellationsCollection);
      });
    });

    describe('compareProductCancellations', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductCancellations(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareProductCancellations(entity1, entity2);
        const compareResult2 = service.compareProductCancellations(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareProductCancellations(entity1, entity2);
        const compareResult2 = service.compareProductCancellations(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareProductCancellations(entity1, entity2);
        const compareResult2 = service.compareProductCancellations(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
