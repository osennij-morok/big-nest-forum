import classNames from "classnames"

export default function SubmitField({ text, isLoading }) {
  const buttonClasses = classNames('button', 'is-info', { 'is-loading': isLoading })

  return (
    <>
      <div className="field">
        <button type="submit" className={buttonClasses}>{text}</button>
      </div>
    </>
  )
}