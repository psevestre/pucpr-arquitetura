import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAuthor } from '../author.model';

@Component({
  selector: 'jhi-author-detail',
  templateUrl: './author-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AuthorDetailComponent {
  author = input<IAuthor | null>(null);

  previousState(): void {
    window.history.back();
  }
}
