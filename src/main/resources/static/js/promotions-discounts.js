/**
 * Promotions and Discounts JavaScript
 */
document.addEventListener('DOMContentLoaded', function() {
    // Initialize Select2 if available
    if (typeof $.fn.select2 !== 'undefined') {
        $('.select2').select2({
            placeholder: "Select items",
            width: '100%'
        });
    }
    
    // Initialize Flatpickr if available
    if (typeof flatpickr !== 'undefined') {
        flatpickr('.datepicker', {
            dateFormat: "Y-m-d",
            minDate: "today"
        });
    }
    
    // Handle discount type change
    const typeSelect = document.getElementById('type');
    const valueSymbol = document.getElementById('valueSymbol');
    const valueInput = document.getElementById('value');
    
    if (typeSelect && valueSymbol) {
        typeSelect.addEventListener('change', function() {
            if (this.value === 'PERCENTAGE') {
                valueSymbol.textContent = '%';
                if (valueInput) {
                    valueInput.setAttribute('max', '100');
                    if (parseFloat(valueInput.value) > 100) {
                        valueInput.value = '100';
                    }
                }
            } else {
                valueSymbol.textContent = '$';
                if (valueInput) {
                    valueInput.removeAttribute('max');
                }
            }
        });
    }
    
    // Confirm delete actions
    const deleteButtons = document.querySelectorAll('[data-confirm]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm(this.getAttribute('data-confirm'))) {
                e.preventDefault();
                return false;
            }
        });
    });
    
    // Update price preview for discounts
    const updatePreview = function() {
        const previewElement = document.getElementById('pricePreview');
        const originalPrice = document.getElementById('originalPrice');
        const discountType = document.getElementById('type');
        const discountValue = document.getElementById('value');
        
        if (previewElement && originalPrice && discountType && discountValue) {
            const price = parseFloat(originalPrice.value);
            const value = parseFloat(discountValue.value) || 0;
            let discountedPrice = price;
            
            if (discountType.value === 'PERCENTAGE') {
                discountedPrice = price - (price * (value / 100));
            } else {
                discountedPrice = price - value;
            }
            
            discountedPrice = Math.max(0, discountedPrice);
            previewElement.textContent = discountedPrice.toFixed(2);
        }
    };
    
    // Set up event listeners for preview
    const previewTriggers = document.querySelectorAll('#type, #value, #originalPrice');
    previewTriggers.forEach(element => {
        if (element) {
            element.addEventListener('input', updatePreview);
            element.addEventListener('change', updatePreview);
        }
    });
    
    // Initial preview update
    if (document.getElementById('pricePreview')) {
        updatePreview();
    }
});