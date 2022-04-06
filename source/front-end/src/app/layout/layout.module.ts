import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from './layout.component';
import { LayoutRoutes } from './layout.routing';
import { IconsProviderModule } from '../../../projects/_base/module/icons-provider.module';
import { MenuComponent } from './menu/menu.component';
import { FooterComponent } from './footer/footer.component';
import { NavbarComponent } from './navbar/navbar.component';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    IconsProviderModule,
    NzSpinModule,
    FormsModule,
    ReactiveFormsModule,
    LayoutRoutes
  ],
  declarations: [
    LayoutComponent,
    NavbarComponent,
    MenuComponent,
    FooterComponent
  ]
})
export class LayoutModule { }
