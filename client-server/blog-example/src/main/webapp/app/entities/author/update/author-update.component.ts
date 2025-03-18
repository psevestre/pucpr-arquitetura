import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAuthor } from '../author.model';
import { AuthorService } from '../service/author.service';
import { AuthorFormGroup, AuthorFormService } from './author-form.service';

@Component({
  selector: 'jhi-author-update',
  templateUrl: './author-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AuthorUpdateComponent implements OnInit {
  isSaving = false;
  author: IAuthor | null = null;

  protected authorService = inject(AuthorService);
  protected authorFormService = inject(AuthorFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AuthorFormGroup = this.authorFormService.createAuthorFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ author }) => {
      this.author = author;
      if (author) {
        this.updateForm(author);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const author = this.authorFormService.getAuthor(this.editForm);
    if (author.id !== null) {
      this.subscribeToSaveResponse(this.authorService.update(author));
    } else {
      this.subscribeToSaveResponse(this.authorService.create(author));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuthor>>): void {
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

  protected updateForm(author: IAuthor): void {
    this.author = author;
    this.authorFormService.resetForm(this.editForm, author);
  }
}
