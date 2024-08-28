import { Component, OnInit, Input, OnDestroy, SimpleChanges, OnChanges } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { Subscription } from 'rxjs';
import { OrderItemService } from '../../entities/order-item/service/order-item.service';

@Component({
  selector: 'jhi-sales-by-week',
  templateUrl: './sales-by-week.component.html',
  styleUrls: ['./sales-by-week.component.scss'],
})
export class SalesByWeekComponent implements OnInit, OnDestroy, OnChanges {
  private salesByWeekChart: Chart | undefined;
  private subscriptions: Subscription[] = [];

  @Input() startDate: string = '';
  @Input() endDate: string = '';

  constructor(private orderItemService: OrderItemService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    // Ensure that the dates are set properly
    if (this.startDate && this.endDate) {
      this.loadSalesData();
    } else {
      console.error('Start date or end date is missing');
      // Optionally set default dates or handle the error
      const { startDate, endDate } = this.getDefaultDateRange();
      this.startDate = startDate;
      this.endDate = endDate;
      this.loadSalesData(); // Retry loading data with default dates
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['startDate'] || changes['endDate']) {
      console.log('Input changed:', changes);
      if (this.startDate && this.endDate) {
        this.loadSalesData();
      } else {
        console.error('Start date or end date is missing');
      }
    }
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

  private loadSalesData(): void {
    console.log('Fetching sales data from', this.startDate, 'to', this.endDate);

    this.orderItemService.getSalesByWeek(this.startDate, this.endDate).subscribe(
      data => {
        console.log('Sales data received:', data);
        this.renderSalesByWeekChart(data);
      },
      error => {
        console.error('Error loading sales data', error);
      }
    );
  }

  private renderSalesByWeekChart(data: any[]): void {
    this.destroyChart(this.salesByWeekChart);

    const ctx = document.getElementById('salesByWeekChart') as HTMLCanvasElement;
    if (!ctx) {
      console.error('Canvas element with id "salesByWeekChart" not found');
      return;
    }

    const weeks = data.map(d => d.week);
    const salesData = data.map(d => d.totalSales);

    console.log('Rendering chart with weeks:', weeks, 'and sales data:', salesData);

    this.salesByWeekChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: weeks,
        datasets: [
          {
            label: 'Weekly Sales Revenue',
            data: salesData,
            backgroundColor: 'rgba(153, 102, 255, 0.2)',
            borderColor: 'rgba(153, 102, 255, 1)',
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
              text: 'Week',
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
    this.destroyChart(this.salesByWeekChart);
  }
}
