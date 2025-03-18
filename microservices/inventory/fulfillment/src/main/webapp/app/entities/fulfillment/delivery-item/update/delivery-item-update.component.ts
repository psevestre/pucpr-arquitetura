import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDelivery } from 'app/entities/fulfillment/delivery/delivery.model';
import { DeliveryService } from 'app/entities/fulfillment/delivery/service/delivery.service';
import { IDeliveryItem } from '../delivery-item.model';
import { DeliveryItemService } from '../service/delivery-item.service';
import { DeliveryItemFormGroup, DeliveryItemFormService } from './delivery-item-form.service';

@Component({
  selector: 'jhi-delivery-item-update',
  templateUrl: './delivery-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DeliveryItemUpdateComponent implements OnInit {
  isSaving = false;
  deliveryItem: IDeliveryItem | null = null;

  deliveriesSharedCollection: IDelivery[] = [];

  protected deliveryItemService = inject(DeliveryItemService);
  protected deliveryItemFormService = inject(DeliveryItemFormService);
  protected deliveryService = inject(DeliveryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DeliveryItemFormGroup = this.deliveryItemFormService.createDeliveryItemFormGroup();

  compareDelivery = (o1: IDelivery | null, o2: IDelivery | null): boolean => this.deliveryService.compareDelivery(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryItem }) => {
      this.deliveryItem = deliveryItem;
      if (deliveryItem) {
        this.updateForm(deliveryItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deliveryItem = this.deliveryItemFormService.getDeliveryItem(this.editForm);
    if (deliveryItem.id !== null) {
      this.subscribeToSaveResponse(this.deliveryItemService.update(deliveryItem));
    } else {
      this.subscribeToSaveResponse(this.deliveryItemService.create(deliveryItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliveryItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(deliveryItem: IDeliveryItem): void {
    this.deliveryItem = deliveryItem;
    this.deliveryItemFormService.resetForm(this.editForm, deliveryItem);

    this.deliveriesSharedCollection = this.deliveryService.addDeliveryToCollectionIfMissing<IDelivery>(
      this.deliveriesSharedCollection,
      deliveryItem.delivery,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deliveryService
      .query()
      .pipe(map((res: HttpResponse<IDelivery[]>) => res.body ?? []))
      .pipe(
        map((deliveries: IDelivery[]) =>
          this.deliveryService.addDeliveryToCollectionIfMissing<IDelivery>(deliveries, this.deliveryItem?.delivery),
        ),
      )
      .subscribe((deliveries: IDelivery[]) => (this.deliveriesSharedCollection = deliveries));
  }
}
