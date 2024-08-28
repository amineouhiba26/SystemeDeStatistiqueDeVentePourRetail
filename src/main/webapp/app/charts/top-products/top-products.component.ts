import { Component, OnInit, Input } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { OrderItemService } from '../../entities/order-item/service/order-item.service';

@Component({
  selector: 'jhi-top-products',
  templateUrl: './top-products.component.html',
  styleUrls: ['./top-products.component.scss'],
})
export class TopProductsComponent implements OnInit {
  private topProductsChart: Chart | undefined;

  @Input() products: any[] = [];
  @Input() productQuantities: { [productId: string]: number } = {};

  constructor(private orderItemService: OrderItemService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.loadTopProducts();
  }

  private loadTopProducts(): void {
    this.orderItemService.getTop10MostSoldProducts().subscribe(data => {
      this.products = data;
      this.updateProductQuantities();
    });
  }

  private updateProductQuantities(): void {
    this.products.forEach(product => {
      this.orderItemService.getTotalQuantityByProductId(product.id).subscribe(quantity => {
        this.productQuantities[product.id] = quantity;
        this.renderTopProductsChart();
      });
    });
  }

  private renderTopProductsChart(): void {
    this.destroyChart(this.topProductsChart);

    const ctx = document.getElementById('topProductsChart') as HTMLCanvasElement;
    if (!ctx) return;

    const labels = this.products.map(p => p.name);
    const data = labels.map(name => this.productQuantities[this.products.find(p => p.name === name)?.id || ''] || 0);

    this.topProductsChart = new Chart(ctx, {
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

  private destroyChart(chart: Chart | undefined): void {
    if (chart) {
      chart.destroy();
    }
  }
}
