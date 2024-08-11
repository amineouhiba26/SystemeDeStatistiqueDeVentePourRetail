import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { IProduct } from '../product.model';
import { PriceHistoryService } from '../../price-history/service/price-history.service';

@Component({
  selector: 'jhi-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent implements OnInit, OnDestroy {
  product: IProduct | null = null;
  priceChart: Chart | undefined;

  constructor(private activatedRoute: ActivatedRoute, private priceHistoryService: PriceHistoryService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (this.product) {
        console.log('Product selected:', this.product);
        this.loadPriceChart(this.product.id);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.priceChart) {
      this.priceChart.destroy();
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
}
