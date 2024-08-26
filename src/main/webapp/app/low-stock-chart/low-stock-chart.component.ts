import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { ProductService } from '../entities/product/service/product.service';
import { IProduct } from '../entities/product/product.model';

@Component({
  selector: 'jhi-app-low-stock-chart',
  templateUrl: './low-stock-chart.component.html',
  styleUrls: ['./low-stock-chart.component.scss'],
})
export class LowStockChartComponent implements OnInit {
  threshold = 10; // Define a default threshold for low stock

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadLowStockProducts();
  }

  loadLowStockProducts(): void {
    this.productService.getLowStockProducts(this.threshold).subscribe(response => {
      const products = response.body || [];

      // Filter out products with invalid or missing names
      const validProducts: IProduct[] = products.filter(product => product.name && product.capacity);

      const productNames = validProducts.map(product => product.name as string); // Safe cast to string
      const productCapacities = validProducts.map(product => {
        const capacity = product.capacity ?? '0'; // Default to '0' if capacity is null/undefined
        return parseInt(capacity, 10); // Parse capacity to an integer
      });

      this.renderChart(productNames, productCapacities);
    });
  }

  renderChart(labels: string[], data: number[]): void {
    const ctx = document.getElementById('lowStockChart') as HTMLCanvasElement;
    new Chart(ctx, {
      type: 'bar',
      data: {
        labels,
        datasets: [
          {
            label: 'Stock Level',
            data,
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });
  }
}
