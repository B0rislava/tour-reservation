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

// Function to render the navigation bar
async function renderNav(activePage = '') {
    const navLinksContainer = document.querySelector('.nav-links');
    if (!navLinksContainer) return;

    const { isAuthenticated, user } = await checkAuth();

    let navHtml = `
        <a href="/" class="${activePage === 'discover' ? 'active' : ''}">Discover</a>
    `;

    if (isAuthenticated && user) {
        const initial = user.firstName ? user.firstName.charAt(0).toUpperCase() : 'U';
        
        if (user.role === 'GUIDE') {
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
    const isFavorited = iconElement.innerText.trim() === '❤️';
    const method = isFavorited ? 'DELETE' : 'POST';

    try {
        const response = await fetch(`/api/v1/users/favorites/${tourId}`, {
            method: method
        });

        if (response.ok) {
            iconElement.innerText = isFavorited ? '🤍' : '❤️';

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
