document.addEventListener("DOMContentLoaded", () => {
    const addCouponBtn = document.getElementById("add-coupon-btn");
    const couponPopup = document.getElementById("coupon-popup");
    const couponPopupClose = document.getElementById("coupon-popup-close");
    const couponForm = document.getElementById("coupon-form");
    const couponTableBody = document.querySelector("#coupon-table tbody");
    const couponPopupTitle = document.getElementById("coupon-popup-title");

    let isEditMode = false;
    let editCouponCode = null;

    async function fetchAllCoupons() {
        try {
            const response = await fetch('http://localhost:8080/allcoupons');
            if (!response.ok) {
                throw new Error(`Error fetching coupons: ${response.statusText}`);
            }
            const coupons = await response.json();
            return coupons;
        } catch (error) {
            console.error(error);
            alert("Failed to fetch coupons.");
            return [];
        }
    }

    function renderCoupons(coupons) {
        couponTableBody.innerHTML = '';
        coupons.forEach(coupon => {
            const tr = document.createElement("tr");

            const tdCode = document.createElement("td");
            tdCode.textContent = coupon.code;
            tr.appendChild(tdCode);

            const tdDiscount = document.createElement("td");
            tdDiscount.textContent = coupon.value.toFixed(2); // Ensure correct property
            tr.appendChild(tdDiscount);

            const tdActions = document.createElement("td");

            const editBtn = document.createElement("button");
            editBtn.textContent = "Edit";
            editBtn.classList.add("action-btn-edit", "action-btn");
            editBtn.addEventListener("click", () => {
                isEditMode = true;
                openEditCouponPopup(coupon.code);
            });
            tdActions.appendChild(editBtn);

            const deleteBtn = document.createElement("button");
            deleteBtn.textContent = "Delete";
            deleteBtn.classList.add("action-btn-delete", "action-btn");
            deleteBtn.addEventListener("click", () => {
                deleteCoupon(coupon.code);
            });
            tdActions.appendChild(deleteBtn);

            tr.appendChild(tdActions);
            couponTableBody.appendChild(tr);
        });
    }

    async function initializeCoupons() {
        const coupons = await fetchAllCoupons();
        renderCoupons(coupons);
    }

    addCouponBtn.addEventListener("click", () => {
        isEditMode = false;
        couponPopupTitle.textContent = "Add New Coupon";
        couponForm.reset();
        couponPopup.style.display = "flex";
    });

    couponPopupClose.addEventListener("click", () => {
        couponPopup.style.display = "none";
    });

    couponForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        const code = document.getElementById("coupon-code").value.trim();
        const value = parseFloat(document.getElementById("coupon-discount").value.trim());

        if (!code || isNaN(value) || value < 0 || value > 100) {
            alert("Please enter a valid coupon code and discount value.");
            return;
        }

        try {
            let response;
            if (isEditMode && editCouponCode) {
                console.log("Updating existing coupon: " + editCouponCode);
                // Update existing coupon
                response = await fetch(`http://localhost:8080/discount?code=${encodeURIComponent(editCouponCode)}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({
                        value: value,
                    }),
                });
            } else {
                console.log("Creating new coupon: " + code);
                // Create new coupon
                response = await fetch('http://localhost:8080/discount', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({
                        code: code,
                        value: value,
                    }),
                });
            }

            if (!response.ok) {
                const errorMsg = await response.text();
                throw new Error(errorMsg);
            }

            alert("Coupon saved successfully.");
            couponPopup.style.display = "none";
            initializeCoupons(); // Refresh the coupon list
        } catch (error) {
            console.error("Error saving coupon:", error);
            alert(`Error saving coupon: ${error.message}`);
        }
    });

    /**
     * Deletes a coupon by its code.
     * Implement the delete endpoint on the backend if not already done.
     * @param {string} code - The coupon code to delete.
     */
    async function deleteCoupon(code) {
        if (!confirm(`Are you sure you want to delete the coupon "${code}"?`)) return;

        try {
            const response = await fetch(`http://localhost:8080/discount?code=${encodeURIComponent(code)}`, {
                method: 'DELETE', // Ensure backend supports DELETE method
            });

            if (!response.ok) {
                const errorMsg = await response.text();
                throw new Error(errorMsg);
            }

            alert("Coupon deleted successfully.");
            initializeCoupons(); // Refresh the coupon list
        } catch (error) {
            console.error("Error deleting coupon:", error);
            alert(`Error deleting coupon: ${error.message}`);
        }
    }

    /**
     * Opens the edit popup with the coupon details pre-filled.
     * @param {string} code - The coupon code to edit.
     */
    function openEditCouponPopup(code) {
        // Fetch the coupon details if necessary
        // For simplicity, assuming the coupon details are already available
        isEditMode = true;
        editCouponCode = code;
        couponPopupTitle.textContent = "Edit Coupon";
        // Pre-fill the form
        const coupon = Array.from(couponTableBody.children)
            .find(row => row.children[0].textContent === code);
        if (coupon) {
            const discount = parseFloat(coupon.children[1].textContent);
            document.getElementById("coupon-code").value = code;
            document.getElementById("coupon-discount").value = discount;
        }
        couponPopup.style.display = "flex";
    }

    initializeCoupons();
});
