import {Component, inject, OnInit, TemplateRef} from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProductsServiceService} from "../../shared/services/product/products-service.service";
import {ProductDto} from "../../shared/domain/product-dto";

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    NavbarComponent,
    ReactiveFormsModule
  ],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent implements OnInit {
  productForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    price: new FormControl('', [Validators.required, Validators.pattern("\\d+")]),
    quantity: new FormControl('', [Validators.required, Validators.pattern("\\d+")])
  });
  products: ProductDto[] = [];
  private modalService = inject(NgbModal);

  constructor(private productsService: ProductsServiceService) {
  }

  ngOnInit(): void {
    this.fetchProducts();
  }

  open(content: TemplateRef<any>) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'});
  }

  onCreateSubmit(modal: NgbActiveModal) {
    console.log("onCreateSubmit");
    const product: ProductDto = {
      name: this.productForm.value.name!,
      price: Number.parseInt(this.productForm.value.price!),
      availability: Number.parseInt(this.productForm.value.quantity!)
    };

    this.productsService.createProduct(product).subscribe((data) => {
      this.fetchProducts();
    });

    modal.close();
    this.productForm.reset();
  }

  onDelete(id?: number) {
    console.log(id);
  }

  onEdit() {
    console.log("onEdit");

  }

  fetchProducts() {
    this.productsService.getProducts().subscribe((data) => {
      this.products = data;
    });
  }

}
