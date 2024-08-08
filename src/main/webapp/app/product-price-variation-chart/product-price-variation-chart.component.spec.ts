import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductPriceVariationChartComponent } from './product-price-variation-chart.component';

describe('ProductPriceVariationChartComponent', () => {
  let component: ProductPriceVariationChartComponent;
  let fixture: ComponentFixture<ProductPriceVariationChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductPriceVariationChartComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductPriceVariationChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
