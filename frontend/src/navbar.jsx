import { Link } from "react-router-dom";
import logo from './assets/vecteezy1.jpg'
import config from "./config";
import { useAuthStore } from "./store/authentication";
import * as accountApi from './api/account'

export default function Navbar() {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  const username = useAuthStore((state) => state.username)
  const onSignedOut = useAuthStore((state) => state.onSignedOut)

  //// temporary...
  const onSignedIn = useAuthStore((state) => state.onSignedIn)
  ////

  const signIn = () => {
    // onSignedIn('john_doe1234', 'ADMIN')
  }

  const signOut = async () => {
    await accountApi.signOut()
    onSignedOut()
  }

  const showAuthButtons = !isAuthenticated && config.allowUsersAuthentication

  const signUpButton = <Link className="button is-success" to="/signUp">Зарегистрироваться</Link>
  // const signInButton = <button className="button is-success" onClick={signIn}>Войти</button>
  const signInButton = <Link className="button is-success" to="/signIn">Войти</Link>
  const signOutButton = <button className="button is-light" onClick={signOut}>Выйти</button>

  const userNotSignedInButtonGroup = <div className="navbar-item">
    <div className="buttons">
      {signInButton}
      {signUpButton}
    </div>
  </div>

  const userIsSignedInButtonGroup = <div className="navbar-item">
    <div className="buttons">
      {signOutButton}
    </div>
  </div>

  return <nav className="navbar is-info"
    role="navigation"
    aria-label="main navigation"
    style={{ marginBottom: "2em" }}>
    <div className="navbar-brand">
      <a href="#" className="navbar-item">
        <img src={logo} alt="Логотип"
          height="100" />
      </a>
    </div>
    <div className="navbar-menu">
      <div className="navbar-start">
        <Link className="navbar-item" to="/forums">Форумы</Link>
      </div>
      <div className="navbar-end">
        {isAuthenticated && (
          <div className="navbar-item">
            <span>Вы вошли как
              <Link className="has-text-warning" to={`/account`}> {username}</Link>
            </span>
          </div>
        )}

        {showAuthButtons && userNotSignedInButtonGroup}
        {isAuthenticated && userIsSignedInButtonGroup}
      </div>
    </div>
  </nav>
}