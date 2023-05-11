import { Component, OnDestroy, OnInit } from '@angular/core';
import { Character } from '../models/character';
import { MarvelCharService } from '../services/marvel-char.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {

  charName:string = ""
  characters!:Character[]
  sub$!:Subscription
  currentIndex!: number;
  pageNumber!: number;
  disablePrevious : boolean = false;
  constructor(private marvelCharSvc:MarvelCharService, private actRoute:ActivatedRoute, private router:Router) {}

  ngOnInit(): void {
      this.sub$ = this.actRoute.params.subscribe(
        async (params) => {
          this.charName = params['charName'];
          this.pageNumber = 1;
          this.disablePrevious = true;
          const result = await this.marvelCharSvc.getCharacters(this.charName, 0, 10)
          this.currentIndex = 1
          if (result === undefined || result.length<=0) {
            this.router.navigate(['/'])
          } else {
            this.characters = result
          }
        }
      )
  }

  ngOnDestroy(): void {
      this.sub$.unsubscribe()
  }

  async previous() {
    if (this.currentIndex > 0) {
      this.currentIndex = this.currentIndex - 10
      this.pageNumber--
      const result = await this.marvelCharSvc.getCharacters(this.charName, this.currentIndex, 10)
      this.characters = result
      if(this.pageNumber == 1)
        this.disablePrevious = true;
    }
  }

  async next() {
    this.currentIndex = this.currentIndex + 10
    const result = await this.marvelCharSvc.getCharacters(this.charName, this.currentIndex, 10)
    this.characters = result
    this.pageNumber++
    this.disablePrevious = false
  }
}
