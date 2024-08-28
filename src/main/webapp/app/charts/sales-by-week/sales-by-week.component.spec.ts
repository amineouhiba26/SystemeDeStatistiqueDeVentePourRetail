import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalesByWeekComponent } from './sales-by-week.component';

describe('SalesByWeekComponent', () => {
  let component: SalesByWeekComponent;
  let fixture: ComponentFixture<SalesByWeekComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SalesByWeekComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SalesByWeekComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
