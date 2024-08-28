import { Component, OnInit, OnDestroy } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { Subscription } from 'rxjs';
import { OrderItemService } from '../../entities/order-item/service/order-item.service';

@Component({
  selector: 'jhi-sales-by-month',
  templateUrl: './sales-by-month.component.html',
  styleUrls: ['./sales-by-month.component.scss'],
})
export class SalesByMonthComponent implements OnInit, OnDestroy {
  private salesByMonthChart: Chart | undefined;
  private subscriptions: Subscription[] = [];

  constructor(private orderItemService: OrderItemService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.loadSalesData();
  }

  ngOnDestroy(): void {
    this.destroyChart(this.salesByMonthChart);
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  private loadSalesData(): void {
    const { startDate, endDate } = this.getDefaultDateRange();
    this.orderItemService.getSalesByMonth(startDate, endDate).subscribe(data => {
      this.renderSalesByMonthChart(data);
    });
  }

  private renderSalesByMonthChart(data: any[]): void {
    this.destroyChart(this.salesByMonthChart);

    const ctx = document.getElementById('salesByMonthChart') as HTMLCanvasElement;
    if (!ctx) return;

    const months = data.map(d => d.month);
    const salesData = data.map(d => d.totalSales);

    this.salesByMonthChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: months,
        datasets: [
          {
            label: 'Sales by Month',
            data: salesData,
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          x: {
            title: {
              display: true,
              text: 'Month',
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
