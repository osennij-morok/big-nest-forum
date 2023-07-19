import ChangePasswordForm from "./change-password-form";

export default function AccountDashboard({  }) {



  return (
    <>
      <div className="columns is-centered">
        <div className="column is-full">
          <div className="columns is-centered">
            <div className="column is-narrow box">
              <ChangePasswordForm />
            </div>
          </div>
        </div>
      </div>
    </>
  )
}