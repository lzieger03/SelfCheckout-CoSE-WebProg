const TAX_RATE = 0.19; // Tax rate

// Update total price for an item
function updateTotalPrice(li) {
  const singlePrice = parseFloat(
    li.querySelector(".barcode-list-productSinglePrice").textContent.replace("$", "").replace(",", ".")
  );
  const quantity = parseInt(li.querySelector(".barcode-list-productQuantity").textContent);
  const totalPrice = (singlePrice * quantity).toFixed(2).replace(".", ",");
  li.querySelector(".barcode-list-productTotalPrice").textContent = `${totalPrice}$`;
}

// Calculate subtotal, tax, and total
function calculateTotals() {
  let subtotal = 0;
  document.querySelectorAll(".barcode-item").forEach((li) => {
    const totalPrice = parseFloat(
      li.querySelector(".barcode-list-productTotalPrice").textContent.replace("$", "").replace(",", ".")
    );
    subtotal += totalPrice;
  });

  const tax = (subtotal * TAX_RATE).toFixed(2).replace(".", ",");
  const total = (subtotal * (1 + TAX_RATE)).toFixed(2).replace(".", ",");

  document.getElementById("price-detail-subtotal").textContent = `${subtotal.toFixed(2).replace(".", ",")}$`;
  document.getElementById("price-detail-tax").textContent = `${tax}$`;
  document.getElementById("price-detail-total").textContent = `${total}$`;
}
