import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Material, Semana, MaterialUpload } from '../models/material.model';

@Injectable({
    providedIn: 'root'
})
export class MaterialService {
    private baseUrl = `${environment.apiUrl}/materiais`;

    constructor(private http: HttpClient) { }

    getSemanasBySala(salaId: string): Observable<Semana[]> {
        return this.http.get<Semana[]>(`${this.baseUrl}/semanas?salaId=${salaId}`);
    }

    getMateriaisBySemana(semanaId: string): Observable<Material[]> {
        return this.http.get<Material[]>(`${this.baseUrl}/materiais?semanaId=${semanaId}`);
    }

    criarSemana(semana: Semana): Observable<Semana> {
        return this.http.post<Semana>(`${this.baseUrl}/semanas`, semana);
    }

    criarMaterial(material: Material): Observable<Material> {
        return this.http.post<Material>(`${this.baseUrl}/materiais`, material);
    }

    uploadMaterial(uploadData: MaterialUpload): Observable<Material> {
        const formData = new FormData();
        formData.append('arquivo', uploadData.arquivo);
        formData.append('titulo', uploadData.titulo);
        formData.append('descricao', uploadData.descricao || '');
        formData.append('semanaId', uploadData.semanaId);
        formData.append('salaId', uploadData.salaId);

        return this.http.post<Material>(`${this.baseUrl}/upload`, formData);
    }

    atualizarMaterial(material: Material): Observable<Material> {
        return this.http.put<Material>(`${this.baseUrl}/materiais/${material.id}`, material);
    }

    excluirMaterial(materialId: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/materiais/${materialId}`);
    }

    excluirSemana(semanaId: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/semanas/${semanaId}`);
    }
}