import { Component, OnInit } from '@angular/core';
import { OrderItemService } from '../entities/order-item/service/order-item.service';
import { OrderService } from '../entities/order/service/order.service';
import { Chart, registerables } from 'chart.js';

@Component({
  selector: 'jhi-app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  products: any[] = [];
  productQuantities: { [productId: string]: number } = {};
  orderStatuses: { [status: string]: number } = {};
  salesByHourData: any[] = [];

  visibleProducts: any[] = [];
  showMore: boolean = false;

  private orderStatusChart: Chart | undefined;
  private productChart: Chart | undefined;
  private salesByDayOfWeekChart: Chart | undefined;
  private salesByWeekChart: Chart | undefined;
  private salesByMonthChart: Chart | undefined;
  private salesByHourChart: Chart | undefined;

  currentChart: 'dayOfWeek' | 'week' | 'month' = 'dayOfWeek';

  constructor(private orderItemService: OrderItemService, private orderService: OrderService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.loadTopProducts();
    this.loadOrderStatuses();
    this.loadSalesData();
  }

  loadTopProducts(): void {
    this.orderItemService.getTop10MostSoldProducts().subscribe(data => {
      this.products = data;
      this.updateVisibleProducts();
      this.loadProductQuantities();
    });
  }

  loadProductQuantities(): void {
    this.products.forEach(product => {
      this.orderItemService.getTotalQuantityByProductId(product.id).subscribe(quantity => {
        this.productQuantities[product.id] = quantity;
        this.renderProductChart();
      });
    });
  }

  loadOrderStatuses(): void {
    this.orderService.getOrderStatuses().subscribe(response => {
      const statuses = response.body || [];
      this.orderStatuses = statuses.reduce((acc: { [key: string]: number }, status: string) => {
        acc[status] = (acc[status] || 0) + 1;
        return acc;
      }, {});

      this.renderStatusChart();
    });
  }

  loadSalesData(): void {
    const { startDate, endDate } = this.getDefaultDateRange();

    this.orderItemService.getSalesByDayOfWeek(startDate, endDate).subscribe(data => {
      this.renderSalesByDayOfWeekChart(data);
    });

    this.orderItemService.getSalesByWeek(startDate, endDate).subscribe(data => {
      this.renderSalesByWeekChart(data);
    });

    this.orderItemService.getSalesByMonth(startDate, endDate).subscribe(data => {
      this.renderSalesByMonthChart(data);
    });

    this.loadSalesByHour(startDate, endDate); // Fetch sales data by hour
  }

  loadSalesByHour(startDate: string, endDate: string): void {
    this.orderItemService.getSalesByHour(startDate, endDate).subscribe(data => {
      this.salesByHourData = data;
      this.renderSalesByHourChart();
    });
  }

  private renderStatusChart(): void {
    this.destroyChart(this.orderStatusChart);

    const ctx = document.getElementById('statusChart') as HTMLCanvasElement;
    if (!ctx) return;

    const labels = Object.keys(this.orderStatuses);
    const data = Object.values(this.orderStatuses);

    this.orderStatusChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Order Status Counts',
            data: data,
            backgroundColor: 'rgba(0, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          x: {
            beginAtZero: true,
          },
          y: {
            beginAtZero: true,
          },
        },
      },
    });
  }

  private renderProductChart(): void {
    this.destroyChart(this.productChart);

    const ctx = document.getElementById('productChart') as HTMLCanvasElement;
    if (!ctx) return;

    const labels = this.products.map(p => p.name);
    const data = labels.map(name => this.productQuantities[this.products.find(p => p.name === name)?.id || ''] || 0);

    this.productChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Units Sold',
            data: data,
            backgroundColor: 'rgba(255, 159, 64, 0.2)',
            borderColor: 'rgba(255, 159, 64, 1)',
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          x: {
            beginAtZero: true,
          },
          y: {
            beginAtZero: true,
          },
        },
      },
    });
  }

  private renderSalesByDayOfWeekChart(data: any[]): void {
    this.destroyChart(this.salesByDayOfWeekChart);

    const ctx = document.getElementById('salesByDayOfWeekChart') as HTMLCanvasElement;
    if (!ctx) return;

    const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const salesData = daysOfWeek.map(day => {
      const sales = data.find(d => d.dayOfWeek.trim() === day);
      return sales ? sales.totalSales : 0;
    });

    this.salesByDayOfWeekChart = new Chart(ctx, {
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

  private renderSalesByWeekChart(data: any[]): void {
    this.destroyChart(this.salesByWeekChart);

    const ctx = document.getElementById('salesByWeekChart') as HTMLCanvasElement;
    if (!ctx) return;

    const weeks = data.map(d => d.week);
    const salesData = data.map(d => d.totalSales);

    this.salesByWeekChart = new Chart(ctx, {
      type: 'line', // Line chart to show sales trend over weeks
      data: {
        labels: weeks,
        datasets: [
          {
            label: 'Weekly Sales Revenue',
            data: salesData,
            backgroundColor: 'rgba(153, 102, 255, 0.2)',
            borderColor: 'rgba(153, 102, 255, 1)',
            borderWidth: 1,
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
    startDate.setFullYear(startDate.getFullYear() - 1); // 1 year ago

    console.log('Start Date:', startDate.toISOString().split('T')[0]);
    console.log('End Date:', endDate.toISOString().split('T')[0]);

    return {
      startDate: startDate.toISOString().split('T')[0],
      endDate: endDate.toISOString().split('T')[0],
    };
  }

  toggleShowMore(): void {
    this.showMore = !this.showMore;
    this.updateVisibleProducts();
  }

  private updateVisibleProducts(): void {
    if (this.showMore) {
      this.visibleProducts = this.products;
    } else {
      this.visibleProducts = this.products.slice(0, 5);
    }
  }

  private destroyChart(chart: Chart | undefined): void {
    if (chart) {
      chart.destroy();
    }
  }

  toggleChart(chartType: 'dayOfWeek' | 'week' | 'month'): void {
    this.currentChart = chartType;
  }
}
