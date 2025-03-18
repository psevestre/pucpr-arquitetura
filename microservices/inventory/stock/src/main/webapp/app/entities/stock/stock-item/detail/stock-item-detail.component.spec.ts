import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StockItemDetailComponent } from './stock-item-detail.component';

describe('StockItem Management Detail Component', () => {
  let comp: StockItemDetailComponent;
  let fixture: ComponentFixture<StockItemDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockItemDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./stock-item-detail.component').then(m => m.StockItemDetailComponent),
              resolve: { stockItem: () => of({ id: 23342 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StockItemDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load stockItem on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StockItemDetailComponent);

      // THEN
      expect(instance.stockItem()).toEqual(expect.objectContaining({ id: 23342 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
