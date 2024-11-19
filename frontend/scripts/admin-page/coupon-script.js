document.addEventListener("DOMContentLoaded", () => {
    const addCouponBtn = document.getElementById("add-coupon-btn");
    const couponPopup = document.getElementById("coupon-popup");
    const couponPopupClose = document.getElementById("coupon-popup-close");
    const couponForm = document.getElementById("coupon-form");
    const couponTableBody = document.querySelector("#coupon-table tbody");
    const couponPopupTitle = document.getElementById("coupon-popup-title");

    let isEditMode = false;
    let editCouponCode = null;
    let isCouponListStale = localStorage.getItem("couponsStale") !== "false";

    /**
     * Fetches all coupons from the backend API.
     * @returns {Promise<Array>} A promise that resolves to an array of coupons.
     */
    async function fetchAllCoupons() {
        // If coupons are cached and not stale, return from localStorage
        if (!isCouponListStale && localStorage.getItem("coupons")) {
            try {
                return JSON.parse(localStorage.getItem("coupons"));
            } catch (error) {
                console.warn("Error parsing cached coupons:", error);
                isCouponListStale = true;
            }
        }

        try {
            const response = await fetch('http://localhost:3000/coupons');
            if (!response.ok) {
                throw new Error(`Error fetching coupons: ${response.statusText}`);
            }
            const coupons = await response.json();
            
            try {
                localStorage.setItem("coupons", JSON.stringify(coupons));
                localStorage.setItem("couponsStale", "false");
                isCouponListStale = false;
            } catch (error) {
                console.warn("Failed to cache coupons:", error);
            }

            return coupons;
        } catch (error) {
            console.error(error);
            alert("Failed to fetch coupons.");
            return [];
        }
    }

    /**
     * Renders the list of coupons in the admin table.
     * @param {Array} coupons - An array of coupon objects.
     */
    function renderCoupons(coupons) {
        couponTableBody.innerHTML = "";
        coupons.forEach(coupon => {
            const tr = document.createElement("tr");

            const tdCode = document.createElement("td");
            tdCode.textContent = coupon.code;
            tr.appendChild(tdCode);

            const tdValue = document.createElement("td");
            tdValue.textContent = coupon.value;
            tr.appendChild(tdValue);

            const tdActions = document.createElement("td");

            const editButton = document.createElement("button");
            editButton.textContent = "Edit";
            editButton.classList.add("edit-btn");
            editButton.addEventListener("click", () => openEditCouponPopup(coupon.code));
            tdActions.appendChild(editButton);

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.classList.add("delete-btn");
            deleteButton.addEventListener("click", () => deleteCoupon(coupon.code));
            tdActions.appendChild(deleteButton);

            tr.appendChild(tdActions);

            couponTableBody.appendChild(tr);
        });
    }

    /**
     * Initializes the coupon management by fetching and rendering all coupons.
     */
    async function initializeCoupons() {
        const coupons = await fetchAllCoupons();
        renderCoupons(coupons);
    }

    /**
     * Opens the popup for adding or editing a coupon.
     * @param {string} [code] - The coupon code to edit. If undefined, opens in add mode.
     */
    function openCouponPopup(code = null) {
        if (code) {
            isEditMode = true;
            editCouponCode = code;
            couponPopupTitle.textContent = "Edit Coupon";
            // Pre-fill the form with existing coupon data
            const coupon = JSON.parse(localStorage.getItem("coupons")).find(c => c.code === code);
            if (coupon) {
                document.getElementById("coupon-code").value = coupon.code;
                document.getElementById("coupon-discount").value = coupon.value;
                document.getElementById("coupon-code").disabled = true; // Prevent changing code
            }
        } else {
            isEditMode = false;
            editCouponCode = null;
            couponPopupTitle.textContent = "Add Coupon";
            couponForm.reset();
            document.getElementById("coupon-code").disabled = false;
        }
        couponPopup.style.display = "flex";
    }

    /**
     * Closes the coupon popup.
     */
    function closeCouponPopup() {
        couponPopup.style.display = "none";
    }

    /**
     * Handles the submission of the coupon form for adding or editing.
     * @param {Event} event - The form submission event.
     */
    couponForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        const code = document.getElementById("coupon-code").value.trim();
        const value = parseFloat(document.getElementById("coupon-discount").value);

        if (!code || isNaN(value)) {
            alert("Please provide valid coupon code and discount value.");
            return;
        }

        if (isEditMode) {
            // Edit existing coupon
            try {
                const response = await fetch(`http://localhost:3000/coupons/${encodeURIComponent(code)}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ code, value })
                });

                if (!response.ok) {
                    const errorMsg = await response.text();
                    throw new Error(errorMsg);
                }

                isCouponListStale = true;
                localStorage.setItem("couponsStale", "true");
                alert("Coupon updated successfully.");
                closeCouponPopup();
                initializeCoupons(); // Refresh the coupon list
            } catch (error) {
                console.error("Error updating coupon:", error);
                alert(`Error updating coupon: ${error.message}`);
            }
        } else {
            // Add new coupon
            try {
                const response = await fetch('http://localhost:3000/coupons', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ code, value })
                });

                if (!response.ok) {
                    const errorMsg = await response.text();
                    throw new Error(errorMsg);
                }

                isCouponListStale = true;
                localStorage.setItem("couponsStale", "true");
                alert("Coupon added successfully.");
                closeCouponPopup();
                initializeCoupons(); // Refresh the coupon list
            } catch (error) {
                console.error("Error adding coupon:", error);
                alert(`Error adding coupon: ${error.message}`);
            }
        }
    });

    /**
     * Deletes a coupon by its code.
     * @param {string} code - The coupon code to delete.
     */
    async function deleteCoupon(code) {
        if (!confirm(`Are you sure you want to delete the coupon "${code}"?`)) return;

        try {
            const response = await fetch(`http://localhost:3000/coupons/${encodeURIComponent(code)}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                const errorMsg = await response.text();
                throw new Error(errorMsg);
            }

            isCouponListStale = true;
            localStorage.setItem("couponsStale", "true");
            alert("Coupon deleted successfully.");
            initializeCoupons(); // Refresh the coupon list
        } catch (error) {
            console.error("Error deleting coupon:", error);
            alert(`Error deleting coupon: ${error.message}`);
        }
    }

    // Event Listeners
    addCouponBtn.addEventListener("click", () => openCouponPopup());
    couponPopupClose.addEventListener("click", closeCouponPopup);

    // Initialize Coupons on Page Load
    initializeCoupons();
});
