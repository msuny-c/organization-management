window.validateField = function(field) {
    if (field.offsetParent === null || field.disabled) {
        return true;
    }
        const value = field.value.trim();
        const type = field.type;
        const required = field.hasAttribute('required');
        const min = field.getAttribute('min');
        const max = field.getAttribute('max');
        const minlength = field.getAttribute('minlength');
        
        let errorMessage = '';
        let isValid = true;
        
        if (type === 'number') {
            if (value === '') {
                if (required) {
                    errorMessage = 'Это поле обязательно для заполнения';
                    isValid = false;
                }
            } else {
                const numValue = parseFloat(value);
                
                if (isNaN(numValue) || value.match(/[^\d.\-]/)) {
                    errorMessage = 'Введите числовое значение';
                    isValid = false;
                } else {
                    if (min !== null && numValue < parseFloat(min)) {
                        errorMessage = `Значение должно быть не меньше ${min}`;
                        isValid = false;
                    }
                    if (max !== null && numValue > parseFloat(max)) {
                        errorMessage = `Значение должно быть не больше ${max}`;
                        isValid = false;
                    }
                }
            }
        } else {
            if (required && !value) {
                errorMessage = 'Это поле обязательно для заполнения';
                isValid = false;
            } else if (value) {
                if (type === 'text' || type === 'textarea') {
                    if (minlength && value.length < parseInt(minlength)) {
                        errorMessage = `Минимальная длина: ${minlength} символов`;
                        isValid = false;
                    }
                }
            }
        }
        
    updateFieldValidation(field, isValid, errorMessage);
    return isValid;
}

