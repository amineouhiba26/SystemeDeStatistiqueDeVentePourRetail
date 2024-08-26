import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LowStockChartComponent } from './low-stock-chart.component';

@NgModule({
  declarations: [LowStockChartComponent],
  imports: [CommonModule],
  exports: [LowStockChartComponent], // Export so other modules can use it
})
export class ChartsModule {}
