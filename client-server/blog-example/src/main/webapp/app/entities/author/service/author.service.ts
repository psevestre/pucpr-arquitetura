import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAuthor, NewAuthor } from '../author.model';

export type PartialUpdateAuthor = Partial<IAuthor> & Pick<IAuthor, 'id'>;

export type EntityResponseType = HttpResponse<IAuthor>;
export type EntityArrayResponseType = HttpResponse<IAuthor[]>;

@Injectable({ providedIn: 'root' })
export class AuthorService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/authors');

  create(author: NewAuthor): Observable<EntityResponseType> {
    return this.http.post<IAuthor>(this.resourceUrl, author, { observe: 'response' });
  }

  update(author: IAuthor): Observable<EntityResponseType> {
    return this.http.put<IAuthor>(`${this.resourceUrl}/${this.getAuthorIdentifier(author)}`, author, { observe: 'response' });
  }

  partialUpdate(author: PartialUpdateAuthor): Observable<EntityResponseType> {
    return this.http.patch<IAuthor>(`${this.resourceUrl}/${this.getAuthorIdentifier(author)}`, author, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAuthor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAuthor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAuthorIdentifier(author: Pick<IAuthor, 'id'>): number {
    return author.id;
  }

  compareAuthor(o1: Pick<IAuthor, 'id'> | null, o2: Pick<IAuthor, 'id'> | null): boolean {
    return o1 && o2 ? this.getAuthorIdentifier(o1) === this.getAuthorIdentifier(o2) : o1 === o2;
  }

  addAuthorToCollectionIfMissing<Type extends Pick<IAuthor, 'id'>>(
    authorCollection: Type[],
    ...authorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const authors: Type[] = authorsToCheck.filter(isPresent);
    if (authors.length > 0) {
      const authorCollectionIdentifiers = authorCollection.map(authorItem => this.getAuthorIdentifier(authorItem));
      const authorsToAdd = authors.filter(authorItem => {
        const authorIdentifier = this.getAuthorIdentifier(authorItem);
        if (authorCollectionIdentifiers.includes(authorIdentifier)) {
          return false;
        }
        authorCollectionIdentifiers.push(authorIdentifier);
        return true;
      });
      return [...authorsToAdd, ...authorCollection];
    }
    return authorCollection;
  }
}
