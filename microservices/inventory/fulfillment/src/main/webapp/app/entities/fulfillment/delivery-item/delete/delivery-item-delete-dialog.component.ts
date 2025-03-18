import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDeliveryItem } from '../delivery-item.model';
import { DeliveryItemService } from '../service/delivery-item.service';

@Component({
  templateUrl: './delivery-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DeliveryItemDeleteDialogComponent {
  deliveryItem?: IDeliveryItem;

  protected deliveryItemService = inject(DeliveryItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliveryItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
