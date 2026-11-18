import { NavLink, Outlet } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

export function AppShell() {
  const { user, logout } = useAuth()

  return (
    <div className="shell">
      <header className="topbar">
        <NavLink to="/dashboard" className="brand">
          Broker
        </NavLink>
        <nav className="nav">
          <NavLink to="/dashboard">Dashboard</NavLink>
          <NavLink to="/profile">Profile</NavLink>
        </nav>
        <div className="topbar-actions">
          <span className="user-chip">{user?.name}</span>
          <button type="button" className="btn btn-ghost" onClick={() => void logout()}>
            Log out
          </button>
        </div>
      </header>
      <main className="shell-main">
        <Outlet />
      </main>
    </div>
  )
}
