import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { PriceHistoryService } from '../entities/price-history/service/price-history.service';

@Component({
  selector: 'jhi-product-price-variation-chart',
  templateUrl: './product-price-variation-chart.component.html',
  styleUrls: ['./product-price-variation-chart.component.scss'],
})
export class ProductPriceVariationChartComponent implements OnInit {
  productId!: string; // Product ID will be fetched from route parameters

  constructor(private priceHistoryService: PriceHistoryService, private route: ActivatedRoute) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.productId = params.get('id')!;
      this.loadChart();
    });
  }

  private loadChart(): void {
    this.priceHistoryService.getPriceHistoriesByProductId(this.productId).subscribe(data => {
      const labels = data.map(d => new Date(d.createdDate).toLocaleDateString());
      const oldPrices = data.map(d => d.oldPrice ?? 0);
      const newPrices = data.map(d => d.newPrice ?? 0);

      new Chart('productPriceVariationChart', {
        type: 'line',
        data: {
          labels: labels,
          datasets: [
            {
              label: 'Old Price',
              data: oldPrices,
              borderColor: 'red',
              backgroundColor: 'rgba(255, 0, 0, 0.2)',
              fill: false,
            },
            {
              label: 'New Price',
              data: newPrices,
              borderColor: 'blue',
              backgroundColor: 'rgba(0, 0, 255, 0.2)',
              fill: false,
            },
          ],
        },
      });
    });
  }
}
