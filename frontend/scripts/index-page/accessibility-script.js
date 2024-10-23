document.addEventListener("DOMContentLoaded", () => {
    const screenReaderBtn = document.getElementById("screenReader-btn");
    const textSizeBtn = document.getElementById("textSize-btn");
    const helpBtn = document.getElementById("help-btn");
    const infoBtn = document.getElementById("info-btn");
    const helpPopupCloseBtn = document.getElementById("help-popup-close-btn");
    const infoPopupCloseBtn = document.getElementById("info-popup-close-btn");

    let textSize = 1.6;

    // Screen Reader Button
    screenReaderBtn.addEventListener("click", () => {
        // TODO: Implement screen reader functionality
        alert("Screen reader functionality not implemented yet.");
    });

    textSizeBtn.addEventListener("click", () => {
        // Implement text size functionality
        // Erhöhe die Textgröße um 0.2rem bei jedem Klick
        textSize += 0.2;

        if (textSize > 2.4) {
            textSize = 1.2;
        }

        console.log(`Aktuelle Textgröße: ${textSize}rem`);

        // Erhöhe die Größe der benötigten Elemente
        const elementsToResize = [
            "barcode-input",
            "barcode-list",
            "help-popup-content",
            "info-popup-content",
            "price-detail-subtotal",
            "price-detail-tax",
            "price-detail-total",
            "payment-btn-label",
            "coupon-btn-label"
        ];

        elementsToResize.forEach(id => {
            const element = document.getElementById(id);
            if (element) {
                element.style.fontSize = `${textSize}rem`;
            }
        });
    });

    helpBtn.addEventListener("click", () => {
        // open help popup
        document.getElementById("help-popup").style.display = "flex";
    });

    helpPopupCloseBtn.addEventListener("click", () => {
        // close help popup
        document.getElementById("help-popup").style.display = "none";
    });

    infoBtn.addEventListener("click", () => {
        // open info popup
        document.getElementById("info-popup").style.display = "flex";
    });

    infoPopupCloseBtn.addEventListener("click", () => {
        // close info popup
        document.getElementById("info-popup").style.display = "none";
    });

    // Initialisiere die kollabierbaren Elemente
    const collapsibles = document.querySelectorAll(".collapsible");

    collapsibles.forEach((collapsible) => {
        collapsible.addEventListener("click", function () {
            this.classList.toggle("active");
            const content = this.nextElementSibling;
            if (content.style.display === "block") {
                content.style.display = "none";
            } else {
                content.style.display = "block";
            }
        });
    });
});
