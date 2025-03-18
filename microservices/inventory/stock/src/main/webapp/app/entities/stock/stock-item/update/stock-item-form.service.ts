import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStockItem, NewStockItem } from '../stock-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStockItem for edit and NewStockItemFormGroupInput for create.
 */
type StockItemFormGroupInput = IStockItem | PartialWithRequiredKeyOf<NewStockItem>;

type StockItemFormDefaults = Pick<NewStockItem, 'id'>;

type StockItemFormGroupContent = {
  id: FormControl<IStockItem['id'] | NewStockItem['id']>;
  sku: FormControl<IStockItem['sku']>;
  description: FormControl<IStockItem['description']>;
  available: FormControl<IStockItem['available']>;
  reserved: FormControl<IStockItem['reserved']>;
  minStock: FormControl<IStockItem['minStock']>;
};

export type StockItemFormGroup = FormGroup<StockItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StockItemFormService {
  createStockItemFormGroup(stockItem: StockItemFormGroupInput = { id: null }): StockItemFormGroup {
    const stockItemRawValue = {
      ...this.getFormDefaults(),
      ...stockItem,
    };
    return new FormGroup<StockItemFormGroupContent>({
      id: new FormControl(
        { value: stockItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sku: new FormControl(stockItemRawValue.sku, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      description: new FormControl(stockItemRawValue.description, {
        validators: [Validators.required, Validators.maxLength(256)],
      }),
      available: new FormControl(stockItemRawValue.available, {
        validators: [Validators.required],
      }),
      reserved: new FormControl(stockItemRawValue.reserved, {
        validators: [Validators.required, Validators.min(0)],
      }),
      minStock: new FormControl(stockItemRawValue.minStock, {
        validators: [Validators.required, Validators.min(0)],
      }),
    });
  }

  getStockItem(form: StockItemFormGroup): IStockItem | NewStockItem {
    return form.getRawValue() as IStockItem | NewStockItem;
  }

  resetForm(form: StockItemFormGroup, stockItem: StockItemFormGroupInput): void {
    const stockItemRawValue = { ...this.getFormDefaults(), ...stockItem };
    form.reset(
      {
        ...stockItemRawValue,
        id: { value: stockItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StockItemFormDefaults {
    return {
      id: null,
    };
  }
}
