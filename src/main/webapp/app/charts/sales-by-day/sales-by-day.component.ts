import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { OrderItemService } from '../../entities/order-item/service/order-item.service';

@Component({
  selector: 'jhi-sales-by-day',
  templateUrl: './sales-by-day.component.html',
  styleUrls: ['./sales-by-day.component.scss'],
})
export class SalesByDayComponent implements OnInit, OnDestroy {
  @Input() salesData: any[] = [];
  private salesByDayChart: Chart | undefined;

  constructor(private orderItemService: OrderItemService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.loadSalesData();
  }
  private getDefaultDateRange(): { startDate: string; endDate: string } {
    const endDate = new Date(); // Today's date
    const startDate = new Date();
    startDate.setFullYear(startDate.getFullYear() - 5); // 1 year ago

    console.log('Start Date:', startDate.toISOString().split('T')[0]);
    console.log('End Date:', endDate.toISOString().split('T')[0]);

    return {
      startDate: startDate.toISOString().split('T')[0],
      endDate: endDate.toISOString().split('T')[0],
    };
  }

  loadSalesData(): void {
    const { startDate, endDate } = this.getDefaultDateRange();

    this.orderItemService.getSalesByDayOfWeek(startDate, endDate).subscribe(data => {
      this.renderSalesByDayOfWeekChart(data);
    });
  }

  private renderSalesByDayOfWeekChart(data: any[]): void {
    this.destroyChart(this.salesByDayChart);

    const ctx = document.getElementById('salesByDayOfWeekChart') as HTMLCanvasElement;
    if (!ctx) return;

    const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const salesData = daysOfWeek.map(day => {
      const sales = data.find(d => d.dayOfWeek.trim() === day);
      return sales ? sales.totalSales : 0;
    });

    this.salesByDayChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: daysOfWeek,
        datasets: [
          {
            label: 'Sales by Day of the Week',
            data: salesData,
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 2,
            fill: true,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          x: {
            title: {
              display: true,
              text: 'Day of the Week',
            },
          },
          y: {
            title: {
              display: true,
              text: 'Total Sales',
            },
          },
        },
      },
    });
  }

  private destroyChart(chart: Chart | undefined): void {
    if (chart) {
      chart.destroy();
    }
  }

  ngOnDestroy(): void {
    this.destroyChart(this.salesByDayChart);
  }
}
