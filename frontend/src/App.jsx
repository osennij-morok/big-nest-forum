import 'bulma/css/bulma.min.css'
import './App.css'
import { Outlet } from 'react-router-dom'
import Navbar from './navbar'
import { useTitleSwitcher } from './utils/title-switcher'
import config from './config'

export default function App() {

  useTitleSwitcher()

  // console.log(`Config:`)
  // console.log(config)

  return (
    <>
      <Navbar />
      <Outlet />
    </>
  )
}
