import { Link, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export function Layout() {
  const { displayName, role, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="app-shell">
      <header className="topbar">
        <Link to="/" className="brand">
          Digital Detox
        </Link>
        {isAuthenticated && (
          <nav>
            <Link to="/plans">Plans</Link>
            {role === 'ADMIN' && <Link to="/admin">Admin</Link>}
            <span className="user-chip">
              {displayName} ({role})
            </span>
            <button type="button" onClick={handleLogout}>
              Logout
            </button>
          </nav>
        )}
      </header>
      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}
