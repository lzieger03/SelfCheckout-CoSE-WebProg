document.addEventListener("DOMContentLoaded", () => {
  // empty local storage on load
  localStorage.clear();

  const barcodeInput = document.getElementById("barcode-input");
  const barcodeList = document.getElementById("barcode-list");
  const startPopup = document.getElementById("start-popup");
  const subtotalElement = document.getElementById("price-detail-subtotal");
  const taxElement = document.getElementById("price-detail-tax");
  const totalElement = document.getElementById("price-detail-total");

  const TAX_RATE = 0.19; // Beispielsteuer von 19%

  const itemNameElement = document.getElementById("product-info-itemName");
  const itemNumberElement = document.getElementById("product-info-itemNumber");
  const quantityDisplay = document.querySelector(".quantity-display");
  const productImageElement = document.getElementById("item-preview-image");
  let currentSelectedItem = null; // Halte das aktuelle Listenelement fest

  // Setze den Fokus auf das Eingabefeld, sobald die Seite geladen wird
  barcodeInput.focus();

  // Funktion, um den Fokus kontinuierlich auf das Eingabefeld zu setzen
  function keepFocusOnInput() {
    // Workaround für Safari: Timer nutzen, um sicherzustellen, dass der Fokus gesetzt wird
    setTimeout(() => {
      barcodeInput.focus();
    }, 10); // Verzögerung von 10 Millisekunden
  }

  // Setze den Fokus nach Interaktionen
  document.addEventListener("click", (event) => {
    if (!barcodeInput.contains(event.target)) {
      keepFocusOnInput();
    }
  });

  // Verhindere, dass der Fokus verloren geht, wenn eine Taste gedrückt wird
  barcodeInput.addEventListener("keydown", () => {
    keepFocusOnInput();
  });

  // Funktion, um das Produkt auf der linken Seite anzuzeigen
  function displayItemOnLeft(item) {
    const itemName = item.querySelector(
      ".barcode-list-productName"
    ).textContent;
    const itemBarcode = item.querySelector(
      ".barcode-list-productBarcode"
    ).textContent;
    const itemQuantity = item.querySelector(
      ".barcode-list-productQuantity"
    ).textContent;

    // Dynamische Zuordnung des Bildes basierend auf der Artikelnummer (Barcode)
    const itemImageSrc = `/backend/itemPictures/${itemBarcode}.png`; // Bilddatei basierend auf Barcode

    itemNameElement.textContent = itemName;
    itemNumberElement.textContent = itemBarcode;
    quantityDisplay.textContent = itemQuantity;
    productImageElement.src = itemImageSrc; // Aktualisiere das Bild

    currentSelectedItem = item;
  }

  // Funktion, um das Start-Popup zu schließen
  function closeStartPopup() {
    startPopup.style.display = "none";
  }

  startPopup.addEventListener("click", () => {
    closeStartPopup();
  });

  // Setze den Fokus auf das Eingabefeld, wenn die Eingabe bearbeitet wird
  barcodeInput.addEventListener("input", () => {
    keepFocusOnInput();
  });

  // Setze den Fokus zurück auf das Eingabefeld, wenn irgendwo auf der Seite geklickt wird
  document.addEventListener("focusout", () => {
    keepFocusOnInput();
  });

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
    const barcodeListContainer = document.getElementById(
      "barcode-list-container"
    );
    barcodeListContainer.scrollTop = barcodeListContainer.scrollHeight;
  }

  // Funktion, um gescannte Artikelnummer zur Liste hinzuzufügen oder Menge zu erhöhen
  function addBarcodeToList(barcode) {
    // Schließe das Start-Popup nach dem ersten Scan
    if (barcodeList.children.length === 0) {
      closeStartPopup();
    }

    // Entferne alle 'selected'-Klassen von bestehenden Listenelementen
    document.querySelectorAll(".barcode-item.selected").forEach((item) => {
      item.classList.remove("selected");
    });

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

      // Zeige das aktualisierte Produkt auf der linken Seite an
      displayItemOnLeft(existingItem);
    } else {
      // Barcode existiert noch nicht, füge neuen Eintrag hinzu
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

      // Event-Listener für den Löschbutton
      li.querySelector(".delete-btn").addEventListener("click", () => {
        decreaseQuantity(li);
      });

      // Event-Listener, um das Listenelement anklickbar zu machen und auf der linken Seite anzuzeigen
      li.addEventListener("click", function () {
        // Wenn das Listenelement bereits ausgewählt ist, entferne die 'selected'-Klasse (toggle)
        if (li.classList.contains("selected")) {
          li.classList.remove("selected");

          // Prüfe, ob noch ein Element ausgewählt ist
          const selectedItem = document.querySelector(".barcode-item.selected");
          if (!selectedItem) {
            // Wenn kein Element ausgewählt ist, zeige einfach das letzte Element der Liste, ohne es auszuwählen
            const lastItem = barcodeList.lastElementChild; // Das letzte Listenelement
            if (lastItem) {
              displayItemOnLeft(lastItem); // Zeige nur das letzte Element auf der linken Seite an, ohne es auszuwählen
            }
          }
        } else {
          // Entferne die 'selected'-Klasse von allen Listenelementen
          document
            .querySelectorAll(".barcode-item")
            .forEach((item) => item.classList.remove("selected"));

          // Füge die 'selected'-Klasse zum geklickten Element hinzu
          li.classList.add("selected");

          displayItemOnLeft(li); // Zeige das geklickte Element auf der linken Seite an
        }
      });

      barcodeList.appendChild(li);

      // Zeige das neue Produkt auf der linken Seite an
      displayItemOnLeft(li);
    }

    calculateTotals(); // Berechne Totals nach jedem Update
    saveBarcodes(); // Liste nach dem Hinzufügen oder Aktualisieren speichern
    scrollToBottom(); // Automatisch nach unten scrollen
    keepFocusOnInput(); // Halte den Fokus auf der Eingabezeile
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

  // Plus und Minus Buttons für das aktuelle Produkt auf der linken Seite
  document.getElementById("quantity-btn-plus").addEventListener("click", () => {
    if (currentSelectedItem) {
      const quantityElement = currentSelectedItem.querySelector(
        ".barcode-list-productQuantity"
      );
      quantityElement.textContent = parseInt(quantityElement.textContent) + 1;
      quantityDisplay.textContent = quantityElement.textContent; // Aktualisiere die Anzeige auf der linken Seite
      updateTotalPrice(currentSelectedItem);
      calculateTotals();
      saveBarcodes();
    }
  });

  document
    .getElementById("quantity-btn-minus")
    .addEventListener("click", () => {
      if (currentSelectedItem) {
        const quantityElement = currentSelectedItem.querySelector(
          ".barcode-list-productQuantity"
        );
        const newQuantity = parseInt(quantityElement.textContent) - 1;
        if (newQuantity > 0) {
          quantityElement.textContent = newQuantity;
          quantityDisplay.textContent = quantityElement.textContent; // Aktualisiere die Anzeige auf der linken Seite
          updateTotalPrice(currentSelectedItem);
          calculateTotals();
          saveBarcodes();
        } else {
          currentSelectedItem.remove(); // Entferne den Artikel, wenn die Menge 0 ist
          currentSelectedItem = null; // Setze die Auswahl zurück
          itemNameElement.textContent = "Item name";
          itemNumberElement.textContent = "Item number";
          quantityDisplay.textContent = "1";
          calculateTotals();
          saveBarcodes();
        }
      }
    });

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
