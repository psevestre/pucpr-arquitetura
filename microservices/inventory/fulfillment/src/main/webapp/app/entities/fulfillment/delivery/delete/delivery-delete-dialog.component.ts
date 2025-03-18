import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDelivery } from '../delivery.model';
import { DeliveryService } from '../service/delivery.service';

@Component({
  templateUrl: './delivery-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DeliveryDeleteDialogComponent {
  delivery?: IDelivery;

  protected deliveryService = inject(DeliveryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliveryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
