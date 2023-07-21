import classNames from "classnames";
import { Link, useLocation } from "react-router-dom";
import { useAuthStore } from "../store/authentication";

// export default function DashboardMenu({}) {

//   const location = useLocation()
//   const userRole = useAuthStore(store => store.role)
  
//   console.log(location)

//   return (
//     <>
//       <aside className="menu box">
//         <ul className="menu-list">
//           {/* <li><Link className="is-active" to="change-password">Сменить пароль</Link></li> */}
//           {/* <li><Link to="accounts-management">Управление аккаунтами</Link></li> */}
//           <MenuLink to="change-password" label="Сменить пароль" />
//           {(userRole === 'OWNER' || userRole === 'ADMIN') && (
//             <MenuLink to="accounts-management" label="Управление аккаунтами" />
//           )}
//         </ul>
//       </aside>
//     </>
//   )
// }

export default function DashboardMenu({}) {

  const location = useLocation()
  const userRole = useAuthStore(store => store.role)
  
  console.log(location)

  return (
    <>
      <aside className="tabs is-centered">
        <ul>
          {/* <li><Link className="is-active" to="change-password">Сменить пароль</Link></li> */}
          {/* <li><Link to="accounts-management">Управление аккаунтами</Link></li> */}
          <MenuLink to="change-password" label="Сменить пароль" />
          {(userRole === 'OWNER' || userRole === 'ADMIN') && (
            <MenuLink to="accounts-management" label="Управление аккаунтами" />
          )}
        </ul>
      </aside>
    </>
  )
}

  // const links = [
  //   {
  //     to: 'change-password',
  //     label: 'Сменить пароль',
  //     active: true,
  //   },
  //   {
  //     to: 'accounts-management',
  //     label: 'Управление аккаунтами',
  //     active: false,
  //   },
  // ]

function MenuLink({ to, label }) {

  const location = useLocation()
  const linkClasses = classNames({ 'is-active': location.pathname.endsWith(to) })

  return (
    <>
      <li
        className={linkClasses}
      >
        <Link 
          // className={linkClasses} 
          to={to}
          // onClick={() => }
        >{label}</Link>
      </li>
    </>
  )
}