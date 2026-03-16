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
        navHtml += `
            <a href="/bookings.html" class="${activePage === 'bookings' ? 'active' : ''}">Bookings</a>
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
