import { Component } from '@angular/core';
import { Subscription } from 'rxjs/internal/Subscription';
import { Character } from '../models/character';
import { Comment } from '../models/comment';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MarvelCharService } from '../services/marvel-char.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent {
  charId =  "";
  sub$! :  Subscription;
  character! : Character;
  comments!: Comment[];

  constructor(private activatedRoute: ActivatedRoute, 
    private marvelCharSvc: MarvelCharService, private router: Router){

  }

  ngOnInit(): void {
    this.sub$ = this.activatedRoute.params.subscribe(
       async (params) => {
        this.charId = params['charId']
        const details = await this.marvelCharSvc.getCharacterDetails(this.charId)
        this.character = details.details
        this.comments = await this.marvelCharSvc.getCharComments(this.charId)
        console.log(this.comments)
      }
    );

  }

  addComent(){
    const queryParams: Params = { charParam: this.character['name'] + '|' + this.character.id };
    this.router.navigate(['/comment'], {queryParams : queryParams})
  }

  ngOnDestroy(): void{
    this.sub$.unsubscribe();
  }
}
