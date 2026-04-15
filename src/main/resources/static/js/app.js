function getCsrfToken() {
    return document.cookie.split('; ')
        .find(row => row.startsWith('XSRF-TOKEN='))
        ?.split('=')[1];
}

async function checkAuth() {
    try {
        const response = await fetch('/api/v1/users/profile');
        if (response.ok) {
            const user = await response.json();
            return { isAuthenticated: true, user };
        }
    } catch (e) {
        console.error("Auth check failed", e);
    }
    return { isAuthenticated: false, user: null };
}

async function renderNav(activePage = '') {
    const navLinksContainer = document.querySelector('.nav-links');
    if (!navLinksContainer) return;

    const { isAuthenticated, user } = await checkAuth();

    let navHtml = `
        <a href="/" class="${activePage === 'discover' ? 'active' : ''}">Discover</a>
    `;

    if (isAuthenticated && user) {
        const initial = user.firstName ? user.firstName.charAt(0).toUpperCase() : 'U';

        if (user.role === 'ADMIN') {
            navHtml += `
                <a href="/admin/dashboard.html" class="${activePage === 'admin-dashboard' ? 'active' : ''}">Admin Panel</a>
            `;
        } else if (user.role === 'GUIDE') {
            navHtml += `
                <a href="/my-tours.html" class="${activePage === 'my-tours' ? 'active' : ''}">My Tours</a>
                <a href="/create-tour.html" class="${activePage === 'create-tour' ? 'active' : ''}">Create a Tour</a>
            `;
        } else {
            navHtml += `
                <a href="/bookings.html" class="${activePage === 'bookings' ? 'active' : ''}">Bookings</a>
                <a href="/favorites.html" class="${activePage === 'favorites' ? 'active' : ''}">Favorites</a>
            `;
        }

        navHtml += `
            <a href="/profile.html" class="${activePage === 'profile' ? 'active' : ''}">
                <div class="profile-pic">
                    <span>${initial}</span>
                </div>
            </a>
        `;
    } else {
        navHtml += `
            <a href="/login.html" class="btn-book" style="padding: 0.5rem 1.5rem;">Login</a>
        `;
    }

    navLinksContainer.innerHTML = navHtml;
}

// Ensure the standard header HTML exists
function initNav(activePage) {
    const header = document.querySelector('header.navbar');
    if (header && !header.innerHTML.trim().includes('nav-links')) {
        header.innerHTML = `
            <a href="/" style="text-decoration: none; color: inherit;">
                <div class="logo">Tourly</div>
            </a>
            <nav class="nav-links"></nav>
        `;
    }
    renderNav(activePage);
}

async function getUserFavorites() {
    try {
        const response = await fetch('/api/v1/users/favorites');
        if (response.ok) {
            const tours = await response.json();
            return tours.map(tour => tour.id);
        }
    } catch (e) {
        console.error("Error fetching favorites", e);
    }
    return [];
}

async function toggleFavorite(tourId, iconElement) {
    const isFavorited = iconElement.getAttribute('data-favorited') === 'true';
    const method = isFavorited ? 'DELETE' : 'POST';

    try {
        const response = await fetch(`/api/v1/users/favorites/${tourId}`, {
            method: method,
            headers: {
                'X-XSRF-TOKEN': getCsrfToken()
            }
        });

        if (response.ok) {
            const heartFilled = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="currentColor" color="#e63946"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg>`;
            const heartOutline = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" color="white"><path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" /></svg>`;

            iconElement.innerHTML = isFavorited ? heartOutline : heartFilled;
            iconElement.setAttribute('data-favorited', (!isFavorited).toString());

            if (isFavorited && window.location.pathname.includes('favorites.html')) {
                const card = iconElement.closest('.tour-card');
                if (card) {
                    card.remove();
                    const container = document.getElementById('tours-container');
                    const remainingCards = container.querySelectorAll('.tour-card');
                    if (remainingCards.length === 0) {
                        const emptyWarning = document.getElementById('empty-warning');
                        if (emptyWarning) emptyWarning.style.display = 'block';
                    }
                }
            }
        } else if (response.status === 401) {
            window.location.href = '/login.html';
        } else {
            console.error('Failed to toggle favorite');
        }
    } catch (e) {
        console.error("Error toggling favorite", e);
    }
}

function formatDuration(d) {
    if (!d) return '';
    if (d.includes(':')) {
        const parts = d.split(':');
        const h = parseInt(parts[0], 10);
        const m = parseInt(parts[1], 10);
        return `${h > 0 ? h + ' hours' : ''}${h > 0 && m > 0 ? ' ' : ''}${m > 0 ? m + ' mins' : ''}`.trim();
    }
    return d;
}

function formatDate(dateStr) {
    if (!dateStr) return 'TBD';
    try {
        const d = new Date(dateStr);
        if (isNaN(d.getTime())) return dateStr;
        return d.toLocaleDateString('en-GB', { day: 'numeric', month: 'short', year: 'numeric' });
    } catch (e) {
        return dateStr;
    }
}
