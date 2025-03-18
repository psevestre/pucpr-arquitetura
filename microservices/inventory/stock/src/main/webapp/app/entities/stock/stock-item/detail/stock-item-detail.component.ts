import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IStockItem } from '../stock-item.model';

@Component({
  selector: 'jhi-stock-item-detail',
  templateUrl: './stock-item-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class StockItemDetailComponent {
  stockItem = input<IStockItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
