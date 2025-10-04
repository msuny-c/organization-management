document.addEventListener('DOMContentLoaded', function() {
    const postalAddressSelect = document.getElementById('postalAddressId');
    const postalZipCode = document.querySelector('input[name="postalAddress.zipCode"]');
    const postalTownSelect = document.querySelector('select[name="postalAddress.townId"]');
    
    const officialAddressSelect = document.getElementById('officialAddressId');
    const reuseFlag = document.getElementById('reusePostalAddressAsOfficial');
    const newOfficialAddressDiv = document.getElementById('newOfficialAddress');
    
    if (!postalAddressSelect || !officialAddressSelect || !reuseFlag) {
        return;
    }
    
    let dynamicPostalOption = null;
    let isReuseSelected = false;
    
    function updateOfficialAddressOptions() {
        const postalIsNew = postalAddressSelect.value === '';
        
        if (dynamicPostalOption) {
            dynamicPostalOption.remove();
            dynamicPostalOption = null;
        }
        
        if (postalIsNew && postalZipCode && postalZipCode.value && postalZipCode.value.length >= 7) {
            let townName = 'город не указан';
            
            if (postalTownSelect && postalTownSelect.value) {
                const selectedOption = postalTownSelect.options[postalTownSelect.selectedIndex];
                townName = selectedOption.text;
            } else {
                const townNameInput = document.querySelector('input[name="postalAddress.town.name"]');
                if (townNameInput && townNameInput.value) {
                    townName = townNameInput.value;
                }
            }
            
            dynamicPostalOption = document.createElement('option');
            dynamicPostalOption.value = '__REUSE_POSTAL__';
            dynamicPostalOption.textContent = `${postalZipCode.value} - ${townName}`;
            dynamicPostalOption.dataset.isReuse = 'true';
            
            const firstExistingOption = officialAddressSelect.querySelector('option[value]:not([value=""])');
            if (firstExistingOption) {
                officialAddressSelect.insertBefore(dynamicPostalOption, firstExistingOption);
            } else {
                officialAddressSelect.appendChild(dynamicPostalOption);
            }
        }
    }
    
    function handleOfficialAddressChange() {
        const selectedOption = officialAddressSelect.options[officialAddressSelect.selectedIndex];
        
        if (selectedOption && selectedOption.dataset.isReuse === 'true') {
            isReuseSelected = true;
            reuseFlag.value = 'true';
            
            setTimeout(() => {
                officialAddressSelect.value = '__REUSE_POSTAL__';
            }, 0);
            
            if (newOfficialAddressDiv) {
                newOfficialAddressDiv.style.display = 'none';
                const inputs = newOfficialAddressDiv.querySelectorAll('input, select');
                inputs.forEach(input => input.removeAttribute('required'));
            }
        } else {
            isReuseSelected = false;
            reuseFlag.value = 'false';
            
            if (newOfficialAddressDiv && !officialAddressSelect.value) {
                newOfficialAddressDiv.style.display = 'none';
            }
        }
    }
    
    const form = officialAddressSelect.closest('form');
    if (form) {
        form.addEventListener('submit', function(e) {
            if (isReuseSelected) {
                officialAddressSelect.value = '';
            }
        });
    }
    
    postalAddressSelect.addEventListener('change', function() {
        setTimeout(updateOfficialAddressOptions, 100);
    });
    
    if (postalZipCode) {
        postalZipCode.addEventListener('blur', updateOfficialAddressOptions);
        postalZipCode.addEventListener('input', function() {
            if (this.value.length >= 7) {
                updateOfficialAddressOptions();
            }
        });
    }
    
    if (postalTownSelect) {
        postalTownSelect.addEventListener('change', updateOfficialAddressOptions);
    }
    
    const postalTownInputs = document.querySelectorAll('#newPostalTown input');
    postalTownInputs.forEach(input => {
        input.addEventListener('blur', updateOfficialAddressOptions);
    });
    
    officialAddressSelect.addEventListener('change', handleOfficialAddressChange);
    
    const wizardNextButtons = document.querySelectorAll('.wizard-nav button[type="button"]');
    wizardNextButtons.forEach(btn => {
        if (btn.textContent.includes('Далее')) {
            btn.addEventListener('click', function() {
                const currentStep = document.querySelector('.wizard-step.active');
                if (currentStep && currentStep.querySelector('#postalAddressId')) {
                    setTimeout(updateOfficialAddressOptions, 150);
                }
            });
        }
    });
});
