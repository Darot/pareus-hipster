import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { EstateAgency } from './estate-agency.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EstateAgencyService {

    private resourceUrl =  SERVER_API_URL + 'api/estate-agencies';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/estate-agencies';

    constructor(private http: Http) { }

    create(estateAgency: EstateAgency): Observable<EstateAgency> {
        const copy = this.convert(estateAgency);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(estateAgency: EstateAgency): Observable<EstateAgency> {
        const copy = this.convert(estateAgency);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<EstateAgency> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to EstateAgency.
     */
    private convertItemFromServer(json: any): EstateAgency {
        const entity: EstateAgency = Object.assign(new EstateAgency(), json);
        return entity;
    }

    /**
     * Convert a EstateAgency to a JSON which can be sent to the server.
     */
    private convert(estateAgency: EstateAgency): EstateAgency {
        const copy: EstateAgency = Object.assign({}, estateAgency);
        return copy;
    }
}
