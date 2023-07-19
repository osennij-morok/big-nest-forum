import classNames from "classnames"

export default function SelectField({ label, name, register, errors, 
                                      additionalError, isLoading, defaultValue, children }) {
  const selectCategoryClasses = classNames('select', { 'is-loading': isLoading })

  return (
    <>
      <div className="field">
        <label className="label">{label}</label>
        <div className="control">
          <div className={selectCategoryClasses}>
            <select {...register(name)}>
              <option value={defaultValue} defaultChecked></option>
              {children}
            </select>
          </div>
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