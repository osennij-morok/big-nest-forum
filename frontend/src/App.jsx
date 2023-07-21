import 'bulma/css/bulma.min.css'
import './App.css'
import '@fortawesome/fontawesome-free/js/all'
import { Navigate, Outlet } from 'react-router-dom'
import Navbar from './navbar'
import { useTitleSwitcher } from './utils/title-switcher'

export default function App() {

  useTitleSwitcher()

  return (
    <>
      <Navbar />
      <Navigate to='forums' />
      <div className="main-content">
        <Outlet />
      </div>
    </>
  )
}
