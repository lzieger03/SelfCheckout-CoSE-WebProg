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
        // implement text size functionality
        // increase text size by 0.5rem for each click 
        textSize += 0.2;

        if (textSize > 2.4) {
            textSize = 1.2;
        }

        console.log(textSize);

        // increase size of barcode input
        document.getElementById("barcode-input").style.fontSize = `${textSize}rem`;
        document.getElementById("barcode-list").style.fontSize = `${textSize}rem`;

        // increase size of help and info popup content
        document.getElementById("help-popup-content").style.fontSize = `${textSize}rem`;
        document.getElementById("info-popup-content").style.fontSize = `${textSize}rem`;

        // increase size of price details
        document.getElementById("price-detail-subtotal").style.fontSize = `${textSize}rem`;
        document.getElementById("price-detail-tax").style.fontSize = `${textSize}rem`;
        document.getElementById("price-detail-total").style.fontSize = `${textSize}rem`;

        // increase size of buttons
        document.getElementById("payment-btn-label").style.fontSize = `${textSize}rem`;
        document.getElementById("coupon-btn-label").style.fontSize = `${textSize}rem`;
        
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
});