import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalesByHourComponent } from './sales-by-hour.component';

describe('SalesByHourComponent', () => {
  let component: SalesByHourComponent;
  let fixture: ComponentFixture<SalesByHourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SalesByHourComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SalesByHourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
