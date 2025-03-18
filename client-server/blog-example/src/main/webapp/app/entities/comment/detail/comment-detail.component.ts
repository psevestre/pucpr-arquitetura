import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IComment } from '../comment.model';

@Component({
  selector: 'jhi-comment-detail',
  templateUrl: './comment-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class CommentDetailComponent {
  comment = input<IComment | null>(null);

  previousState(): void {
    window.history.back();
  }
}
