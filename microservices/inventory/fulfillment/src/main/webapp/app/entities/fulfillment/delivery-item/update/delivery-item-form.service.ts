import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDeliveryItem, NewDeliveryItem } from '../delivery-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeliveryItem for edit and NewDeliveryItemFormGroupInput for create.
 */
type DeliveryItemFormGroupInput = IDeliveryItem | PartialWithRequiredKeyOf<NewDeliveryItem>;

type DeliveryItemFormDefaults = Pick<NewDeliveryItem, 'id'>;

type DeliveryItemFormGroupContent = {
  id: FormControl<IDeliveryItem['id'] | NewDeliveryItem['id']>;
  sku: FormControl<IDeliveryItem['sku']>;
  description: FormControl<IDeliveryItem['description']>;
  quantity: FormControl<IDeliveryItem['quantity']>;
  weight: FormControl<IDeliveryItem['weight']>;
  volume: FormControl<IDeliveryItem['volume']>;
  delivery: FormControl<IDeliveryItem['delivery']>;
};

export type DeliveryItemFormGroup = FormGroup<DeliveryItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeliveryItemFormService {
  createDeliveryItemFormGroup(deliveryItem: DeliveryItemFormGroupInput = { id: null }): DeliveryItemFormGroup {
    const deliveryItemRawValue = {
      ...this.getFormDefaults(),
      ...deliveryItem,
    };
    return new FormGroup<DeliveryItemFormGroupContent>({
      id: new FormControl(
        { value: deliveryItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sku: new FormControl(deliveryItemRawValue.sku, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      description: new FormControl(deliveryItemRawValue.description),
      quantity: new FormControl(deliveryItemRawValue.quantity, {
        validators: [Validators.required, Validators.min(0)],
      }),
      weight: new FormControl(deliveryItemRawValue.weight, {
        validators: [Validators.required, Validators.min(0)],
      }),
      volume: new FormControl(deliveryItemRawValue.volume, {
        validators: [Validators.required, Validators.min(0)],
      }),
      delivery: new FormControl(deliveryItemRawValue.delivery),
    });
  }

  getDeliveryItem(form: DeliveryItemFormGroup): IDeliveryItem | NewDeliveryItem {
    return form.getRawValue() as IDeliveryItem | NewDeliveryItem;
  }

  resetForm(form: DeliveryItemFormGroup, deliveryItem: DeliveryItemFormGroupInput): void {
    const deliveryItemRawValue = { ...this.getFormDefaults(), ...deliveryItem };
    form.reset(
      {
        ...deliveryItemRawValue,
        id: { value: deliveryItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DeliveryItemFormDefaults {
    return {
      id: null,
    };
  }
}
