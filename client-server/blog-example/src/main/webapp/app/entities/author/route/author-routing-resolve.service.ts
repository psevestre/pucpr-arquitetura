import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAuthor } from '../author.model';
import { AuthorService } from '../service/author.service';

const authorResolve = (route: ActivatedRouteSnapshot): Observable<null | IAuthor> => {
  const id = route.params.id;
  if (id) {
    return inject(AuthorService)
      .find(id)
      .pipe(
        mergeMap((author: HttpResponse<IAuthor>) => {
          if (author.body) {
            return of(author.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default authorResolve;
