import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClient} from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { NZ_I18N } from 'ng-zorro-antd/i18n';
import { en_US } from 'ng-zorro-antd/i18n';
import { registerLocaleData } from '@angular/common';
import en from '@angular/common/locales/en';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { DemoNgZorroAntdModule } from './DemoNgZorroAntdModule';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { UserModule } from './features/user/user.module';
import { CoreModule } from './core/core.module';
import { NzLayoutModule } from 'ng-zorro-antd/layout';


registerLocaleData(en);


@NgModule({
  declarations: [
    AppComponent,
    
  ],
  imports: [
    UserModule,
      BrowserModule,
      AppRoutingModule,
      FormsModule,
      DemoNgZorroAntdModule,
      BrowserAnimationsModule,
      ReactiveFormsModule,
      NzInputModule,
      NzFormModule,
      NzSpinModule,
      NzInputModule,
      NzButtonModule,
      NzIconModule,
      CoreModule,
      NzLayoutModule

  ],
  providers: [
    provideHttpClient(),
    { provide: NZ_I18N, useValue: en_US },
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
