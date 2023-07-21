import { Navigate, Outlet } from "react-router-dom";
import DashboardMenu from "./dashboard-menu";
import './dashboard.css'
import { useAuthStore } from '../store/authentication'

export default function AccountDashboard({  }) {

  const actorRole = useAuthStore(store => store.role)

  return (
    <div className="dashboard">
      <Navigate to='change-password' />
      {(actorRole === 'ADMIN' || actorRole === 'OWNER') && (
        <div className="columns is-centered">
          <div className="column is-narrow">
            <DashboardMenu />
          </div>

          {/* <div className="column">
          <Outlet />
        </div> */}
        </div>
      )}
      <div className="columns is-centered is-vcentered dashboard-body">
        <div className="column is-narrow">
          <Outlet />
        </div>
      </div>
    </div>
  )
}