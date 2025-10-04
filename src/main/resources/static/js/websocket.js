let stompClient = null;

function connectWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function (frame) {
        
        stompClient.subscribe('/topic/organizations', function (message) {
            const event = JSON.parse(message.body);
            handleOrganizationEvent(event);
        });
    }, function(error) {
        console.error('WebSocket connection error:', error);
        
        setTimeout(() => {
            connectWebSocket();
        }, 5000);
    });
}

function handleOrganizationEvent(event) {
    
    const currentPath = window.location.pathname;
    
    if (currentPath === '/organizations' || currentPath === '/') {
        handleListPageEvent(event);
    }
    
    showEventToast(event);
}

function handleListPageEvent(event) {
    const table = document.querySelector('tbody');
    if (!table) return;
    
    switch (event.eventType) {
        case 'CREATED':
            if (event.organization) {
                addOrganizationRow(event.organization);
            }
            break;
            
        case 'UPDATED':
        case 'DISMISSED':
            if (event.organization) {
                updateOrganizationRow(event.organization);
            }
            break;
            
        case 'DELETED':
            if (event.organizationId) {
                removeOrganizationRow(event.organizationId);
            }
            break;
    }
}

function addOrganizationRow(organization) {
    const table = document.querySelector('tbody');
    if (!table) return;
    
    const row = document.createElement('tr');
    row.setAttribute('data-org-id', organization.id);
    row.innerHTML = createOrganizationRowHTML(organization);
    
    table.insertBefore(row, table.firstChild);
    
    row.classList.add('table-success');
    setTimeout(() => row.classList.remove('table-success'), 3000);
}

function updateOrganizationRow(organization) {
    const row = document.querySelector(`tr[data-org-id="${organization.id}"]`);
    if (!row) return;
    
    row.innerHTML = createOrganizationRowHTML(organization);
    
    row.classList.add('table-warning');
    setTimeout(() => row.classList.remove('table-warning'), 3000);
}

function removeOrganizationRow(organizationId) {
    const row = document.querySelector(`tr[data-org-id="${organizationId}"]`);
    if (!row) return;
    
    row.classList.add('table-danger');
    setTimeout(() => {
        row.style.transition = 'opacity 0.5s';
        row.style.opacity = '0';
        setTimeout(() => row.remove(), 500);
    }, 1000);
}

function createOrganizationRowHTML(organization) {
    const coordinates = organization.coordinates || {};
    const creationDate = organization.creationDate ? 
        new Date(organization.creationDate).toLocaleString('ru-RU') : '';
    
    return `
        <td>${organization.id}</td>
        <td>
            <div class="fw-bold">${organization.name}</div>
            ${organization.fullName ? `<small class="text-muted">${organization.fullName}</small>` : ''}
        </td>
        <td>
            ${coordinates.x !== undefined && coordinates.y !== undefined ? 
                `<small>X: ${coordinates.x}<br>Y: ${coordinates.y}</small>` : ''}
        </td>
        <td>${creationDate}</td>
        <td><span class="badge bg-info">${organization.employeesCount || 0}</span></td>
        <td><span class="badge bg-warning text-dark">${organization.rating || ''}</span></td>
        <td>
            ${organization.type ? 
                `<span class="badge bg-secondary">${organization.type}</span>` : 
                '<span class="text-muted">—</span>'}
        </td>
        <td>
            <div class="btn-group btn-group-sm" role="group">
                <a href="/organizations/${organization.id}" class="btn btn-outline-primary" title="Просмотр">
                    <i class="bi bi-eye"></i>
                </a>
                <a href="/organizations/${organization.id}/edit" class="btn btn-outline-warning" title="Редактировать">
                    <i class="bi bi-pencil"></i>
                </a>
                <button type="button" class="btn btn-outline-danger" 
                        data-bs-toggle="modal" 
                        data-bs-target="#deleteModal${organization.id}"
                        title="Удалить">
                    <i class="bi bi-trash"></i>
                </button>
            </div>
        </td>
    `;
}

function showEventToast(event) {
    let message = '';
    let bgClass = 'bg-info';
    
    switch (event.eventType) {
        case 'CREATED':
            message = `Создана новая организация: ${event.organization?.name || 'Неизвестно'}`;
            bgClass = 'bg-success';
            break;
        case 'UPDATED':
            message = `Обновлена организация: ${event.organization?.name || 'ID ' + event.organizationId}`;
            bgClass = 'bg-primary';
            break;
        case 'DELETED':
            message = `Удалена организация с ID: ${event.organizationId}`;
            bgClass = 'bg-danger';
            break;
        case 'DISMISSED':
            message = `Уволены все сотрудники организации: ${event.organization?.name || 'ID ' + event.organizationId}`;
            bgClass = 'bg-warning';
            break;
    }
    
    if (message) {
        showToast(message, bgClass);
    }
}

function showToast(message, bgClass = 'bg-info') {
    let toastContainer = document.getElementById('toast-container');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toast-container';
        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
        toastContainer.style.zIndex = '9999';
        document.body.appendChild(toastContainer);
    }
    
    const toastId = 'toast-' + Date.now();
    const toastHTML = `
        <div id="${toastId}" class="toast align-items-center text-white ${bgClass}" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-bell me-2"></i>${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `;
    
    toastContainer.insertAdjacentHTML('beforeend', toastHTML);
    
    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement, {
        autohide: true,
        delay: 5000
    });
    
    toast.show();
    
    toastElement.addEventListener('hidden.bs.toast', () => {
        toastElement.remove();
    });
}

document.addEventListener('DOMContentLoaded', function() {
    if (typeof SockJS !== 'undefined' && typeof Stomp !== 'undefined') {
        connectWebSocket();
    }
});

window.addEventListener('beforeunload', function() {
    if (stompClient !== null && stompClient.connected) {
        stompClient.disconnect();
    }
});
