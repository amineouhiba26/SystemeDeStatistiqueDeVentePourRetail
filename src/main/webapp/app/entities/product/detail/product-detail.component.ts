import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { IProduct } from '../product.model';
import { PriceHistoryService } from '../../price-history/service/price-history.service';
import { ProductCancellationsService } from '../../product-cancellations/service/product-cancellations.service'; // Import the service

@Component({
  selector: 'jhi-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent implements OnInit, OnDestroy {
  product: IProduct | null = null;
  priceChart: Chart | undefined;
  cancellationChart: Chart | undefined; // Add property for cancellation chart

  constructor(
    private activatedRoute: ActivatedRoute,
    private priceHistoryService: PriceHistoryService,
    private productCancellationsService: ProductCancellationsService // Inject the service
  ) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (this.product) {
        console.log('Product selected:', this.product);
        this.loadPriceChart(this.product.id);
        this.loadCancellationChart(this.product.id); // Load cancellation chart
      }
    });
  }

  ngOnDestroy(): void {
    if (this.priceChart) {
      this.priceChart.destroy();
    }
    if (this.cancellationChart) {
      // Destroy cancellation chart if exists
      this.cancellationChart.destroy();
    }
  }

  previousState(): void {
    window.history.back();
  }

  private loadPriceChart(productId: string): void {
    console.log('Fetching price history data for product ID:', productId);

    this.priceHistoryService.getPriceHistoriesByProductId(productId).subscribe(data => {
      console.log('Fetched price history data:', data);

      // Destroy previous chart if it exists
      if (this.priceChart) {
        this.priceChart.destroy();
      }

      const labels = data.map(d => new Date(d.createdDate).toLocaleDateString());
      const oldPrices = data.map(d => d.oldPrice ?? 0);
      const newPrices = data.map(d => d.newPrice ?? 0);

      console.log('Price history data:', data);
      console.log('Labels:', labels);
      console.log('Old Prices:', oldPrices);
      console.log('New Prices:', newPrices);

      // Ensure canvas element has the correct ID
      const ctx = document.getElementById(`productPriceVariationChart-${productId}`) as HTMLCanvasElement;
      if (ctx) {
        this.priceChart = new Chart(ctx, {
          type: 'bar', // Change type to 'bar'
          data: {
            labels: labels,
            datasets: [
              {
                label: 'Old Price',
                data: oldPrices,
                backgroundColor: 'rgba(255, 0, 0, 0.5)',
                borderColor: 'red',
                borderWidth: 1,
              },
              {
                label: 'New Price',
                data: newPrices,
                backgroundColor: 'rgba(0, 0, 255, 0.5)',
                borderColor: 'blue',
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
                  text: 'Date',
                },
                ticks: {
                  autoSkip: true,
                  maxTicksLimit: 20,
                },
              },
              y: {
                title: {
                  display: true,
                  text: 'Price',
                },
                beginAtZero: true,
              },
            },
          },
        });
      } else {
        console.error('Canvas element not found');
      }
    });
  }

  private loadCancellationChart(productId: string): void {
    console.log('Fetching product cancellations data for product ID:', productId);

    this.productCancellationsService.getProductCancellationsByProductId(productId).subscribe(data => {
      console.log('Fetched product cancellations data:', data);

      // Destroy previous chart if it exists
      if (this.cancellationChart) {
        this.cancellationChart.destroy();
      }

      const reasons = data.reduce((acc: { [key: string]: number }, cancellation) => {
        const reason = cancellation.reason || 'Unknown';
        acc[reason] = (acc[reason] || 0) + 1;
        return acc;
      }, {});

      const labels = Object.keys(reasons);
      const values = Object.values(reasons);

      console.log('Cancellation reasons:', reasons);
      console.log('Labels:', labels);
      console.log('Values:', values);

      // Ensure canvas element has the correct ID
      const ctx = document.getElementById(`productCancellationChart-${productId}`) as HTMLCanvasElement;
      if (ctx) {
        this.cancellationChart = new Chart(ctx, {
          type: 'doughnut', // Change type to 'doughnut'
          data: {
            labels: labels,
            datasets: [
              {
                data: values,
                backgroundColor: [
                  'rgba(255, 99, 132, 0.2)',
                  'rgba(54, 162, 235, 0.2)',
                  'rgba(255, 206, 86, 0.2)',
                  'rgba(75, 192, 192, 0.2)',
                  'rgba(153, 102, 255, 0.2)',
                  'rgba(255, 159, 64, 0.2)',
                ],
                borderColor: [
                  'rgba(255, 99, 132, 1)',
                  'rgba(54, 162, 235, 1)',
                  'rgba(255, 206, 86, 1)',
                  'rgba(75, 192, 192, 1)',
                  'rgba(153, 102, 255, 1)',
                  'rgba(255, 159, 64, 1)',
                ],
                borderWidth: 1,
              },
            ],
          },
          options: {
            responsive: true,
            plugins: {
              legend: {
                position: 'top',
              },
              tooltip: {
                callbacks: {
                  label: function (context) {
                    let label = context.label || '';
                    if (label) {
                      label += ': ';
                    }
                    if (context.parsed > 0) {
                      label += context.parsed + ' cancellations';
                    }
                    return label;
                  },
                },
              },
            },
          },
        });
      } else {
        console.error('Canvas element not found');
      }
    });
  }
}
