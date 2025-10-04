document.addEventListener('DOMContentLoaded', function() {
    const wizard = document.querySelector('.form-wizard');
    if (!wizard) return;
    
    const steps = wizard.querySelectorAll('.wizard-step');
    const progressBar = wizard.querySelector('.wizard-progress-fill');
    const prevBtn = wizard.querySelector('.wizard-prev');
    const nextBtn = wizard.querySelector('.wizard-next');
    const submitBtn = wizard.querySelector('.wizard-submit');
    
    let currentStep = 0;
    
    function showStep(stepIndex) {
        steps.forEach((step, index) => {
            if (index === stepIndex) {
                step.classList.add('active');
            } else {
                step.classList.remove('active');
            }
        });
        
        const progress = ((stepIndex + 1) / steps.length) * 100;
        if (progressBar) {
            progressBar.style.width = progress + '%';
        }
        
        if (prevBtn) {
            prevBtn.style.display = stepIndex === 0 ? 'none' : 'inline-flex';
        }
        
        if (nextBtn && submitBtn) {
            if (stepIndex === steps.length - 1) {
                nextBtn.style.display = 'none';
                submitBtn.style.display = 'inline-flex';
            } else {
                nextBtn.style.display = 'inline-flex';
                submitBtn.style.display = 'none';
            }
        }
        
        steps[stepIndex].scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
    
    function validateCurrentStep() {
        const currentStepElement = steps[currentStep];
        const inputs = currentStepElement.querySelectorAll('input, select, textarea');
        let isValid = true;
        const invalidFields = [];
        
        inputs.forEach(input => {
            if (input.offsetParent === null || input.disabled) {
                return;
            }
            
            if (window.validateField && !window.validateField(input)) {
                isValid = false;
                invalidFields.push(input);
            }
        });
        
        if (!isValid && invalidFields.length > 0) {
            invalidFields[0].focus();
            invalidFields[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
        
        return isValid;
    }
    
    if (nextBtn) {
        nextBtn.addEventListener('click', function() {
            if (validateCurrentStep()) {
                if (currentStep < steps.length - 1) {
                    currentStep++;
                    showStep(currentStep);
                }
            }
        });
    }
    
    if (prevBtn) {
        prevBtn.addEventListener('click', function() {
            if (currentStep > 0) {
                currentStep--;
                showStep(currentStep);
            }
        });
    }
    
    showStep(0);
});

