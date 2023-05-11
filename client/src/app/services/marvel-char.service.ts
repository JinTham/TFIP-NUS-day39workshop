import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs/internal/lastValueFrom';
import { Character } from '../models/character';
import { Comment } from '../models/comment';

@Injectable({
  providedIn: 'root'
})
export class MarvelCharService {

  API_URL:string = "/api/characters"
  headers = new HttpHeaders().set("Content-Type","application/json; charset=utf-8")

  constructor(private httpClient:HttpClient) { }

  getCharacters(charName:string, offset:number, limit:number) : Promise<any> {
    const headers = new HttpHeaders().set("Content-Type","application/json; charset=utf-8")
    const params = new HttpParams()
                    .set("charName", charName)
                    .set("limit", limit)
                    .set("offset", offset)
    return lastValueFrom(this.httpClient.get<Character[]>(this.API_URL,{params:params, headers:this.headers}))
  }

  getCharacterDetails(charId:string): Promise<any>{
    return lastValueFrom(this.httpClient.get<Character>(this.API_URL+"/"+charId,{headers:this.headers}))
  }

  getCharComments(charId:string):Promise<any> {
    return lastValueFrom(this.httpClient.get<Comment[]>(this.API_URL+"/comments/"+charId,{headers:this.headers}))
  }

  saveComment(c: Comment):Promise<any> {
    const body = JSON.stringify(c)
    return lastValueFrom(this.httpClient.post<Comment>(this.API_URL+"/"+c.id, body, {headers:this.headers}))
  }

}
