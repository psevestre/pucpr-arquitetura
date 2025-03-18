import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DeliveryItemDetailComponent } from './delivery-item-detail.component';

describe('DeliveryItem Management Detail Component', () => {
  let comp: DeliveryItemDetailComponent;
  let fixture: ComponentFixture<DeliveryItemDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeliveryItemDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./delivery-item-detail.component').then(m => m.DeliveryItemDetailComponent),
              resolve: { deliveryItem: () => of({ id: 16562 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DeliveryItemDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliveryItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deliveryItem on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DeliveryItemDetailComponent);

      // THEN
      expect(instance.deliveryItem()).toEqual(expect.objectContaining({ id: 16562 }));
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
