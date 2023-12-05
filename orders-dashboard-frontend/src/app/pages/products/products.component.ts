import {Component, inject, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {NgbActiveModal, NgbModal, NgbToast} from "@ng-bootstrap/ng-bootstrap";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProductsServiceService} from "../../shared/services/product/products-service.service";
import {ProductDto} from "../../shared/domain/product-dto";
import {CurrencyPipe} from "@angular/common";

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    NavbarComponent,
    ReactiveFormsModule,
    CurrencyPipe,
    NgbToast
  ],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent implements OnInit {
  productFormCreate = new FormGroup({
    name: new FormControl('', [Validators.required]),
    price: new FormControl('', [Validators.required, Validators.pattern("\\d+\.?\\d{0,2}")]),
    quantity: new FormControl('', [Validators.required, Validators.pattern("\\d+")])
  });

  productFormUpdate = new FormGroup({
    name: new FormControl('', [Validators.required]),
    price: new FormControl('', [Validators.required, Validators.pattern("\\d+\.?\\d{0,2}")]),
    quantity: new FormControl('', [Validators.required, Validators.pattern("\\d+")])
  });

  products: ProductDto[] = [];

  @ViewChild('update') updateModal!: TemplateRef<any>;

  @ViewChild('deleteConfirmation') deleteModal!: TemplateRef<any>;

  public deleteMsg: string = "";

  private modalService = inject(NgbModal);

  private selectedProduct?: ProductDto;

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
      name: this.productFormCreate.value.name!,
      price: Number.parseFloat(this.productFormCreate.value.price!),
      availability: Number.parseInt(this.productFormCreate.value.quantity!)
    };

    this.productsService.createProduct(product).subscribe((data) => {
      this.fetchProducts();
    });

    modal.close();
    this.productFormCreate.reset();
  }

  onDelete(id?: number) {
    if (id == null) return;
    this.selectedProduct = this.products.find((product) => product.id === id);
    this.deleteMsg = "Are you sure you want to delete " + this.selectedProduct!.name + "?";
    this.open(this.deleteModal);
  }

  onDeleteSubmit(modal: NgbActiveModal) {
    console.log("onDeleteSubmit");
    this.productsService.deleteProduct(this.selectedProduct?.id!).subscribe((data) => {
      this.fetchProducts();
    });

    modal.close();
  }

  onEdit(id?: number) {
    if (id == null) return;

    this.open(this.updateModal);
    this.productsService.getProduct(id).subscribe((data) => {
      this.selectedProduct = data;
      this.productFormUpdate.setValue({
        name: data.name,
        price: data.price.toString(),
        quantity: data.availability.toString()
      });
    });
  }

  onEditSubmit(modal: NgbActiveModal) {
    console.log(this.selectedProduct);
    const product: ProductDto = {
      id: this.selectedProduct?.id,
      name: this.productFormUpdate.value.name!,
      price: Number.parseFloat(this.productFormUpdate.value.price!),
      availability: Number.parseInt(this.productFormUpdate.value.quantity!)
    };

    this.productsService.updateProduct(product).subscribe((data) => {
      this.fetchProducts();
    });
    modal.close();
  }

  selectAll() {
    console.log("selectAll");
  }

  fetchProducts() {
    this.productsService.getProducts().subscribe((data) => {
      this.products = data;
    });
  }

}
