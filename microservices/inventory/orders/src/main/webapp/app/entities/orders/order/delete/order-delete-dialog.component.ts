import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrder } from '../order.model';
import { OrderService } from '../service/order.service';

@Component({
  templateUrl: './order-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrderDeleteDialogComponent {
  order?: IOrder;

  protected orderService = inject(OrderService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
