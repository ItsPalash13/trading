import { useGetProfileQuery } from '../store/api'
import { getErrorMessage } from '../store/errorMessage'

export function ProfilePage() {
  const { data: profile, isLoading, error } = useGetProfileQuery()

  return (
    <section className="page">
      <header className="page-header">
        <p className="eyebrow">Account</p>
        <h1>Profile</h1>
        <p className="lede">Details returned from the broker user API.</p>
      </header>

      <article className="panel profile-panel">
        {isLoading ? <p className="muted">Loading profile…</p> : null}
        {error ? (
          <p className="form-error">{getErrorMessage(error, 'Could not load profile')}</p>
        ) : null}

        {!isLoading && !error && profile ? (
          <dl className="profile-list">
            <div>
              <dt>Name</dt>
              <dd>{profile.name}</dd>
            </div>
            <div>
              <dt>Email</dt>
              <dd>{profile.email}</dd>
            </div>
            <div>
              <dt>User ID</dt>
              <dd>{profile.id}</dd>
            </div>
          </dl>
        ) : null}
      </article>
    </section>
  )
}
