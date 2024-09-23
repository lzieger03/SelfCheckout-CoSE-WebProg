document.addEventListener('DOMContentLoaded', () => {
    const barcodeInput = document.getElementById('barcode-input');
    const barcodeList = document.getElementById('barcode-list');

    // Funktion, um gescannte Artikelnummer zur Liste hinzuzufügen
    function addBarcodeToList(barcode) {
        const li = document.createElement('li');
        li.className = 'barcode-item';
        li.innerHTML = `
            <span>${barcode}</span>
            <button class="delete-btn">Delete</button>
        `;

        // Event-Listener für den Löschbutton
        li.querySelector('.delete-btn').addEventListener('click', () => {
            li.remove();
            saveBarcodes(); // Liste nach dem Entfernen speichern
        });

        barcodeList.appendChild(li);
        saveBarcodes(); // Liste nach dem Hinzufügen speichern
    }

    // Event-Listener für Barcode-Eingaben
    barcodeInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault(); // Verhindert das Neuladen der Seite

            const barcode = barcodeInput.value.trim();

            if (barcode !== '') {
                addBarcodeToList(barcode); // Barcode der Liste hinzufügen
                barcodeInput.value = ''; // Eingabefeld leeren
            }
        }
    });

    // Speichern der Barcode-Liste in localStorage
    function saveBarcodes() {
        const barcodes = [];
        document.querySelectorAll('.barcode-item span').forEach(item => {
            barcodes.push(item.textContent);
        });
        localStorage.setItem('barcodes', JSON.stringify(barcodes));
    }

    // Laden der Barcode-Liste aus localStorage
    function loadBarcodes() {
        const storedBarcodes = localStorage.getItem('barcodes');
        if (storedBarcodes) {
            JSON.parse(storedBarcodes).forEach(barcode => addBarcodeToList(barcode));
        }
    }

    // Barcode-Liste beim Laden der Seite wiederherstellen
    loadBarcodes();
});
