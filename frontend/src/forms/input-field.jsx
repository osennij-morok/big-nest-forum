import classNames from "classnames"

export function InputField({ label, name, type, register, errors, additionalError }) {
  const inputClass = classNames('input', { 'is-danger': errors[name] })

  return (
    <>
      <div className="field">
        <label className="label">{label}</label>
        <div className="control">
          <input type={type} className={inputClass} {...register(name)} />
        </div>
        {errors[name] && (
          <p className="help is-danger">{errors[name]?.message}</p>
        )}
        {additionalError && (
          <p className="help is-danger">{additionalError.message}</p>
        )}
      </div>
    </>
  )
}

export function TextField(props) {
  return (<InputField {...props} type="text" />)
}

export function PasswordField(props) {
  return (<InputField {...props} type="password" />)
}