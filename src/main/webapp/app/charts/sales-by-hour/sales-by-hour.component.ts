import { Component, OnInit, OnDestroy } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { Subscription } from 'rxjs';
import { OrderItemService } from '../../entities/order-item/service/order-item.service';

@Component({
  selector: 'jhi-sales-by-hour',
  templateUrl: './sales-by-hour.component.html',
  styleUrls: ['./sales-by-hour.component.scss'],
})
export class SalesByHourComponent implements OnInit, OnDestroy {
  salesByHourData: any[] = [];
  private salesByHourChart: Chart | undefined;
  private subscriptions: Subscription[] = [];

  constructor(private orderItemService: OrderItemService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.loadSalesByHour();
  }

  ngOnDestroy(): void {
    this.destroyChart(this.salesByHourChart);
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadSalesByHour(): void {
    const { startDate, endDate } = this.getDefaultDateRange();
    this.orderItemService.getSalesByHour(startDate, endDate).subscribe(data => {
      this.salesByHourData = data;
      this.renderSalesByHourChart();
    });
  }

  private renderSalesByHourChart(): void {
    this.destroyChart(this.salesByHourChart);

    const ctx = document.getElementById('salesByHourChart') as HTMLCanvasElement;
    if (!ctx) return;

    const hours = this.salesByHourData.map(d => d.hour);
    const salesData = this.salesByHourData.map(d => d.totalSales);

    this.salesByHourChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: hours,
        datasets: [
          {
            label: 'Sales by Hour',
            data: salesData,
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
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
              text: 'Hour of the Day',
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

  private getDefaultDateRange(): { startDate: string; endDate: string } {
    const endDate = new Date(); // Today's date
    const startDate = new Date();
    startDate.setFullYear(startDate.getFullYear() - 5); // 5 years ago

    return {
      startDate: startDate.toISOString().split('T')[0],
      endDate: endDate.toISOString().split('T')[0],
    };
  }

  private destroyChart(chart: Chart | undefined): void {
    if (chart) {
      chart.destroy();
    }
  }
}
