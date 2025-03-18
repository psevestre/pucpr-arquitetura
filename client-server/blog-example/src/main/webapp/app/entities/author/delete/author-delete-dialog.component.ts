import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAuthor } from '../author.model';
import { AuthorService } from '../service/author.service';

@Component({
  templateUrl: './author-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AuthorDeleteDialogComponent {
  author?: IAuthor;

  protected authorService = inject(AuthorService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.authorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
