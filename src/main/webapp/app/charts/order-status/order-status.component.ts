import { Component, OnInit, OnDestroy } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { Subscription } from 'rxjs';
import { OrderService } from '../../entities/order/service/order.service';

@Component({
  selector: 'jhi-order-status',
  templateUrl: './order-status.component.html',
  styleUrls: ['./order-status.component.scss'],
})
export class OrderStatusComponent implements OnInit, OnDestroy {
  orderStatuses: { [status: string]: number } = {};
  private orderStatusChart: Chart | undefined;
  private subscriptions: Subscription[] = [];

  constructor(private orderService: OrderService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.loadOrderStatuses();
  }

  ngOnDestroy(): void {
    this.destroyChart();
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  private loadOrderStatuses(): void {
    const subscription = this.orderService.getOrderStatuses().subscribe(response => {
      const statuses = response.body || [];
      this.orderStatuses = statuses.reduce((acc: { [key: string]: number }, status: string) => {
        acc[status] = (acc[status] || 0) + 1;
        return acc;
      }, {});

      this.renderStatusChart();
    });

    this.subscriptions.push(subscription);
  }

  private renderStatusChart(): void {
    this.destroyChart();

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

  private destroyChart(): void {
    if (this.orderStatusChart) {
      this.orderStatusChart.destroy();
    }
  }
}
