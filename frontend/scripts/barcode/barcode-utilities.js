// Find an item in the list by barcode
function findBarcodeInList(barcode) {
    return [...document.querySelectorAll(".barcode-item")].find(
      (li) => li.querySelector(".barcode-list-productBarcode").textContent === barcode
    );
  }
  
  // Create new list item
  function createListItem(barcode) {
    const li = document.createElement("li");
    li.className = "barcode-item";
    li.innerHTML = `
      <img src="/backend/itemPictures/${barcode}.png" alt="product-name" class="barcode-list-productImage">
      <p class="barcode-list-productName">Placeholder Apple</p>
      <p class="barcode-list-productBarcode">${barcode}</p>
      <p class="barcode-list-productSinglePrice">1,99€</p>
      <p class="barcode-list-productQuantity">1</p>
      <p class="barcode-list-productTotalPrice">1,99€</p>
      <button class="delete-btn">Delete</button>
    `;
  
    li.querySelector(".delete-btn").addEventListener("click", () => {
      decreaseQuantity(li);
    });
  
    li.addEventListener("click", () => handleItemClick(li));
    return li;
  }
  
  // Handle list item click and selection
  function handleItemClick(li) {
    if (li.classList.contains("selected")) {
      li.classList.remove("selected");
      const lastItem = barcodeList.lastElementChild;
      if (lastItem) displayItemOnLeft(lastItem);
    } else {
      document.querySelectorAll(".barcode-item").forEach((item) => item.classList.remove("selected"));
      li.classList.add("selected");
      displayItemOnLeft(li);
    }
  }
  
  // Update the quantity of an item
  function updateQuantity(item, change) {
    const quantityElement = item.querySelector(".barcode-list-productQuantity");
    let newQuantity = parseInt(quantityElement.textContent) + change;
    if (newQuantity > 0) {
      quantityElement.textContent = newQuantity;
      updateTotalPrice(item);
    } else {
      item.remove();
      clearLeftDisplay();
    }
    calculateTotals();
    saveBarcodes();
  }
  
  // Clear left section display when item is removed
  function clearLeftDisplay() {
    itemNameElement.textContent = "Item name";
    itemNumberElement.textContent = "Item number";
    quantityDisplay.textContent = "1";
  }
  
  // Scroll list to bottom
  function scrollToBottom() {
    const barcodeListContainer = document.getElementById("barcode-list-container");
    barcodeListContainer.scrollTop = barcodeListContainer.scrollHeight;
  }
  