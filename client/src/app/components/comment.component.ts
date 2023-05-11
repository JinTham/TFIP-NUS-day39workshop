import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
import { MarvelCharService } from '../services/marvel-char.service';
import { Comment } from '../models/comment';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent {
  form!: FormGroup;
  sub$! :  Subscription;
  charParam!: any;
  charName! : string;
  charId!: string;
  commentObj!:Comment

  constructor(private activatedRoute: ActivatedRoute,  private formBuilder: FormBuilder,
    private marvelCharSvc: MarvelCharService, private router: Router){ }

  ngOnInit(): void {
    this.form = this.createForm();
    this.sub$ = this.activatedRoute.queryParams.subscribe(
      (queryParams) => {
        this.charParam = queryParams['charParam'].split('|')
        this.charName = this.charParam[0]
        this.charId = this.charParam[1]
      }
    );

  }

  saveComment(){
    const comment = this.form.value['comment'];
    this.commentObj = {id:this.charId, comment:comment}
    this.marvelCharSvc.saveComment(this.commentObj);
    this.router.navigate(['/details', this.charId]);
  }

  cancel(){
    this.router.navigate(['/details', this.charId]);
  }

  ngOnDestroy(): void{
    this.sub$.unsubscribe();
  }

  private createForm(): FormGroup{
    return this.formBuilder.group({
      comment : this.formBuilder.control(''),  
    })
  }
}
