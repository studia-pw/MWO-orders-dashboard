import {Routes} from '@angular/router';
import {OrdersComponent} from "./pages/orders/orders.component";
import {ClientsComponent} from "./pages/clients/clients.component";
import {ProductsComponent} from "./pages/products/products.component";
import {NavbarComponent} from "./shared/components/navbar/navbar.component";

export const routes: Routes = [
  {path: '', component: OrdersComponent},
  {path: 'orders', component: OrdersComponent},
  {path: 'clients', component: ClientsComponent},
  {path: 'products', component: ProductsComponent},
  {path: 'navbar', component: NavbarComponent}
];
