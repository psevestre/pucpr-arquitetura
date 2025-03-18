import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { StockItemService } from '../service/stock-item.service';
import { IStockItem } from '../stock-item.model';
import { StockItemFormService } from './stock-item-form.service';

import { StockItemUpdateComponent } from './stock-item-update.component';

describe('StockItem Management Update Component', () => {
  let comp: StockItemUpdateComponent;
  let fixture: ComponentFixture<StockItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stockItemFormService: StockItemFormService;
  let stockItemService: StockItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StockItemUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(StockItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StockItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stockItemFormService = TestBed.inject(StockItemFormService);
    stockItemService = TestBed.inject(StockItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const stockItem: IStockItem = { id: 21429 };

      activatedRoute.data = of({ stockItem });
      comp.ngOnInit();

      expect(comp.stockItem).toEqual(stockItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockItem>>();
      const stockItem = { id: 23342 };
      jest.spyOn(stockItemFormService, 'getStockItem').mockReturnValue(stockItem);
      jest.spyOn(stockItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockItem }));
      saveSubject.complete();

      // THEN
      expect(stockItemFormService.getStockItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stockItemService.update).toHaveBeenCalledWith(expect.objectContaining(stockItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockItem>>();
      const stockItem = { id: 23342 };
      jest.spyOn(stockItemFormService, 'getStockItem').mockReturnValue({ id: null });
      jest.spyOn(stockItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockItem }));
      saveSubject.complete();

      // THEN
      expect(stockItemFormService.getStockItem).toHaveBeenCalled();
      expect(stockItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockItem>>();
      const stockItem = { id: 23342 };
      jest.spyOn(stockItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stockItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
