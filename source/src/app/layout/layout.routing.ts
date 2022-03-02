import { BasicModule } from './example/basic/basic.module';
import { LayoutComponent } from './layout.component';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '', component: LayoutComponent, children: [
      { path: '', pathMatch: 'full', redirectTo: '/home' },
      { path: 'home', loadChildren: () => import('./home/home.module').then(m => m.HomeModule) },
      { path: 'example/basic', loadChildren: () => import('./example/basic/basic.module').then(m => m.BasicModule) },
    ]
  },
];

export const LayoutRoutes = RouterModule.forChild(routes);
