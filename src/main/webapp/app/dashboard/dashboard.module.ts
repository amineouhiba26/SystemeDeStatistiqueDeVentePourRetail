import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard.component';
import { ChartsModule } from '../low-stock-chart/low-stock.module';

@NgModule({
  declarations: [DashboardComponent],
  imports: [CommonModule, ChartsModule], // Import ChartsModule to use LowStockChartComponent
})
export class DashboardModule {}
