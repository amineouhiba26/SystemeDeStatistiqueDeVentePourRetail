import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { PriceHistoryService } from '../price-history/service/price-history.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-useapp-my-chart',
  templateUrl: './my-chart.component.html',
  styleUrls: ['./my-chart.component.scss'],
})
export class PriceHistoryChartComponent implements OnInit {
  private chart: Chart<'line', number[], unknown> | undefined;

  constructor(private route: ActivatedRoute, private priceHistoryService: PriceHistoryService) {}

  ngOnInit(): void {
    Chart.register(...registerables); // Register chart components
    this.route.paramMap.subscribe(params => {
      const productId = params.get('productId');
      if (productId) {
        this.loadPriceHistories(productId);
      }
    });
  }

  loadPriceHistories(productId: string): void {
    this.priceHistoryService.getPriceHistoriesByProductId(productId).subscribe(data => {
      const labels = data.map(d => new Date(d.createdDate).toLocaleDateString());
      const oldPrices = data.map(d => d.oldPrice ?? 0);
      const newPrices = data.map(d => d.newPrice ?? 0);

      this.createChart(labels, oldPrices, newPrices);
    });
  }

  createChart(labels: string[], oldPrices: number[], newPrices: number[]): void {
    if (this.chart) {
      this.chart.destroy();
    }

    this.chart = new Chart('priceHistoryChart', {
      type: 'line',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Old Price',
            data: oldPrices,
            borderColor: 'rgba(255, 99, 132, 1)',
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            fill: false,
          },
          {
            label: 'New Price',
            data: newPrices,
            borderColor: 'rgba(54, 162, 235, 1)',
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            fill: false,
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
          },
          y: {
            title: {
              display: true,
              text: 'Price',
            },
            suggestedMin: 0,
          },
        },
      },
    });
  }
}
