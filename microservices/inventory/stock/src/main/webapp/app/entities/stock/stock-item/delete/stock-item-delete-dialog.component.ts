import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStockItem } from '../stock-item.model';
import { StockItemService } from '../service/stock-item.service';

@Component({
  templateUrl: './stock-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StockItemDeleteDialogComponent {
  stockItem?: IStockItem;

  protected stockItemService = inject(StockItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stockItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
