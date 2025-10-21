// Utility functions for Hostel Management System

/**
 * Show alert message to user
 * @param {string} message - The message to display
 * @param {string} type - Alert type (success, danger, warning, info)
 */
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer');
    if (!alertContainer) {
        console.error('Alert container not found');
        alert(message); // Fallback to browser alert
        return;
    }
    
    // Remove existing alerts
    alertContainer.innerHTML = '';
    
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    alertContainer.appendChild(alertDiv);
    
    // Auto-dismiss after 5 seconds for success messages
    if (type === 'success') {
        setTimeout(() => {
            if (alertDiv && alertDiv.parentNode) {
                alertDiv.remove();
            }
        }, 5000);
    }
}

/**
 * Show loading state for a button
 * @param {HTMLElement} button - The button element
 * @param {boolean} loading - Whether to show loading state
 */
function setButtonLoading(button, loading) {
    if (loading) {
        button.disabled = true;
        button.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Loading...';
    } else {
        button.disabled = false;
        // Restore original text - you'll need to store it
        const originalText = button.getAttribute('data-original-text');
        if (originalText) {
            button.innerHTML = originalText;
        }
    }
}

/**
 * Format date for display
 * @param {string|Date} date - Date to format
 * @returns {string} Formatted date string
 */
function formatDate(date) {
    if (!date) return 'N/A';
    const d = new Date(date);
    return d.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

/**
 * Format time for display
 * @param {string} time - Time in HH:MM format
 * @returns {string} Formatted time string
 */
function formatTime(time) {
    if (!time) return 'N/A';
    const [hours, minutes] = time.split(':');
    const hour12 = parseInt(hours) % 12 || 12;
    const ampm = parseInt(hours) >= 12 ? 'PM' : 'AM';
    return `${hour12}:${minutes} ${ampm}`;
}

/**
 * Validate email format
 * @param {string} email - Email to validate
 * @returns {boolean} Whether email is valid
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Validate phone number format
 * @param {string} phone - Phone number to validate
 * @returns {boolean} Whether phone is valid
 */
function isValidPhone(phone) {
    if (!phone) return true; // Optional field
    const phoneRegex = /^[\d\s\-\+\(\)]{10,15}$/;
    return phoneRegex.test(phone.replace(/\s/g, ''));
}

/**
 * Get compatibility color based on score
 * @param {number} score - Compatibility score (0-100)
 * @returns {string} Bootstrap color class
 */
function getCompatibilityColor(score) {
    if (score >= 80) return 'success';
    if (score >= 60) return 'warning';
    return 'danger';
}

/**
 * Capitalize first letter of each word
 * @param {string} str - String to capitalize
 * @returns {string} Capitalized string
 */
function capitalizeWords(str) {
    if (!str) return '';
    return str.replace(/\w\S*/g, (txt) => 
        txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase()
    );
}

/**
 * Handle API responses consistently
 * @param {Response} response - Fetch response object
 * @returns {Promise} Promise resolving to parsed JSON
 */
async function handleApiResponse(response) {
    const contentType = response.headers.get('content-type');
    
    if (!response.ok) {
        if (contentType && contentType.includes('application/json')) {
            const error = await response.json();
            throw new Error(error.message || `HTTP ${response.status}`);
        } else {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
    }
    
    if (contentType && contentType.includes('application/json')) {
        return await response.json();
    } else {
        return await response.text();
    }
}

/**
 * Make API request with error handling
 * @param {string} url - API endpoint URL
 * @param {object} options - Fetch options
 * @returns {Promise} Promise resolving to response data
 */
async function apiRequest(url, options = {}) {
    try {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        return await handleApiResponse(response);
    } catch (error) {
        console.error('API Request failed:', error);
        throw error;
    }
}

/**
 * Debounce function to limit API calls
 * @param {Function} func - Function to debounce
 * @param {number} wait - Wait time in milliseconds
 * @returns {Function} Debounced function
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Initialize page-specific functionality
document.addEventListener('DOMContentLoaded', function() {
    // Store original button texts for loading states
    document.querySelectorAll('button[type="submit"]').forEach(button => {
        button.setAttribute('data-original-text', button.innerHTML);
    });
    
    // Add loading states to forms
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function() {
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) {
                setButtonLoading(submitBtn, true);
            }
        });
    });
    
    // Auto-focus first form input
    const firstInput = document.querySelector('form input:not([type="hidden"]):first-child');
    if (firstInput) {
        firstInput.focus();
    }
    
    // Add form validation feedback
    document.querySelectorAll('input[type="email"]').forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value && !isValidEmail(this.value)) {
                this.classList.add('is-invalid');
                showAlert('Please enter a valid email address', 'warning');
            } else {
                this.classList.remove('is-invalid');
            }
        });
    });
    
    document.querySelectorAll('input[type="tel"]').forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value && !isValidPhone(this.value)) {
                this.classList.add('is-invalid');
                showAlert('Please enter a valid phone number', 'warning');
            } else {
                this.classList.remove('is-invalid');
            }
        });
    });
});

// Export for use in other scripts
window.utils = {
    showAlert,
    setButtonLoading,
    formatDate,
    formatTime,
    isValidEmail,
    isValidPhone,
    getCompatibilityColor,
    capitalizeWords,
    handleApiResponse,
    apiRequest,
    debounce
};