import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDelivery, NewDelivery } from '../delivery.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDelivery for edit and NewDeliveryFormGroupInput for create.
 */
type DeliveryFormGroupInput = IDelivery | PartialWithRequiredKeyOf<NewDelivery>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDelivery | NewDelivery> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type DeliveryFormRawValue = FormValueOf<IDelivery>;

type NewDeliveryFormRawValue = FormValueOf<NewDelivery>;

type DeliveryFormDefaults = Pick<NewDelivery, 'id' | 'createdAt' | 'updatedAt'>;

type DeliveryFormGroupContent = {
  id: FormControl<DeliveryFormRawValue['id'] | NewDelivery['id']>;
  orderId: FormControl<DeliveryFormRawValue['orderId']>;
  status: FormControl<DeliveryFormRawValue['status']>;
  customerId: FormControl<DeliveryFormRawValue['customerId']>;
  name: FormControl<DeliveryFormRawValue['name']>;
  email: FormControl<DeliveryFormRawValue['email']>;
  addressLine1: FormControl<DeliveryFormRawValue['addressLine1']>;
  addressLine2: FormControl<DeliveryFormRawValue['addressLine2']>;
  zipCode: FormControl<DeliveryFormRawValue['zipCode']>;
  city: FormControl<DeliveryFormRawValue['city']>;
  country: FormControl<DeliveryFormRawValue['country']>;
  deliveryInstructions: FormControl<DeliveryFormRawValue['deliveryInstructions']>;
  createdAt: FormControl<DeliveryFormRawValue['createdAt']>;
  updatedAt: FormControl<DeliveryFormRawValue['updatedAt']>;
};

export type DeliveryFormGroup = FormGroup<DeliveryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeliveryFormService {
  createDeliveryFormGroup(delivery: DeliveryFormGroupInput = { id: null }): DeliveryFormGroup {
    const deliveryRawValue = this.convertDeliveryToDeliveryRawValue({
      ...this.getFormDefaults(),
      ...delivery,
    });
    return new FormGroup<DeliveryFormGroupContent>({
      id: new FormControl(
        { value: deliveryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      orderId: new FormControl(deliveryRawValue.orderId, {
        validators: [Validators.required],
      }),
      status: new FormControl(deliveryRawValue.status, {
        validators: [Validators.required],
      }),
      customerId: new FormControl(deliveryRawValue.customerId, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      name: new FormControl(deliveryRawValue.name, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      email: new FormControl(deliveryRawValue.email, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      addressLine1: new FormControl(deliveryRawValue.addressLine1, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      addressLine2: new FormControl(deliveryRawValue.addressLine2, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      zipCode: new FormControl(deliveryRawValue.zipCode, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      city: new FormControl(deliveryRawValue.city, {
        validators: [Validators.maxLength(128)],
      }),
      country: new FormControl(deliveryRawValue.country, {
        validators: [Validators.maxLength(64)],
      }),
      deliveryInstructions: new FormControl(deliveryRawValue.deliveryInstructions, {
        validators: [Validators.maxLength(512)],
      }),
      createdAt: new FormControl(deliveryRawValue.createdAt, {
        validators: [Validators.required],
      }),
      updatedAt: new FormControl(deliveryRawValue.updatedAt, {
        validators: [Validators.required],
      }),
    });
  }

  getDelivery(form: DeliveryFormGroup): IDelivery | NewDelivery {
    return this.convertDeliveryRawValueToDelivery(form.getRawValue() as DeliveryFormRawValue | NewDeliveryFormRawValue);
  }

  resetForm(form: DeliveryFormGroup, delivery: DeliveryFormGroupInput): void {
    const deliveryRawValue = this.convertDeliveryToDeliveryRawValue({ ...this.getFormDefaults(), ...delivery });
    form.reset(
      {
        ...deliveryRawValue,
        id: { value: deliveryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DeliveryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertDeliveryRawValueToDelivery(rawDelivery: DeliveryFormRawValue | NewDeliveryFormRawValue): IDelivery | NewDelivery {
    return {
      ...rawDelivery,
      createdAt: dayjs(rawDelivery.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawDelivery.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertDeliveryToDeliveryRawValue(
    delivery: IDelivery | (Partial<NewDelivery> & DeliveryFormDefaults),
  ): DeliveryFormRawValue | PartialWithRequiredKeyOf<NewDeliveryFormRawValue> {
    return {
      ...delivery,
      createdAt: delivery.createdAt ? delivery.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: delivery.updatedAt ? delivery.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
