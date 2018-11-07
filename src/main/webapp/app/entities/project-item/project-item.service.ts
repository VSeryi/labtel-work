import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProjectItem } from 'app/shared/model/project-item.model';

type EntityResponseType = HttpResponse<IProjectItem>;
type EntityArrayResponseType = HttpResponse<IProjectItem[]>;

@Injectable({ providedIn: 'root' })
export class ProjectItemService {
    public resourceUrl = SERVER_API_URL + 'api/project-items';

    constructor(private http: HttpClient) {}

    create(projectItem: IProjectItem): Observable<EntityResponseType> {
        return this.http.post<IProjectItem>(this.resourceUrl, projectItem, { observe: 'response' });
    }

    update(projectItem: IProjectItem): Observable<EntityResponseType> {
        return this.http.put<IProjectItem>(this.resourceUrl, projectItem, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProjectItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProjectItem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
