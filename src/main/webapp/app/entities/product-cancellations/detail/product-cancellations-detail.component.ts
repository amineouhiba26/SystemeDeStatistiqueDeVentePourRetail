import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductCancellations } from '../product-cancellations.model';

@Component({
  selector: 'jhi-product-cancellations-detail',
  templateUrl: './product-cancellations-detail.component.html',
})
export class ProductCancellationsDetailComponent implements OnInit {
  productCancellations: IProductCancellations | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productCancellations }) => {
      this.productCancellations = productCancellations;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
