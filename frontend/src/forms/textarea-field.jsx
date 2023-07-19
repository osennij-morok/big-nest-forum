import classNames from "classnames"

export default function TextareaField({
  label, name, cols, rows, register, errors, additionalError, handleSubmit
}) {
  
  const textareaClasses = classNames('textarea', { 
    'is-danger': errors[name] || additionalError 
  })

  const onKeyUp = e => {
    if (e.key === 'Enter' && e.shiftKey) {
      console.log(e)
      handleSubmit(e)
    }
  }

  return (
    <>
      <div className="field">
        <label className="label">{label}</label>
        <div className="control">
          <textarea cols={cols} rows={rows} 
                    className={textareaClasses}
                    {...register(name)}
                    onKeyUp={onKeyUp}></textarea>
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