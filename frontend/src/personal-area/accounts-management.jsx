import { useEffect, useMemo, useState } from "react"
import * as accountApi from '../api/account'
import classNames from "classnames"
import { useAuthStore } from "../store/authentication"

export default function AccountsManagement({}) {

  const [accounts, setAccounts] = useState([])
  const [error, setError] = useState(null)

  const [showModal, setShowModal] = useState(false)
  const [accountIdToShowInModal, setAccountIdToShowInModal] = useState(1)

  useEffect(() => {
    const fetchAccounts = async () => {
      setAccounts(await accountApi.getAll())
    }
    fetchAccounts()
  }, [])

  const openAccountModal = (accountId) => {
    setAccountIdToShowInModal(accountId)
    setShowModal(true)
  }

  const closeAccountModal = () => {
    setShowModal(false)
  }

  return (
    <>
      <div className="columns is-centered">
        <div className="column is-narrow box">
          {(accounts.length == 0) && (
            <p>Список аккаунтов пуст</p>
          )}
          {(accounts.length != 0) && (
            <AccountsTable openAccountModal={openAccountModal} accounts={accounts} />
          )}
          <AccountManagementModal 
            accountId={accountIdToShowInModal} 
            accounts={accounts}
            show={showModal}
            onClickClose={closeAccountModal} 
            onAccountDataUpdated={async () => {
              setAccounts(await accountApi.getAll())
            }}
          />
        </div>
      </div>
    </>
  )
}

function AccountManagementModal({ accounts, accountId, show, onClickClose, onAccountDataUpdated }) {

  const account = useMemo(
    () => accounts.find(account => account.id == accountId) ?? {}, 
    [accounts, accountId])

  const actorRole = useAuthStore((store) => store.role)
  const actorCanManageSpecifiedAccountBlocking = actorRole == 'OWNER'
    || (actorRole == 'ADMIN' && account.role != 'OWNER' && account.role != 'ADMIN')

  const modalClasses = classNames('modal', { 'is-active': show })

  // учесть права доступа в зависимости от роли
  const blockAccount = async () => {
    await accountApi.blockAccount(account.id)
    await onAccountDataUpdated()
  }

  const unblockAccount = async () => {
    await accountApi.unblockAccount(account.id)
    await onAccountDataUpdated()
  }

  const [isRoleEdited, setRoleEdited] = useState(false)

  const editRole = () => {
    setRoleEdited(true)
  }

  const roleOptionsNode = ['USER', 'MODER', 'ADMIN', 'OWNER']
    .map((role, index) => (
      <option value={role} key={index}>{role}</option>
    ))
  
  const [selectedRole, setSelectedRole] = useState(account.role)

  const upgradeRole = async () => {
    if (selectedRole == account.role) {
      setRoleEdited(false)
      return
    }
    await accountApi.changeRole(account.id, selectedRole)
    setRoleEdited(false)
    onAccountDataUpdated()
  }

  return (
    <>
      <div className={modalClasses}>
        <div className="modal-background"></div>
        <div className="modal-card">
          <header className="modal-card-head">
            <p className="modal-card-title">Управление аккаунтом</p>
            <button className="delete" onClick={e => onClickClose()}></button>
          </header>
          <section className="modal-card-body">
            <p>Имя пользователя: {account.username}</p>
            <div>
              <span>Роль: </span>  
              <span>
                {!isRoleEdited && (
                  <span>{account.role} </span>
                )}
                {(actorRole == 'OWNER') && (
                  <>
                    {!isRoleEdited && (
                      <a onClick={() => editRole()}>
                        <i className="fa-regular fa-pen-to-square"></i>
                      </a>
                    )}
                    {isRoleEdited && (
                      <>
                      <br />
                        <div className="select" style={{ marginRight: '0.5em' }}>
                          <select
                            value={selectedRole}
                            onChange={e => setSelectedRole(e.target.value)}
                          >
                            {roleOptionsNode}
                          </select>
                        </div>
                        <button 
                          className="button is-success"
                          onClick={upgradeRole}>Сохранить</button>
                      </>
                    )}
                  </>
                )}
              </span>
            </div>
            <p>Заблокирован: {account.blocked ? 'да' : 'нет'}</p>
          </section>
          <footer className="modal-card-foot">
            {(!account.blocked && actorCanManageSpecifiedAccountBlocking) && (
              <button className="button is-danger" onClick={blockAccount}>
                Заблокировать
              </button>
            )}
            {(account.blocked && actorCanManageSpecifiedAccountBlocking) && (
              <button className="button is-danger" onClick={unblockAccount}>
                Разблокировать
              </button>
            )}
          </footer>
        </div>
        
      </div>
    </>
  )
}

function AccountsTable({ accounts, openAccountModal }) {

  const accountsNode = accounts.map(account => (
    <tr key={account.id}>
      <td className="has-text-centered">{account.id}</td>
      <td className="has-text-centered">{account.username}</td>
      <td className="has-text-centered">{account.role}</td>
      <td className="has-text-centered">{account.blocked ? 'Да' : 'Нет'}</td>
      <td className="has-text-centered">
        <button 
          className="button"
          onClick={() => openAccountModal(account.id)}
        >Подробнее</button>
      </td>
    </tr>
  ))

  return (
    <>
      <table className="table accounts-table">
        <thead>
          <tr>
            <th className="has-text-centered">ID</th>
            <th className="has-text-centered">Имя пользователя</th>
            <th className="has-text-centered">Роль</th>
            <th className="has-text-centered">Заблокирован</th>
            <th className="has-text-centered">Управление</th>
          </tr>
        </thead>
        <tbody>
          {accountsNode}
        </tbody>
      </table>
    </>
  )
}