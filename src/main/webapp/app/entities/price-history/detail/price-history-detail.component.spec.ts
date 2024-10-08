import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PriceHistoryDetailComponent } from './price-history-detail.component';

describe('PriceHistory Management Detail Component', () => {
  let comp: PriceHistoryDetailComponent;
  let fixture: ComponentFixture<PriceHistoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PriceHistoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ priceHistory: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(PriceHistoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PriceHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load priceHistory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.priceHistory).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
