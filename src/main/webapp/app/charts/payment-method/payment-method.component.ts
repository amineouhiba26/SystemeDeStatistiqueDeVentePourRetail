import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { OrderService } from '../../entities/order/service/order.service';

@Component({
  selector: 'jhi-payment-method',
  templateUrl: './payment-method.component.html',
  styleUrls: ['./payment-method.component.scss'],
})
export class PaymentMethodComponent implements OnInit {
  chart: Chart | undefined;
  paymentMethods = ['Cash', 'Cheque', 'Credit Card', 'Voucher', 'Click to Pay']; // Example payment methods

  constructor(private orderService: OrderService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.loadPaymentMethodChart();
  }

  private loadPaymentMethodChart(): void {
    const paymentMethodCounts: { [key: string]: number } = {};

    this.paymentMethods.forEach(method => {
      this.orderService.getOrdersByPaymentMethod(method).subscribe(response => {
        paymentMethodCounts[method] = response.body?.length || 0;
        if (Object.keys(paymentMethodCounts).length === this.paymentMethods.length) {
          this.createChart(paymentMethodCounts);
        }
      });
    });
  }

  private createChart(paymentMethodCounts: { [key: string]: number }): void {
    const ctx = document.getElementById('paymentChart') as HTMLCanvasElement;

    if (!ctx) return;

    this.chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: Object.keys(paymentMethodCounts),
        datasets: [
          {
            label: 'Number of Orders',
            data: Object.values(paymentMethodCounts),
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)', // Soft Pink
              'rgba(255, 159, 64, 0.2)', // Soft Orange
              'rgba(255, 205, 86, 0.2)', // Soft Yellow
              'rgba(75, 192, 192, 0.2)', // Soft Teal
              'rgba(153, 102, 255, 0.2)', // Soft Purple
            ],
            borderColor: [
              'rgba(255, 99, 132, 1)', // Darker Pink
              'rgba(255, 159, 64, 1)', // Darker Orange
              'rgba(255, 205, 86, 1)', // Darker Yellow
              'rgba(75, 192, 192, 1)', // Darker Teal
              'rgba(153, 102, 255, 1)', // Darker Purple
            ],
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
        },
      },
    });
  }
}
