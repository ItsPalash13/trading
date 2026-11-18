import { Link } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

export function DashboardPage() {
  const { user } = useAuth()

  return (
    <section className="page">
      <header className="page-header">
        <p className="eyebrow">Overview</p>
        <h1>Welcome back, {user?.name.split(' ')[0]}</h1>
        <p className="lede">
          Your session is active. Use this desk as the home for account activity and
          quick links.
        </p>
      </header>

      <div className="dashboard-grid">
        <article className="panel">
          <h2>Account status</h2>
          <p className="muted">Signed in as {user?.email}</p>
          <dl className="meta-list">
            <div>
              <dt>User ID</dt>
              <dd>{user?.id}</dd>
            </div>
            <div>
              <dt>Session</dt>
              <dd>Active</dd>
            </div>
          </dl>
        </article>

        <article className="panel">
          <h2>Quick actions</h2>
          <p className="muted">Jump to the places you need most often.</p>
          <div className="action-row">
            <Link className="btn btn-primary" to="/profile">
              View profile
            </Link>
            <Link className="btn btn-ghost" to="/login">
              Switch account
            </Link>
          </div>
        </article>

        <article className="panel panel-wide">
          <h2>Market desk</h2>
          <p className="muted">
            Placeholder workspace for future orders, positions, and watchlists once those
            APIs are wired in.
          </p>
          <div className="ticker-strip" aria-hidden="true">
            <span>NIFTY · watch</span>
            <span>BANKNIFTY · watch</span>
            <span>RELIANCE · watch</span>
            <span>TCS · watch</span>
          </div>
        </article>
      </div>
    </section>
  )
}
