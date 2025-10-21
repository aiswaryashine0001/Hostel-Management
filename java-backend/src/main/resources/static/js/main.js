// Main JavaScript for Hostel Management System

// Global utility functions
function showAlert(message, type = 'info', duration = 5000) {
    const alertContainer = document.getElementById('alert-container');
    if (!alertContainer) return;
    
    // Create alert element
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        <i class="fas ${getAlertIcon(type)}"></i> ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // Add to container
    alertContainer.appendChild(alertDiv);
    
    // Auto remove after duration
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.remove();
        }
    }, duration);
}

function getAlertIcon(type) {
    const icons = {
        'success': 'fa-check-circle',
        'danger': 'fa-exclamation-triangle',
        'warning': 'fa-exclamation-circle',
        'info': 'fa-info-circle'
    };
    return icons[type] || 'fa-info-circle';
}

// Form validation utilities
function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function validatePhone(phone) {
    const phoneRegex = /^[\+]?[1-9][\d]{0,15}$/;
    return phoneRegex.test(phone.replace(/\s/g, ''));
}

function validatePassword(password) {
    return password.length >= 6;
}

// API utilities
async function makeAPIRequest(url, options = {}) {
    try {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        return await response.json();
    } catch (error) {
        console.error('API Request Error:', error);
        throw error;
    }
}

// Loading states
function setLoading(element, isLoading = true) {
    if (isLoading) {
        element.classList.add('loading');
        element.setAttribute('disabled', 'disabled');
    } else {
        element.classList.remove('loading');
        element.removeAttribute('disabled');
    }
}

// Form helpers
function getFormData(formElement) {
    const formData = new FormData(formElement);
    return Object.fromEntries(formData.entries());
}

function clearForm(formElement) {
    formElement.reset();
    // Clear any validation states
    const inputs = formElement.querySelectorAll('.form-control, .form-select');
    inputs.forEach(input => {
        input.classList.remove('is-valid', 'is-invalid');
    });
}

function setFieldError(fieldElement, message) {
    fieldElement.classList.add('is-invalid');
    
    // Remove existing feedback
    const existingFeedback = fieldElement.parentNode.querySelector('.invalid-feedback');
    if (existingFeedback) {
        existingFeedback.remove();
    }
    
    // Add new feedback
    const feedback = document.createElement('div');
    feedback.className = 'invalid-feedback';
    feedback.textContent = message;
    fieldElement.parentNode.appendChild(feedback);
}

function clearFieldError(fieldElement) {
    fieldElement.classList.remove('is-invalid');
    const feedback = fieldElement.parentNode.querySelector('.invalid-feedback');
    if (feedback) {
        feedback.remove();
    }
}

// Dashboard utilities
function updateStatCard(cardId, value, suffix = '') {
    const element = document.getElementById(cardId);
    if (element) {
        element.textContent = value + suffix;
    }
}

function formatCompatibilityScore(score) {
    if (score >= 80) return `<span class="text-success">${score}%</span>`;
    if (score >= 60) return `<span class="text-warning">${score}%</span>`;
    return `<span class="text-danger">${score}%</span>`;
}

// Date and time utilities
function formatDateTime(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString();
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString();
}

// Preferences helpers
function getPreferenceDisplayText(key, value) {
    const displayMappings = {
        sleep_time: {
            'early evening': 'Early Evening (8-9 PM)',
            'evening': 'Evening (9-10 PM)',
            'late evening': 'Late Evening (10-11 PM)',
            'night': 'Night (11 PM-12 AM)',
            'late night': 'Late Night (12-1 AM)',
            'midnight': 'After Midnight (1+ AM)'
        },
        wake_time: {
            'early morning': 'Early Morning (5-6 AM)',
            'morning': 'Morning (6-7 AM)',
            'late morning': 'Late Morning (7-8 AM)',
            'noon': 'Late Morning (8-9 AM)',
            'afternoon': 'Mid Morning (9-10 AM)',
            'late afternoon': 'After 10 AM'
        },
        study_preference: {
            'group': 'Group Study',
            'individual': 'Individual Study',
            'mixed': 'Mixed (Both)'
        },
        noise_tolerance: {
            'low': 'Low (Need Silence)',
            'medium': 'Medium (Some Noise OK)',
            'high': 'High (Background Noise OK)'
        },
        cleanliness_level: {
            'low': 'Relaxed',
            'medium': 'Moderate',
            'high': 'Very Clean'
        },
        social_preference: {
            'extrovert': 'Extrovert',
            'introvert': 'Introvert',
            'ambivert': 'Ambivert'
        },
        smoking_preference: {
            'non-smoker': 'Non-smoker',
            'smoker': 'Smoker',
            'occasional': 'Occasional'
        }
    };
    
    return displayMappings[key] && displayMappings[key][value] ? 
           displayMappings[key][value] : 
           value.charAt(0).toUpperCase() + value.slice(1);
}

// Local storage utilities
function saveToStorage(key, data) {
    try {
        localStorage.setItem(key, JSON.stringify(data));
    } catch (error) {
        console.error('Error saving to localStorage:', error);
    }
}

function loadFromStorage(key) {
    try {
        const data = localStorage.getItem(key);
        return data ? JSON.parse(data) : null;
    } catch (error) {
        console.error('Error loading from localStorage:', error);
        return null;
    }
}

function removeFromStorage(key) {
    try {
        localStorage.removeItem(key);
    } catch (error) {
        console.error('Error removing from localStorage:', error);
    }
}

// Theme utilities
function toggleTheme() {
    const body = document.body;
    const currentTheme = body.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    
    body.setAttribute('data-theme', newTheme);
    saveToStorage('theme', newTheme);
}

function initTheme() {
    const savedTheme = loadFromStorage('theme') || 'light';
    document.body.setAttribute('data-theme', savedTheme);
}

// Search and filter utilities
function filterTable(tableId, searchTerm) {
    const table = document.getElementById(tableId);
    if (!table) return;
    
    const rows = table.querySelectorAll('tbody tr');
    const term = searchTerm.toLowerCase();
    
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(term) ? '' : 'none';
    });
}

function sortTable(tableId, columnIndex, direction = 'asc') {
    const table = document.getElementById(tableId);
    if (!table) return;
    
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr'));
    
    rows.sort((a, b) => {
        const aValue = a.cells[columnIndex].textContent.trim();
        const bValue = b.cells[columnIndex].textContent.trim();
        
        const comparison = aValue.localeCompare(bValue, undefined, { numeric: true });
        return direction === 'asc' ? comparison : -comparison;
    });
    
    rows.forEach(row => tbody.appendChild(row));
}

// Export utilities
function exportTableToCSV(tableId, filename) {
    const table = document.getElementById(tableId);
    if (!table) return;
    
    const rows = table.querySelectorAll('tr');
    const csvContent = Array.from(rows)
        .map(row => Array.from(row.cells).map(cell => `"${cell.textContent}"`).join(','))
        .join('\n');
    
    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
}

// Initialization
document.addEventListener('DOMContentLoaded', function() {
    // Initialize theme
    initTheme();
    
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialize popovers
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // Add fade-in animation to cards
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        setTimeout(() => {
            card.classList.add('fade-in-up');
        }, index * 100);
    });
});

// Error handling
window.addEventListener('error', function(e) {
    console.error('Global error:', e.error);
    showAlert('An unexpected error occurred. Please refresh the page.', 'danger');
});

// Service worker registration (for future PWA features)
if ('serviceWorker' in navigator) {
    window.addEventListener('load', function() {
        // Register service worker when available
        // navigator.serviceWorker.register('/sw.js');
    });
}