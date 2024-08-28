import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalesByDayComponent } from './sales-by-day.component';

describe('SalesByDayComponent', () => {
  let component: SalesByDayComponent;
  let fixture: ComponentFixture<SalesByDayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SalesByDayComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SalesByDayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
