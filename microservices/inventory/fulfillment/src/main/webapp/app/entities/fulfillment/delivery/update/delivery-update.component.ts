import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { DeliveryStatus } from 'app/entities/enumerations/delivery-status.model';
import { IDelivery } from '../delivery.model';
import { DeliveryService } from '../service/delivery.service';
import { DeliveryFormGroup, DeliveryFormService } from './delivery-form.service';

@Component({
  selector: 'jhi-delivery-update',
  templateUrl: './delivery-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DeliveryUpdateComponent implements OnInit {
  isSaving = false;
  delivery: IDelivery | null = null;
  deliveryStatusValues = Object.keys(DeliveryStatus);

  protected deliveryService = inject(DeliveryService);
  protected deliveryFormService = inject(DeliveryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DeliveryFormGroup = this.deliveryFormService.createDeliveryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ delivery }) => {
      this.delivery = delivery;
      if (delivery) {
        this.updateForm(delivery);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const delivery = this.deliveryFormService.getDelivery(this.editForm);
    if (delivery.id !== null) {
      this.subscribeToSaveResponse(this.deliveryService.update(delivery));
    } else {
      this.subscribeToSaveResponse(this.deliveryService.create(delivery));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDelivery>>): void {
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

  protected updateForm(delivery: IDelivery): void {
    this.delivery = delivery;
    this.deliveryFormService.resetForm(this.editForm, delivery);
  }
}
