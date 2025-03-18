import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStockItem } from '../stock-item.model';
import { StockItemService } from '../service/stock-item.service';
import { StockItemFormGroup, StockItemFormService } from './stock-item-form.service';

@Component({
  selector: 'jhi-stock-item-update',
  templateUrl: './stock-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StockItemUpdateComponent implements OnInit {
  isSaving = false;
  stockItem: IStockItem | null = null;

  protected stockItemService = inject(StockItemService);
  protected stockItemFormService = inject(StockItemFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StockItemFormGroup = this.stockItemFormService.createStockItemFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockItem }) => {
      this.stockItem = stockItem;
      if (stockItem) {
        this.updateForm(stockItem);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stockItem = this.stockItemFormService.getStockItem(this.editForm);
    if (stockItem.id !== null) {
      this.subscribeToSaveResponse(this.stockItemService.update(stockItem));
    } else {
      this.subscribeToSaveResponse(this.stockItemService.create(stockItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockItem>>): void {
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

  protected updateForm(stockItem: IStockItem): void {
    this.stockItem = stockItem;
    this.stockItemFormService.resetForm(this.editForm, stockItem);
  }
}
