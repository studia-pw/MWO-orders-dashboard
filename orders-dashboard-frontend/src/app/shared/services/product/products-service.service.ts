import {Injectable} from '@angular/core';
import {ProductDto} from "../../domain/product-dto";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ProductsServiceService {
  private productsEnpoint = "http://localhost:8080/api/v1/products";

  constructor(private http: HttpClient) {
  }

  createProduct(product: ProductDto) {
    return this.http.post<ProductDto>(this.productsEnpoint, product);
  }

  getProducts() {
    return this.http.get<ProductDto[]>(this.productsEnpoint);
  }
}
