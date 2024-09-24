document.addEventListener("DOMContentLoaded", () => {
  const barcodeInput = document.getElementById("barcode-input");
  const barcodeList = document.getElementById("barcode-list");
  const subtotalElement = document.getElementById("price-detail-subtotal");
  const taxElement = document.getElementById("price-detail-tax");
  const totalElement = document.getElementById("price-detail-total");

  const TAX_RATE = 0.19; // Beispielsteuer von 19%

  // Erlaubt nur Zahlen-Eingaben im Textfeld
  barcodeInput.addEventListener("input", (event) => {
    barcodeInput.value = barcodeInput.value.replace(/[^0-9]/g, ""); // Filtert alle nicht-numerischen Zeichen heraus
  });

  // Funktion, um zu prüfen, ob der Barcode bereits existiert
  function findBarcodeInList(barcode) {
    return [...document.querySelectorAll(".barcode-item")].find(
      (li) =>
        li.querySelector(".barcode-list-productBarcode").textContent === barcode
    );
  }

  // Funktion, um den Preis neu zu berechnen (Menge * Einzelpreis)
  function updateTotalPrice(li) {
    const singlePrice = parseFloat(
      li
        .querySelector(".barcode-list-productSinglePrice")
        .textContent.replace("$", "")
        .replace(",", ".")
    );
    const quantity = parseInt(
      li.querySelector(".barcode-list-productQuantity").textContent
    );
    const totalPrice = (singlePrice * quantity).toFixed(2).replace(".", ",");
    li.querySelector(
      ".barcode-list-productTotalPrice"
    ).textContent = `${totalPrice}$`;
  }

  // Funktion zur Berechnung von Subtotal, Tax und Total
  function calculateTotals() {
    let subtotal = 0;
    document.querySelectorAll(".barcode-item").forEach((li) => {
      const totalPrice = parseFloat(
        li
          .querySelector(".barcode-list-productTotalPrice")
          .textContent.replace("$", "")
          .replace(",", ".")
      );
      subtotal += totalPrice;
    });

    const tax = (subtotal * TAX_RATE).toFixed(2).replace(".", ",");
    const total = (subtotal * (1 + TAX_RATE)).toFixed(2).replace(".", ",");

    subtotalElement.textContent = `${subtotal.toFixed(2).replace(".", ",")}$`;
    taxElement.textContent = `${tax}$`;
    totalElement.textContent = `${total}$`;
  }

  // Funktion, um die Liste automatisch nach unten zu scrollen
  function scrollToBottom() {
    const barcodeListContainer = document.getElementById("barcode-list-container");
    barcodeListContainer.scrollTop = barcodeListContainer.scrollHeight;
  }

  // Funktion, um gescannte Artikelnummer zur Liste hinzuzufügen oder Menge zu erhöhen
  function addBarcodeToList(barcode) {
    const existingItem = findBarcodeInList(barcode);

    if (existingItem) {
      // Barcode existiert bereits, erhöhe die Menge und aktualisiere den Preis
      const quantityElement = existingItem.querySelector(
        ".barcode-list-productQuantity"
      );
      const newQuantity = parseInt(quantityElement.textContent) + 1;
      quantityElement.textContent = newQuantity;

      // Aktualisiere den Gesamtpreis
      updateTotalPrice(existingItem);
    } else {
      // Barcode existiert noch nicht, füge neuen Eintrag hinzu
      const li = document.createElement("li");
      li.className = "barcode-item";
      li.innerHTML = `
          <img src="/backend/itemPictures/placeholder.png" alt="product-name" class="barcode-list-productImage">
          <p class="barcode-list-productName">Placeholder Apple</p>
          <p class="barcode-list-productBarcode">${barcode}</p>
          <p class="barcode-list-productSinglePrice">1,99€</p>
          <p class="barcode-list-productQuantity">1</p>
          <p class="barcode-list-productTotalPrice">1,99€</p>
          <button class="delete-btn">Delete</button>
      `;

      // Event-Listener für den Löschbutton
      li.querySelector(".delete-btn").addEventListener("click", () => {
        decreaseQuantity(li);
      });

      barcodeList.appendChild(li);
    }

    calculateTotals(); // Berechne Totals nach jedem Update
    saveBarcodes(); // Liste nach dem Hinzufügen oder Aktualisieren speichern
    scrollToBottom(); // Automatisch nach unten scrollen
  }

  // Funktion, um die Menge zu verringern und Artikel zu löschen, wenn die Menge 0 ist
  function decreaseQuantity(li) {
    const quantityElement = li.querySelector(".barcode-list-productQuantity");
    const newQuantity = parseInt(quantityElement.textContent) - 1;

    if (newQuantity > 0) {
      quantityElement.textContent = newQuantity;
      updateTotalPrice(li); // Aktualisiere den Preis nach dem Verringern der Menge
    } else {
      li.remove(); // Entferne den Artikel, wenn die Menge 0 ist
    }

    calculateTotals(); // Berechne Totals nach dem Update
    saveBarcodes(); // Liste nach dem Entfernen speichern
  }

  // Event-Listener für Barcode-Eingaben
  barcodeInput.addEventListener("keyup", (event) => {
    if (event.key === "Enter") {
      event.preventDefault(); // Verhindert das Neuladen der Seite

      const barcode = barcodeInput.value.trim();

      if (barcode !== "") {
        addBarcodeToList(barcode); // Barcode der Liste hinzufügen oder aktualisieren
        barcodeInput.value = ""; // Eingabefeld leeren
      }
    }
  });

  // Speichern der Barcode-Liste in localStorage
  function saveBarcodes() {
    const barcodes = [];
    document.querySelectorAll(".barcode-item").forEach((item) => {
      const barcode = item.querySelector(
        ".barcode-list-productBarcode"
      ).textContent;
      const quantity = item.querySelector(
        ".barcode-list-productQuantity"
      ).textContent;
      barcodes.push({ barcode, quantity });
    });
    localStorage.setItem("barcodes", JSON.stringify(barcodes));
  }

  // Laden der Barcode-Liste aus localStorage
  function loadBarcodes() {
    const storedBarcodes = localStorage.getItem("barcodes");
    if (storedBarcodes) {
      JSON.parse(storedBarcodes).forEach((item) => {
        for (let i = 0; i < item.quantity; i++) {
          addBarcodeToList(item.barcode);
        }
      });
    }
  }

  // Barcode-Liste beim Laden der Seite wiederherstellen
  loadBarcodes();
});