window.updateFieldValidation = function(field, isValid, errorMessage) {
        const feedbackElement = field.parentElement.querySelector('.invalid-feedback');
        
        if (isValid) {
            field.classList.remove('is-invalid');
            field.classList.add('is-valid');
            if (feedbackElement) {
                feedbackElement.style.display = 'none';
            }
        } else {
            field.classList.remove('is-valid');
            field.classList.add('is-invalid');
            
            if (feedbackElement) {
                feedbackElement.textContent = errorMessage;
                feedbackElement.style.display = 'block';
            } else {
                const newFeedback = document.createElement('div');
                newFeedback.className = 'invalid-feedback';
                newFeedback.textContent = errorMessage;
                newFeedback.style.display = 'block';
                field.parentElement.appendChild(newFeedback);
        }
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        const inputs = form.querySelectorAll('input, select, textarea');
        
        inputs.forEach(input => {
            if (input.type === 'number') {
                input.addEventListener('keypress', (e) => {
                    const char = e.key;
                    const value = input.value;
                    
                    if (char === '-' && value.length === 0) return;
                    if (char === '.' && !value.includes('.') && input.step) return;
                    if (char >= '0' && char <= '9') return;
                    
                    if (char !== 'Backspace' && char !== 'Delete' && char !== 'ArrowLeft' && char !== 'ArrowRight' && char !== 'Tab') {
                        e.preventDefault();
                    }
                });
            }
            
            input.addEventListener('blur', () => window.validateField(input));
            input.addEventListener('input', () => {
                if (input.classList.contains('is-invalid')) {
                    window.validateField(input);
                }
            });
        });
        
        form.addEventListener('submit', function(e) {
            let isValid = true;
            
            inputs.forEach(input => {
                if (!window.validateField(input)) {
                    isValid = false;
                }
            });
            
            if (!isValid) {
                e.preventDefault();
                
                const firstInvalid = form.querySelector('.is-invalid');
                if (firstInvalid) {
                    firstInvalid.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    firstInvalid.focus();
                }
                
                window.showValidationError('Пожалуйста, исправьте ошибки в форме перед отправкой');
            }
        });
    });
    
    const coordinatesSelect = document.getElementById('coordinatesId');
    const newCoordinatesDiv = document.getElementById('newCoordinates');
    
    if (coordinatesSelect && newCoordinatesDiv) {
        const coordInputs = newCoordinatesDiv.querySelectorAll('input');
        
        function updateCoordinatesVisibility() {
            if (coordinatesSelect.value && coordinatesSelect.value !== '') {
                newCoordinatesDiv.style.display = 'none';
                coordInputs.forEach(input => {
                    input.removeAttribute('required');
                });
            } else {
                newCoordinatesDiv.style.display = 'block';
                coordInputs.forEach(input => {
                    input.setAttribute('required', 'required');
                });
            }
        }
        
        coordInputs.forEach(input => {
            input.removeAttribute('required');
        });
        
        coordinatesSelect.addEventListener('change', updateCoordinatesVisibility);
        
        requestAnimationFrame(() => {
            requestAnimationFrame(updateCoordinatesVisibility);
        });
    }
    
    const postalAddressSelect = document.getElementById('postalAddressId');
    const newPostalAddressDiv = document.getElementById('newPostalAddress');
    
    if (postalAddressSelect && newPostalAddressDiv) {
        const postalInputs = newPostalAddressDiv.querySelectorAll('input');
        
        function updatePostalAddressVisibility() {
            if (postalAddressSelect.value && postalAddressSelect.value !== '') {
                newPostalAddressDiv.style.display = 'none';
                postalInputs.forEach(input => {
                    input.removeAttribute('required');
                });
            } else {
                newPostalAddressDiv.style.display = 'block';
                postalInputs.forEach(input => {
                    input.setAttribute('required', 'required');
                });
            }
        }
        
        postalInputs.forEach(input => {
            input.removeAttribute('required');
        });
        
        postalAddressSelect.addEventListener('change', updatePostalAddressVisibility);
        
        requestAnimationFrame(() => {
            requestAnimationFrame(updatePostalAddressVisibility);
        });
    }
    
    const postalTownSelect = document.querySelector('select[name="postalAddress.townId"]');
    const newPostalTownDiv = document.getElementById('newPostalTown');
    
    if (postalTownSelect && newPostalTownDiv) {
        const townInputs = newPostalTownDiv.querySelectorAll('input');
        
        function updatePostalTownVisibility() {
            if (postalTownSelect.value && postalTownSelect.value !== '') {
                newPostalTownDiv.style.display = 'none';
                townInputs.forEach(input => {
                    input.removeAttribute('required');
                });
            } else {
                newPostalTownDiv.style.display = 'block';
                const parentAddress = newPostalAddressDiv;
                if (parentAddress && parentAddress.style.display !== 'none') {
                    townInputs.forEach(input => {
                        input.setAttribute('required', 'required');
                    });
                }
            }
        }
        
        townInputs.forEach(input => {
            input.removeAttribute('required');
        });
        
        postalTownSelect.addEventListener('change', updatePostalTownVisibility);
        
        requestAnimationFrame(() => {
            requestAnimationFrame(updatePostalTownVisibility);
        });
    }
    
    const officialAddressSelect = document.getElementById('officialAddressId');
    const newOfficialAddressDiv = document.getElementById('newOfficialAddress');
    
    if (officialAddressSelect && newOfficialAddressDiv) {
        const officialInputs = newOfficialAddressDiv.querySelectorAll('input');
        
        function updateOfficialAddressVisibility() {
            if (officialAddressSelect.value === '-1') {
                newOfficialAddressDiv.style.display = 'block';
                newOfficialAddressDiv.classList.add('fade-in');
                officialInputs.forEach(input => {
                    input.setAttribute('required', 'required');
                });
            } else {
                newOfficialAddressDiv.style.display = 'none';
                officialInputs.forEach(input => {
                    input.removeAttribute('required');
                });
            }
        }
        
        officialInputs.forEach(input => {
            input.removeAttribute('required');
        });
        
        officialAddressSelect.addEventListener('change', updateOfficialAddressVisibility);
        
        requestAnimationFrame(() => {
            requestAnimationFrame(updateOfficialAddressVisibility);
        });
    }
    
    const officialTownSelect = document.querySelector('select[name="officialAddress.townId"]');
    const newOfficialTownDiv = document.getElementById('newOfficialTown');
    
    if (officialTownSelect && newOfficialTownDiv) {
        const officialTownInputs = newOfficialTownDiv.querySelectorAll('input');
        
        function updateOfficialTownVisibility() {
            if (officialTownSelect.value && officialTownSelect.value !== '') {
                newOfficialTownDiv.style.display = 'none';
                officialTownInputs.forEach(input => {
                    input.removeAttribute('required');
                });
            } else {
                newOfficialTownDiv.style.display = 'block';
                const parentDiv = newOfficialAddressDiv;
                if (parentDiv && parentDiv.style.display !== 'none') {
                    officialTownInputs.forEach(input => {
                        input.setAttribute('required', 'required');
                    });
                }
            }
        }
        
        officialTownInputs.forEach(input => {
            input.removeAttribute('required');
        });
        
        officialTownSelect.addEventListener('change', updateOfficialTownVisibility);
        
        requestAnimationFrame(() => {
            requestAnimationFrame(updateOfficialTownVisibility);
        });
    }
});

window.showValidationError = function(message) {
    const existingAlert = document.querySelector('.validation-alert');
    if (existingAlert) {
        existingAlert.remove();
    }
    
    const alert = document.createElement('div');
    alert.className = 'alert alert-danger validation-alert';
    alert.style.cssText = 'position: fixed; top: 80px; right: 20px; z-index: 9999; animation: slideIn 0.3s ease;';
    alert.innerHTML = `
        <div class="d-flex align-items-center">
            <svg width="20" height="20" fill="currentColor" class="me-2" viewBox="0 0 16 16">
                <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
            </svg>
            <span>${message}</span>
        </div>
    `;
    
    document.body.appendChild(alert);
    
    setTimeout(() => {
        alert.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => alert.remove(), 300);
    }, 5000);
};

