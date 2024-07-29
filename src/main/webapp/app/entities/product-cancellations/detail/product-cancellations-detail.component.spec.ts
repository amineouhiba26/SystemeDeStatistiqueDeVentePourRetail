import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductCancellationsDetailComponent } from './product-cancellations-detail.component';

describe('ProductCancellations Management Detail Component', () => {
  let comp: ProductCancellationsDetailComponent;
  let fixture: ComponentFixture<ProductCancellationsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductCancellationsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productCancellations: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(ProductCancellationsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductCancellationsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productCancellations on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productCancellations).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
