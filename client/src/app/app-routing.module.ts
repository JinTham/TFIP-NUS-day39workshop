import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchComponent } from './components/search.component';
import { ListComponent } from './components/list.component';
import { DetailsComponent } from './components/details.component';
import { CommentComponent } from './components/comment.component';

const routes: Routes = [
  {path:"",component:SearchComponent},
  {path:"list/:charName",component:ListComponent},
  {path:"details/:charId",component:DetailsComponent},
  {path:"comment",component:CommentComponent},
  {path:"**",redirectTo:"/",pathMatch:'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
